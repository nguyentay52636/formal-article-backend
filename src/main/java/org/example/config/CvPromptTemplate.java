package org.example.config;

/**
 * Template prompt cho AI tạo CV
 * Định nghĩa cấu trúc JSON chuẩn mà AI cần trả về
 */
public final class CvPromptTemplate {
    
    private CvPromptTemplate() {
        // Utility class
    }
    
    /**
     * Cấu trúc dataJson mẫu
     */
    public static final String DATA_JSON_STRUCTURE = """
            {
                "personalInfo": {
                    "fullName": "string",
                    "email": "string", 
                    "phone": "string",
                    "address": "string",
                    "birthday": "string (YYYY-MM-DD)"
                },
                "summary": "string (giới thiệu bản thân ngắn gọn)",
                "education": [
                    {
                        "school": "string",
                        "degree": "string",
                        "startYear": number,
                        "endYear": number
                    }
                ],
                "experience": [
                    {
                        "company": "string",
                        "position": "string",
                        "startDate": "string (YYYY-MM)",
                        "endDate": "string (YYYY-MM hoặc 'Present')",
                        "description": "string"
                    }
                ],
                "projects": [
                    {
                        "name": "string",
                        "role": "string",
                        "description": "string"
                    }
                ],
                "skills": ["string", "string", ...],
                "languages": ["string", "string", ...]
            }
            """;
    
    /**
     * Cấu trúc styleJson mẫu
     */
    public static final String STYLE_JSON_STRUCTURE = """
            {
                "font": "string (Arial, Roboto, etc.)",
                "color": "string (#hex)",
                "sectionOrder": ["personalInfo", "summary", "education", "experience", "projects", "skills", "languages"],
                "layout": "string (single-column, two-column)"
            }
            """;
    
    /**
     * Prompt chính cho AI tạo CV
     */
    public static final String CV_GENERATION_PROMPT = """
            Bạn là hệ thống tạo CV chuyên nghiệp.
            
            NHIỆM VỤ: Tạo dữ liệu CV theo đúng cấu trúc JSON bên dưới.
            
            OUTPUT FORMAT - Trả về JSON object với 4 trường:
            {
                "title": "string (tiêu đề CV)",
                "dataJson": %s,
                "styleJson": %s,
                "htmlOutput": "string (HTML hoàn chỉnh với CSS inline)"
            }
            
            YÊU CẦU:
            - dataJson: Dữ liệu CV thô, đúng cấu trúc trên
            - styleJson: Cấu hình style, đúng cấu trúc trên
            - htmlOutput: HTML responsive, đẹp, professional với CSS inline
            - title: Tiêu đề ngắn gọn cho CV
            
            ⚠ QUAN TRỌNG:
            - CHỈ trả về JSON object
            - KHÔNG có text/giải thích bên ngoài
            - KHÔNG có markdown code block
            - KHÔNG có ký tự thừa
            
            THÔNG TIN TỪ NGƯỜI DÙNG:
            %s
            """.formatted(DATA_JSON_STRUCTURE, STYLE_JSON_STRUCTURE, "%s");
    
    /**
     * Lấy prompt đã format với user input
     */
    public static String getPrompt(String userInput) {
        return CV_GENERATION_PROMPT.formatted(userInput);
    }
}
