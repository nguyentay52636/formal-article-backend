package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.comment.CommentRequest;
import org.example.dto.request.rating.RatingRequest;
import org.example.dto.request.template.TemplateCreateRequest;
import org.example.dto.request.template.TemplateUpdateRequest;
import org.example.dto.response.comment.CommentResponse;
import org.example.dto.response.rating.RatingResponse;
import org.example.dto.response.template.TemplateResponse;
import org.example.entity.Comment;
import org.example.entity.Rating;
import org.example.entity.Tag;
import org.example.entity.Template;
import org.example.entity.User;
import org.example.mapper.TemplateMapper;
import org.example.repository.CommentRepository;
import org.example.repository.RatingRepository;
import org.example.repository.TagRepository;
import org.example.repository.TemplateRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final TagRepository tagRepository;
    private final TemplateMapper templateMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;

    @Transactional(readOnly = true)
    public List<TemplateResponse> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(templateMapper::toTemplateResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TemplateResponse getTemplateById(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return templateMapper.toTemplateResponse(template);
    }

    @Transactional
    public TemplateResponse createTemplate(TemplateCreateRequest request) {
        if (templateRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        Template template = templateMapper.toEntity(request);
        
        if (request.getTagId() != null) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            template.setTag(tag);
        }

        template = templateRepository.save(template);
        return templateMapper.toTemplateResponse(template);
    }

    @Transactional
    public TemplateResponse updateTemplate(Long id, TemplateUpdateRequest request) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getSlug().equals(request.getSlug()) && templateRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        templateMapper.updateEntity(template, request);

        if (request.getTagId() != null) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            template.setTag(tag);
        }

        template = templateRepository.save(template);
        return templateMapper.toTemplateResponse(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new RuntimeException("Template not found");
        }
        templateRepository.deleteById(id);
    }

    @Transactional
    public void increaseViews(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setViews(template.getViews() + 1);
        templateRepository.save(template);
    }

    @Transactional(readOnly = true)
    public List<TemplateResponse> getTemplatesByTag(Long tagId) {
        return templateRepository.findByTagId(tagId).stream()
                .map(templateMapper::toTemplateResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse addComment(Long templateId, CommentRequest request) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTemplate(template);
        comment.setUser(user);

        comment = commentRepository.save(comment);
        return templateMapper.toCommentResponse(comment);
    }

    @Transactional
    public RatingResponse addRating(Long templateId, RatingRequest request) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = new Rating();
        rating.setScore(request.getScore());
        rating.setTemplate(template);
        rating.setUser(user);

        rating = ratingRepository.save(rating);
        return templateMapper.toRatingResponse(rating);
    }
}
