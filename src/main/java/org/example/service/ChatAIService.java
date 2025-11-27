package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.response.AI.ChatAIResponse;
import org.example.dto.response.AI.ChatResponse;
import org.example.dto.response.generatedCv.GeneratedCvPreviewResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service xử lý các chức năng chat AI cơ bản
 * AI tự động hiểu intent giống ChatGPT - linh động và thông minh
 */
@Service
public class ChatAIService {
    
    /**
     * Enum định nghĩa các loại intent của người dùng
     */
    private enum UserIntent {
        CREATE_CV,          // Người dùng muốn tạo CV
        CV_RELATED,         // Câu hỏi về CV (tư vấn, hướng dẫn)
        GENERAL_GREETING,   // Chào hỏi chung, cần hỗ trợ
        OFF_TOPIC           // Câu hỏi không liên quan đến CV
    }

    private final RobustChatService robustChatService;
    private final MarkdownFormatterService markdownFormatterService;
    private final GeneratedCvService generatedCvService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Keywords liên quan đến CV và xin việc - sử dụng Set để tìm kiếm nhanh hơn
    private static final Set<String> CV_KEYWORDS = new HashSet<>(Set.of(
        // CV và Resume
        "cv", "resume", "sơ yếu lý lịch", "đơn xin việc", "hồ sơ xin việc", "mẫu cv", "template cv", 
        "tạo cv", "viết cv", "làm cv", "thiết kế cv", "format cv", "cấu trúc cv",
        
        // Tìm việc và xin việc
        "xin việc", "tuyển dụng", "ứng tuyển", "tìm việc", "tìm việc làm", "việc làm", 
        "tuyển nhân viên", "job", "career", "employment", "job search", "tìm công việc",
        
        // Phỏng vấn
        "phỏng vấn", "interview", "phỏng vấn xin việc", "chuẩn bị phỏng vấn",
        
        // Nội dung CV
        "kinh nghiệm làm việc", "kỹ năng", "học vấn", "bằng cấp", "mục tiêu nghề nghiệp", 
        "giới thiệu bản thân", "mô tả bản thân", "thông tin cá nhân", "liên hệ",
        "thư xin việc", "cover letter", "portfolio", "profile",
        
        // Công việc và nghề nghiệp
        "mô tả công việc", "job description", "nghề nghiệp", "nghề", "công việc",
        "nhân viên", "chuyên viên", "manager", "director", "executive", 
        "intern", "thực tập", "ứng viên", "candidate",
        
        // Các ngành nghề cụ thể
        "phá chế", "bartender", "barista", "nhà hàng", "khách sạn", "lễ tân", 
        "bảo vệ", "tài xế", "kỹ sư", "developer", "lập trình", "designer", 
        "marketing", "sales", "kế toán", "nhân sự", "giáo viên", "bác sĩ", 
        "y tá", "dược sĩ", "luật sư", "kiến trúc sư", "kỹ thuật viên",
        
        // Từ khóa tiếng Anh
        "work experience", "education", "skills", "qualification", "certificate",
        "reference", "achievement", "objective", "summary"
    ));
    
    // Keywords cho intent tạo CV
    private static final Set<String> CREATE_CV_KEYWORDS = new HashSet<>(Set.of(
        "tạo cv", "làm cv", "viết cv", "generate cv", "create cv", "make cv",
        "tạo resume", "làm resume", "viết resume", "generate resume", "create resume",
        "tạo sơ yếu lý lịch", "làm sơ yếu lý lịch", "viết sơ yếu lý lịch",
        "tôi muốn tạo", "tôi cần tạo", "giúp tôi tạo", "hãy tạo cho tôi"
    ));

    public ChatAIService(RobustChatService robustChatService, 
                        MarkdownFormatterService markdownFormatterService,
                        GeneratedCvService generatedCvService) {
        this.robustChatService = robustChatService;
        this.markdownFormatterService = markdownFormatterService;
        this.generatedCvService = generatedCvService;
    }
    
    /**
     * Phân loại intent của người dùng bằng AI - giống ChatGPT
     * AI tự hiểu ngữ cảnh và phân loại chính xác
     */
    private UserIntent classifyIntent(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return UserIntent.GENERAL_GREETING;
        }
        
        // Fast check: nếu có keyword rõ ràng thì không cần gọi AI
        String lowerPrompt = prompt.toLowerCase().trim();
        
        // Kiểm tra greeting patterns
        if (lowerPrompt.matches("^(hi|hello|chào|xin chào|hey|good morning|good afternoon|good evening).*") ||
            lowerPrompt.matches(".*(giúp tôi|tôi cần hỗ trợ|help|assist|hỗ trợ).*") ||
            lowerPrompt.length() < 10) {
            // Có thể là greeting, nhưng để AI quyết định chính xác
        }
        
        // Gọi AI để phân loại intent - prompt ngắn gọn để nhanh
        String classificationPrompt = String.format(
            "Phân loại câu sau thành 1 trong 4 loại: CREATE_CV, CV_RELATED, GENERAL_GREETING, OFF_TOPIC\n" +
            "CREATE_CV: người dùng muốn tạo/làm/viết CV mới\n" +
            "CV_RELATED: câu hỏi về CV, tư vấn CV, hướng dẫn CV\n" +
            "GENERAL_GREETING: chào hỏi, xin hỗ trợ chung, chưa rõ mục đích\n" +
            "OFF_TOPIC: không liên quan CV (thời tiết, thể thao, tin tức...)\n\n" +
            "Câu: \"%s\"\n" +
            "Chỉ trả về 1 từ: CREATE_CV, CV_RELATED, GENERAL_GREETING, hoặc OFF_TOPIC",
            prompt
        );
        
        try {
            String aiResponse = robustChatService.askAI(classificationPrompt).trim().toUpperCase();
            
            // Parse response
            if (aiResponse.contains("CREATE_CV")) {
                return UserIntent.CREATE_CV;
            } else if (aiResponse.contains("CV_RELATED")) {
                return UserIntent.CV_RELATED;
            } else if (aiResponse.contains("GENERAL_GREETING") || aiResponse.contains("GREETING")) {
                return UserIntent.GENERAL_GREETING;
            } else if (aiResponse.contains("OFF_TOPIC") || aiResponse.contains("OFFTOPIC")) {
                return UserIntent.OFF_TOPIC;
            }
            
            // Fallback: nếu AI không trả về đúng format, dùng keyword matching
            return fallbackIntentClassification(prompt);
            
        } catch (Exception e) {
            // Nếu AI fail, fallback về keyword matching
            System.err.println("Intent classification failed, using fallback: " + e.getMessage());
            return fallbackIntentClassification(prompt);
        }
    }
    
    /**
     * Fallback classification bằng keyword matching khi AI không available
     */
    private UserIntent fallbackIntentClassification(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return UserIntent.GENERAL_GREETING;
        }
        
        String lowerPrompt = prompt.toLowerCase();
        
        // Kiểm tra greeting
        if (lowerPrompt.matches("^(hi|hello|chào|xin chào|hey).*") ||
            lowerPrompt.matches(".*(giúp tôi|tôi cần hỗ trợ|help|assist).*") ||
            lowerPrompt.length() < 10) {
            return UserIntent.GENERAL_GREETING;
        }
        
        // Kiểm tra create CV keywords
        for (String keyword : CREATE_CV_KEYWORDS) {
            if (lowerPrompt.contains(keyword)) {
                return UserIntent.CREATE_CV;
            }
        }
        
        // Kiểm tra CV related keywords
        for (String keyword : CV_KEYWORDS) {
            if (lowerPrompt.contains(keyword)) {
                return UserIntent.CV_RELATED;
            }
        }
        
        // Mặc định là off-topic nếu không match gì
        return UserIntent.OFF_TOPIC;
    }
    
   
    private String getRejectionMessage() {
        return """
            Xin lỗi, tôi chỉ hỗ trợ các câu hỏi liên quan đến CV (Sơ yếu lý lịch) và tìm việc làm.
            
            Tôi có thể giúp bạn:
            ✅ Tìm mẫu CV phù hợp với ngành nghề của bạn
            ✅ Tạo CV chuyên nghiệp
            ✅ Hướng dẫn viết CV hiệu quả
            ✅ Gợi ý cách trình bày thông tin trong CV
            ✅ Tư vấn về các phần quan trọng trong CV (thông tin cá nhân, kinh nghiệm, kỹ năng, học vấn)
            ✅ Hướng dẫn về thư xin việc (cover letter)
            ✅ Tư vấn về quy trình xin việc và phỏng vấn
            
            Vui lòng đặt câu hỏi về CV hoặc tìm việc để tôi có thể hỗ trợ bạn tốt nhất.
            """;
    }

    /**
     * Tìm kiếm template CV phù hợp dựa trên yêu cầu người dùng
     */
    public List<String> findTemplates(String userQuery) {
        String prompt = """
            Bạn là hệ thống gợi ý template CV. Người dùng hỏi:
            %s
            Trả về danh sách tên template phù hợp, dạng JSON array.
            """.formatted(userQuery);

        String aiResponse = robustChatService.askAI(prompt);

        try {
            // Clean up response if it contains markdown code blocks
            if (aiResponse.contains("```json")) {
                aiResponse = aiResponse.substring(aiResponse.indexOf("```json") + 7);
                if (aiResponse.contains("```")) {
                    aiResponse = aiResponse.substring(0, aiResponse.indexOf("```"));
                }
            } else if (aiResponse.contains("```")) {
                 aiResponse = aiResponse.substring(aiResponse.indexOf("```") + 3);
                 if (aiResponse.contains("```")) {
                    aiResponse = aiResponse.substring(0, aiResponse.indexOf("```"));
                }
            }
            
            return objectMapper.readValue(aiResponse.trim(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing AI response: " + aiResponse);
            return Collections.emptyList();
        }
    }

    /**
     * Chat với AI về CV và xin việc - Trợ lý ảo hoàn chỉnh
     * AI tự động hiểu intent giống ChatGPT - linh động và thông minh
     */
    public ChatResponse chatWithIntent(String prompt) {
        // Phân loại intent bằng AI
        UserIntent intent = classifyIntent(prompt);
        
        // Xử lý theo từng loại intent
        switch (intent) {
            case CREATE_CV:
                return handleCreateCVIntent(prompt);
                
            case CV_RELATED:
                return handleCVRelatedIntent(prompt);
                
            case GENERAL_GREETING:
                return handleGeneralGreetingIntent();
                
            case OFF_TOPIC:
            default:
                return ChatResponse.textOnly(getRejectionMessage(), null);
        }
    }
    
    /**
     * Xử lý khi người dùng muốn tạo CV
     */
    private ChatResponse handleCreateCVIntent(String prompt) {
        try {
            GeneratedCvPreviewResponse cvPreview = generatedCvService.generateCVPreview(prompt);
            
            if ("success".equals(cvPreview.getStatus())) {
                // Tạo text response ngắn gọn
                String textResponse = String.format(
                    "Tôi đã tạo CV cho bạn với tiêu đề: %s\n\n" +
                    "CV đã được tạo thành công! Bạn có thể xem preview ở bên dưới.",
                    cvPreview.getTitle()
                );
                
                // Tạo CV preview data
                ChatResponse.CvPreviewData cvData = ChatResponse.CvPreviewData.builder()
                        .title(cvPreview.getTitle())
                        .dataJson(cvPreview.getDataJson())
                        .styleJson(cvPreview.getStyleJson())
                        .htmlOutput(cvPreview.getHtmlOutput())
                        .build();
                
                return ChatResponse.withCv(textResponse, cvData, null);
            } else {
                // Nếu tạo CV thất bại, trả về text response với lời giải thích
                String errorMsg = cvPreview.getErrorMessage() != null 
                    ? cvPreview.getErrorMessage() 
                    : "Có lỗi xảy ra khi tạo CV";
                return ChatResponse.textOnly(
                    "Xin lỗi, tôi không thể tạo CV lúc này. " + errorMsg + 
                    "\n\nVui lòng thử lại hoặc cung cấp thêm thông tin về CV bạn muốn tạo.", 
                    null
                );
            }
        } catch (Exception e) {
            return ChatResponse.textOnly(
                "Xin lỗi, có lỗi xảy ra khi tạo CV. Vui lòng thử lại sau.", 
                null
            );
        }
    }
    
    /**
     * Xử lý khi người dùng hỏi về CV (tư vấn, hướng dẫn)
     */
    private ChatResponse handleCVRelatedIntent(String prompt) {
        // Trả về text response với prompt tối ưu
        String optimizedPrompt = buildOptimizedPrompt(prompt);
        RobustChatService.ResponseWithModel response = robustChatService.askAIWithModel(optimizedPrompt);
        return ChatResponse.textOnly(response.getContent(), response.getModel());
    }
    
    /**
     * Xử lý khi người dùng chào hỏi hoặc cần hỗ trợ chung
     * AI sẽ hỏi lại để hiểu rõ hơn nhu cầu
     */
    private ChatResponse handleGeneralGreetingIntent() {
        String greetingResponse = """
            Xin chào! Tôi là trợ lý AI chuyên về CV và tìm việc làm. 
            
            Tôi có thể giúp bạn:
            ✅ Tạo CV chuyên nghiệp
            ✅ Tư vấn về cách viết CV hiệu quả
            ✅ Hướng dẫn trình bày CV
            ✅ Tìm mẫu CV phù hợp
            ✅ Tư vấn về phỏng vấn xin việc
            
            Bạn muốn tôi giúp gì về CV hôm nay?
            """;
        
        return ChatResponse.textOnly(greetingResponse, null);
    }
    
    /**
     * Chat với AI về CV và xin việc (backward compatibility)
     * Chỉ trả lời các câu hỏi liên quan đến CV, tìm CV, tạo CV
     */
    public String chat(String prompt) {
        ChatResponse response = chatWithIntent(prompt);
        return response.getResponse() != null ? response.getResponse() : "";
    }
    
    /**
     * Tạo prompt tối ưu cho AI - ngắn gọn và tự nhiên như ChatGPT
     */
    private String buildOptimizedPrompt(String userPrompt) {
        return String.format(
            "Bạn là trợ lý AI thân thiện chuyên về CV và tìm việc. " +
            "Trả lời tự nhiên, hữu ích như một người bạn tư vấn. " +
            "Nếu câu hỏi không liên quan CV, nhẹ nhàng hướng người dùng về chủ đề CV.\n\n" +
            "Người dùng: %s",
            userPrompt
        );
    }
    
    /**
     * Chat với AI và trả về response đã format HTML
     * Sử dụng intent classification để xử lý linh động
     */
    public ChatAIResponse chatFormatted(String prompt) {
        UserIntent intent = classifyIntent(prompt);
        
        String responseText;
        String model = null;
        
        switch (intent) {
            case CREATE_CV:
                // Với create CV, trả về thông báo hướng dẫn
                responseText = "Tôi đã nhận được yêu cầu tạo CV của bạn. CV đang được tạo, bạn có thể xem preview ở response khác.";
                break;
                
            case GENERAL_GREETING:
                responseText = handleGeneralGreetingIntent().getResponse();
                break;
                
            case OFF_TOPIC:
                responseText = getRejectionMessage();
                break;
                
            case CV_RELATED:
            default:
                String optimizedPrompt = buildOptimizedPrompt(prompt);
                RobustChatService.ResponseWithModel aiResponse = robustChatService.askAIWithModel(optimizedPrompt);
                responseText = aiResponse.getContent();
                model = aiResponse.getModel();
                break;
        }
        
        String htmlContent = markdownFormatterService.markdownToHtml(responseText);
        return ChatAIResponse.formatted(htmlContent, responseText, model);
    }
}
