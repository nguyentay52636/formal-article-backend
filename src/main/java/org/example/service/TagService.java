package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.tag.TagCreateRequest;
import org.example.dto.request.tag.TagUpdateRequest;
import org.example.dto.response.tag.TagResponse;
import org.example.entity.Tag;
import org.example.mapper.TagMapper;
import org.example.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toTagResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTagsByType(String type) {
        return tagRepository.findByType(type).stream()
                .map(tagMapper::toTagResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return tagMapper.toTagResponse(tag);
    }

    @Transactional
    public TagResponse createTag(TagCreateRequest request) {
        if (tagRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }
        Tag tag = tagMapper.toEntity(request);
        tag = tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    @Transactional
    public TagResponse updateTag(Long id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        if (!tag.getSlug().equals(request.getSlug()) && tagRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        tagMapper.updateEntity(tag, request);
        tag = tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }
}
