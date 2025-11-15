package org.example.mapper;

import org.example.dto.request.favouriteCv.FavouriteCvCreateRequest;
import org.example.dto.request.favouriteCv.FavouriteCvUpdateRequest;
import org.example.dto.response.favouriteCv.FavouriteCvResponse;
import org.example.entity.FavouriteCv;
import org.example.entity.Template;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavouriteCvMapper {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TemplateMapper templateMapper;

    /**
     * Chuyển đổi FavouriteCv entity sang FavouriteCvResponse
     */
    public FavouriteCvResponse toFavouriteCvResponse(FavouriteCv favouriteCv) {
        if (favouriteCv == null) {
            return null;
        }
        
        FavouriteCvResponse response = new FavouriteCvResponse();
        response.setId(favouriteCv.getId());
        response.setCreatedAt(favouriteCv.getCreatedAt());
        
        // Lấy thông tin user
        if (favouriteCv.getUser() != null) {
            response.setUserId(favouriteCv.getUser().getId());
            response.setUser(userMapper.toUserResponse(favouriteCv.getUser()));
        }
        
        // Lấy thông tin template
        if (favouriteCv.getTemplate() != null) {
            response.setTemplateId(favouriteCv.getTemplate().getId());
            response.setTemplate(templateMapper.toTemplateResponse(favouriteCv.getTemplate()));
        }
        
        return response;
    }

    /**
     * Tạo FavouriteCv entity từ FavouriteCvCreateRequest
     */
    public FavouriteCv toFavouriteCv(FavouriteCvCreateRequest request, User user, Template template) {
        if (request == null) {
            return null;
        }
        
        FavouriteCv favouriteCv = new FavouriteCv();
        favouriteCv.setUser(user);
        favouriteCv.setTemplate(template);
        
        return favouriteCv;
    }

    /**
     * Cập nhật FavouriteCv entity từ FavouriteCvUpdateRequest
     */
    public void updateFavouriteCv(FavouriteCvUpdateRequest request, FavouriteCv favouriteCv, User user, Template template) {
        if (request == null || favouriteCv == null) {
            return;
        }
        
        favouriteCv.setUser(user);
        favouriteCv.setTemplate(template);
    }
}

