package com.example.foodforum.service;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    public boolean addFavorite(Long userId, Long postId) {
        // 检查是否已收藏
        Favorite existing = favoriteMapper.findByUserAndPost(userId, postId);
        if (existing != null) {
            return false; // 已经收藏过了
        }
        
        Favorite favorite = new Favorite(userId, postId);
        return favoriteMapper.insert(favorite) > 0;
    }
    
    public boolean removeFavorite(Long userId, Long postId) {
        return favoriteMapper.deleteByUserAndPost(userId, postId) > 0;
    }
    
    public boolean isFavorite(Long userId, Long postId) {
        return favoriteMapper.findByUserAndPost(userId, postId) != null;
    }
    
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteMapper.findByUserId(userId);
    }
    
    public int getFavoriteCount(Long postId) {
        return favoriteMapper.countByPostId(postId);
    }
}