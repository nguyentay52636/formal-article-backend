package org.example.mapper;

import org.example.dto.response.template.TemplateResponse;
import org.example.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemplateMapper {

    @Autowired
    private TagMapper tagMapper;

    public TemplateResponse toTemplateResponse(Template template) {
        if (template == null) {
            return null;
        }
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setName(template.getName());
        response.setSlug(template.getSlug());
        response.setSummary(template.getSummary());
        response.setPreviewUrl(template.getPreviewUrl());
        response.setViews(template.getViews());
        response.setDownloads(template.getDownloads());
        response.setTag(tagMapper.toTagResponse(template.getTag()));
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());
        return response;
    }
}

