package org.example.mapper;

import org.example.dto.request.rating.RatingUpdateRequest;
import org.example.dto.response.rating.RatingResponse;
import org.example.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    @Autowired
    private UserMapper userMapper;

    public RatingResponse toRatingResponse(Rating rating) {
        if (rating == null) {
            return null;
        }
        return RatingResponse.builder()
                .id(rating.getId())
                .score(rating.getScore())
                .user(userMapper.toUserResponse(rating.getUser()))
                .templateId(rating.getTemplate().getId())
                .templateName(rating.getTemplate().getName())
                .createdAt(rating.getCreatedAt())
                .build();
    }

    public void updateRatingFromRequest(Rating rating, RatingUpdateRequest request) {
        if (request == null || rating == null) {
            return;
        }
        if (request.getScore() != null) {
            rating.setScore(request.getScore());
        }
    }
}