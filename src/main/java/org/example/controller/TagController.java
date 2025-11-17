package org.example.controller;

import org.example.entity.Tag;
import org.example.repository.TagRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @QueryMapping
    public List<Tag> tags() {
        return tagRepository.findAll();
    }

    @QueryMapping
    public Tag tag(@Argument Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Tag tagBySlug(@Argument String slug) {
        return tagRepository.findBySlug(slug).orElse(null);
    }

    @MutationMapping
    public Tag createTag(@Argument TagInput input) {
        if (tagRepository.existsBySlug(input.slug())) {
            throw new IllegalArgumentException("Slug đã tồn tại: " + input.slug());
        }
        Tag tag = new Tag();
        tag.setSlug(input.slug());
        tag.setName(input.name());
        return tagRepository.save(tag);
    }

    @MutationMapping
    public Tag updateTag(@Argument Long id, @Argument TagInput input) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tag với id " + id));

        if (!tag.getSlug().equals(input.slug()) && tagRepository.existsBySlug(input.slug())) {
            throw new IllegalArgumentException("Slug đã tồn tại: " + input.slug());
        }

        tag.setSlug(input.slug());
        tag.setName(input.name());
        return tagRepository.save(tag);
    }

    @MutationMapping
    public Boolean deleteTag(@Argument Long id) {
        if (!tagRepository.existsById(id)) {
            return false;
        }
        tagRepository.deleteById(id);
        return true;
    }

    public record TagInput(String slug, String name) { }
}
