package org.example.mapper;

import org.example.dto.request.template.TemplateCreateRequest;
import org.example.dto.request.template.TemplateUpdateRequest;
import org.example.dto.response.comment.CommentResponse;
import org.example.dto.response.rating.RatingResponse;
import org.example.dto.response.template.TemplateResponse;
import org.example.entity.Comment;
import org.example.entity.Rating;
import org.example.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemplateMapper {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    public TemplateResponse toTemplateResponse(Template template) {
        if (template == null) {
            return null;
        }
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setName(template.getName());
        response.setSlug(template.getSlug());
        response.setSummary(template.getSummary());
        response.setHtml(template.getHtml());
        response.setCss(template.getCss());
        response.setPreviewUrl(template.getPreviewUrl());
        response.setColor(template.getColor());
        response.setDescription(template.getDescription());
        response.setLanguage(template.getLanguage());
        response.setUsage(template.getUsage());
        response.setDesign(template.getDesign());
        response.setFeatures(template.getFeatures());
        response.setViews(template.getViews());
        response.setDownloads(template.getDownloads());
        response.setTag(tagMapper.toTagResponse(template.getTag()));
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());
        return response;
    }

    public Template toEntity(TemplateCreateRequest request) {
        if (request == null) {
            return null;
        }
        Template template = new Template();
        template.setName(request.getName());
        template.setSlug(request.getSlug());
        template.setSummary(request.getSummary());
        template.setHtml(request.getHtml());
        template.setCss(request.getCss());
        template.setPreviewUrl(request.getPreviewUrl());
        template.setColor(request.getColor());
        template.setDescription(request.getDescription());
        template.setLanguage(request.getLanguage());
        template.setUsage(request.getUsage());
        template.setDesign(request.getDesign());
        template.setFeatures(request.getFeatures());
        return template;
    }

    public void updateEntity(Template template, TemplateUpdateRequest request) {
        if (request == null || template == null) {
            return;
        }
        template.setName(request.getName());
        template.setSlug(request.getSlug());
        template.setSummary(request.getSummary());
        template.setHtml(request.getHtml());
        template.setCss(request.getCss());
        template.setPreviewUrl(request.getPreviewUrl());
        template.setColor(request.getColor());
        template.setDescription(request.getDescription());
        template.setLanguage(request.getLanguage());
        template.setUsage(request.getUsage());
        template.setDesign(request.getDesign());
        template.setFeatures(request.getFeatures());
    }

    public CommentResponse toCommentResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(userMapper.toUserResponse(comment.getUser()))
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .replies(comment.getReplies() != null ? 
                        comment.getReplies().stream()
                                .map(this::toCommentResponse)
                                .collect(java.util.stream.Collectors.toList()) 
                        : new java.util.ArrayList<>())
                .createdAt(comment.getCreatedAt())
                .build();
    }


}

