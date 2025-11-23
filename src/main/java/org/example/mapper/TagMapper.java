package org.example.mapper;

import org.example.dto.response.tag.TagResponse;
import org.example.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResponse toTagResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        response.setSlug(tag.getSlug());
        response.setType(tag.getType());
        return response;
    }

    public Tag toEntity(org.example.dto.request.tag.TagCreateRequest request) {
        if (request == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        tag.setType(request.getType());
        return tag;
    }

    public void updateEntity(Tag tag, org.example.dto.request.tag.TagUpdateRequest request) {
        if (tag == null || request == null) {
            return;
        }
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        tag.setType(request.getType());
    }
}

