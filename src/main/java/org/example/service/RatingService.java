package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.rating.RatingRequest;
import org.example.dto.request.rating.RatingUpdateRequest;
import org.example.dto.response.rating.RatingResponse;
import org.example.entity.Rating;
import org.example.entity.Template;
import org.example.entity.User;
import org.example.mapper.RatingMapper;
import org.example.mapper.TemplateMapper;
import org.example.repository.RatingRepository;
import org.example.repository.TemplateRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final TemplateMapper templateMapper;
    private final RatingMapper ratingMapper;

    @Transactional
    public RatingResponse createRating(RatingRequest request) {
        if (ratingRepository.existsByTemplateIdAndUserId(request.getTemplateId(), request.getUserId())) {
            throw new RuntimeException("User has already rated this template");
        }

        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = new Rating();
        rating.setScore(request.getScore());
        rating.setTemplate(template);
        rating.setUser(user);

        rating = ratingRepository.save(rating);
        return ratingMapper.toRatingResponse(rating);
    }

    @Transactional
    public RatingResponse updateRating(Long id, RatingUpdateRequest request) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        ratingMapper.updateRatingFromRequest(rating, request);

        rating = ratingRepository.save(rating);
        return ratingMapper.toRatingResponse(rating);
    }

    @Transactional
    public void deleteRating(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new RuntimeException("Rating not found");
        }
        ratingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public RatingResponse getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return ratingMapper.toRatingResponse(rating);
    }

    @Transactional(readOnly = true)
    public java.util.List<RatingResponse> getRatingsByUserId(Long userId) {
        return ratingRepository.findByUserId(userId).stream()
                .map(ratingMapper::toRatingResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional(readOnly = true)
    public java.util.List<RatingResponse> getRatingsByTemplateId(Long templateId) {
        return ratingRepository.findByTemplateId(templateId).stream()
                .map(ratingMapper::toRatingResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
