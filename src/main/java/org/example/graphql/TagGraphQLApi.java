package org.example.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.example.dto.response.tag.TagResponse;
import org.example.entity.Tag;
import org.example.mapper.TagMapper;
import org.example.repository.TagRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagGraphQLApi {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagGraphQLApi(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @GraphQLQuery(name = "tags", description = "Danh sách tất cả tag")
    public List<TagResponse> tags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toTagResponse)
                .toList();
    }

    @GraphQLQuery(name = "tag", description = "Chi tiết tag theo ID")
    public TagResponse tag(@GraphQLArgument(name = "id") Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toTagResponse)
                .orElse(null);
    }

    @GraphQLQuery(name = "tagBySlug", description = "Chi tiết tag theo slug")
    public TagResponse tagBySlug(@GraphQLArgument(name = "slug") String slug) {
        return tagRepository.findBySlug(slug)
                .map(tagMapper::toTagResponse)
                .orElse(null);
    }

    @GraphQLMutation(name = "createTag", description = "Tạo tag mới")
    public TagResponse createTag(@GraphQLArgument(name = "slug") String slug,
                                 @GraphQLArgument(name = "name") String name) {
        if (tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug đã tồn tại: " + slug);
        }
        Tag tag = new Tag();
        tag.setSlug(slug);
        tag.setName(name);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toTagResponse(saved);
    }

    @GraphQLMutation(name = "updateTag", description = "Cập nhật tag")
    public TagResponse updateTag(@GraphQLArgument(name = "id") Long id,
                                 @GraphQLArgument(name = "slug") String slug,
                                 @GraphQLArgument(name = "name") String name) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag không tồn tại: " + id));

        if (slug != null && !slug.equals(tag.getSlug()) && tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug đã tồn tại: " + slug);
        }

        if (slug != null && !slug.isBlank()) {
            tag.setSlug(slug);
        }
        if (name != null && !name.isBlank()) {
            tag.setName(name);
        }

        Tag updated = tagRepository.save(tag);
        return tagMapper.toTagResponse(updated);
    }

    @GraphQLMutation(name = "deleteTag", description = "Xóa tag theo ID")
    public Boolean deleteTag(@GraphQLArgument(name = "id") Long id) {
        if (!tagRepository.existsById(id)) {
            return false;
        }
        tagRepository.deleteById(id);
        return true;
    }
}

