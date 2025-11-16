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
        return response;
    }
}

