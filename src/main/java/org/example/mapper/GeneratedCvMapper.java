package org.example.mapper;

import org.example.dto.request.ai.GenerateCvRequest;
import org.example.dto.response.generatedCv.GeneratedCvResponse;
import org.example.entity.GeneratedCv;
import org.example.entity.Template;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa GeneratedCv entity và các DTO liên quan
 */
@Component
public class GeneratedCvMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateMapper templateMapper;

    /**
     * Chuyển đổi GeneratedCv entity sang GeneratedCvResponse
     *
     * @param entity GeneratedCv entity
     * @return GeneratedCvResponse DTO
     */
    public GeneratedCvResponse toResponse(GeneratedCv entity) {
        if (entity == null) {
            return null;
        }
        GeneratedCvResponse response = new GeneratedCvResponse();
        response.setId(entity.getId());
        response.setUser(userMapper.toUserResponse(entity.getUser()));
        response.setTemplate(templateMapper.toTemplateResponse(entity.getTemplate()));
        response.setTitle(entity.getTitle());
        response.setDataJson(entity.getDataJson());
        response.setStyleJson(entity.getStyleJson());
        response.setHtmlOutput(entity.getHtmlOutput());
        response.setPdfUrl(entity.getPdfUrl());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    /**
     * Tạo GeneratedCv entity từ dữ liệu AI response
     *
     * @param title      Tiêu đề CV
     * @param dataJson   JSON data của CV
     * @param styleJson  JSON style của CV
     * @param htmlOutput HTML output
     * @param user       User entity
     * @param template   Template entity
     * @return GeneratedCv entity
     */
    public GeneratedCv toEntity(String title, String dataJson, String styleJson, String htmlOutput, 
                                 User user, Template template) {
        GeneratedCv entity = new GeneratedCv();
        entity.setTitle(title);
        entity.setDataJson(dataJson);
        entity.setStyleJson(styleJson);
        entity.setHtmlOutput(htmlOutput);
        entity.setUser(user);
        entity.setTemplate(template);
        return entity;
    }

    /**
     * Cập nhật GeneratedCv entity từ request
     *
     * @param entity     Entity cần cập nhật
     * @param title      Tiêu đề mới (null để giữ nguyên)
     * @param dataJson   JSON data mới (null để giữ nguyên)
     * @param styleJson  JSON style mới (null để giữ nguyên)
     * @param htmlOutput HTML output mới (null để giữ nguyên)
     * @param pdfUrl     PDF URL mới (null để giữ nguyên)
     */
    public void updateEntity(GeneratedCv entity, String title, String dataJson, 
                              String styleJson, String htmlOutput, String pdfUrl) {
        if (entity == null) {
            return;
        }
        if (title != null) {
            entity.setTitle(title);
        }
        if (dataJson != null) {
            entity.setDataJson(dataJson);
        }
        if (styleJson != null) {
            entity.setStyleJson(styleJson);
        }
        if (htmlOutput != null) {
            entity.setHtmlOutput(htmlOutput);
        }
        if (pdfUrl != null) {
            entity.setPdfUrl(pdfUrl);
        }
    }
}
