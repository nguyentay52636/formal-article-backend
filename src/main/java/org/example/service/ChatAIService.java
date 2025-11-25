package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.response.AI.ChatAIResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Service xử lý các chức năng chat AI cơ bản
 * Các chức năng tạo CV đã được chuyển sang GeneratedCvService
 */
@Service
public class ChatAIService {

    private final RobustChatService robustChatService;
    private final MarkdownFormatterService markdownFormatterService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Keywords liên quan đến CV và xin việc
    private static final String[] CV_KEYWORDS = {
        "cv", "resume", "sơ yếu lý lịch", "đơn xin việc", "hồ sơ xin việc", "mẫu cv", "template cv", 
        "tạo cv", "viết cv", "xin việc", "tuyển dụng", "ứng tuyển", "phỏng vấn", 
        "kinh nghiệm làm việc", "kỹ năng", "học vấn", "bằng cấp", "mục tiêu nghề nghiệp", 
        "thư xin việc", "cover letter", "portfolio", "profile", "giới thiệu bản thân",
        "mô tả công việc", "job description", "nghề nghiệp", "việc làm", "tìm việc", 
        "tuyển nhân viên", "nhân viên", "chuyên viên", "manager", "director", "executive", 
        "intern", "thực tập", "phá chế", "bartender", "barista", "nhà hàng", "khách sạn", 
        "lễ tân", "bảo vệ", "tài xế", "kỹ sư", "developer", "lập trình", "designer", 
        "marketing", "sales", "kế toán", "nhân sự", "giáo viên", "bác sĩ", "y tá", 
        "dược sĩ", "luật sư", "kiến trúc sư", "kỹ thuật viên", "công việc", "nghề",
        "ứng viên", "candidate", "job", "career", "employment"
    };

    public ChatAIService(RobustChatService robustChatService, MarkdownFormatterService markdownFormatterService) {
        this.robustChatService = robustChatService;
        this.markdownFormatterService = markdownFormatterService;
    }
    
    /**
     * Kiểm tra xem prompt có liên quan đến CV và xin việc không
     */
    private boolean isCVRelated(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return false;
        }
        
        String lowerPrompt = prompt.toLowerCase();
        
        for (String keyword : CV_KEYWORDS) {
            if (lowerPrompt.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Tạo thông báo từ chối khi prompt không liên quan đến CV
     */
    private String getRejectionMessage() {
        return """
            Xin lỗi, tôi chỉ hỗ trợ các câu hỏi liên quan đến CV (Sơ yếu lý lịch) và tìm việc làm.
            
            Tôi có thể giúp bạn:
            - Tạo mẫu CV cho các ngành nghề khác nhau
            - Hướng dẫn viết CV chuyên nghiệp
            - Gợi ý cách trình bày thông tin trong CV
            - Tư vấn về các phần quan trọng trong CV
            - Tìm mẫu CV phù hợp với ngành nghề của bạn
            
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
     * Chat với AI về CV và xin việc
     */
    public String chat(String prompt) {
        // Kiểm tra xem prompt có liên quan đến CV không
        if (!isCVRelated(prompt)) {
            return getRejectionMessage();
        }
        
        String enhancedPrompt = """
            Bạn là trợ lý AI chuyên về CV (Sơ yếu lý lịch) và tìm việc làm. 
            Bạn CHỈ trả lời các câu hỏi liên quan đến:
            - Tạo mẫu CV cho các ngành nghề
            - Hướng dẫn viết CV
            - Tư vấn về CV và xin việc
            - Gợi ý cách trình bày CV
            
            Nếu câu hỏi không liên quan đến CV hoặc tìm việc, hãy nhẹ nhàng từ chối và hướng dẫn người dùng đặt câu hỏi về CV.
            
            Câu hỏi của người dùng: %s
            """.formatted(prompt);
        
        return robustChatService.askAI(enhancedPrompt);
    }
    
    /**
     * Chat với AI và trả về response đã format HTML
     */
    public ChatAIResponse chatFormatted(String prompt) {
        // Kiểm tra xem prompt có liên quan đến CV không
        if (!isCVRelated(prompt)) {
            String rejectionMessage = getRejectionMessage();
            String htmlContent = markdownFormatterService.markdownToHtml(rejectionMessage);
            return ChatAIResponse.formatted(htmlContent, rejectionMessage, null);
        }
        
        String enhancedPrompt = """
            Bạn là trợ lý AI chuyên về CV (Sơ yếu lý lịch) và tìm việc làm. 
            Bạn CHỈ trả lời các câu hỏi liên quan đến:
            - Tạo mẫu CV cho các ngành nghề
            - Hướng dẫn viết CV
            - Tư vấn về CV và xin việc
            - Gợi ý cách trình bày CV
            
            Nếu câu hỏi không liên quan đến CV hoặc tìm việc, hãy nhẹ nhàng từ chối và hướng dẫn người dùng đặt câu hỏi về CV.
            
            Câu hỏi của người dùng: %s
            """.formatted(prompt);
        
        RobustChatService.ResponseWithModel response = robustChatService.askAIWithModel(enhancedPrompt);
        String rawContent = response.getContent();
        String model = response.getModel();
        
        // Format markdown thành HTML đẹp
        String htmlContent = markdownFormatterService.markdownToHtml(rawContent);
        
        return ChatAIResponse.formatted(htmlContent, rawContent, model);
    }
}
