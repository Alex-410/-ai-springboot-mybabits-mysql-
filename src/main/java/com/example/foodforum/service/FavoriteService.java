package com.example.foodforum.service;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    /**
     * 添加收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏ID，如果已收藏则返回-1
     */
    public long addFavorite(Long userId, Long postId) {
        // 检查是否已收藏
        Favorite existing = favoriteMapper.findByUserAndPost(userId, postId);
        if (existing != null) {
            // 已收藏，返回-1表示已存在
            return -1;
        }
        
        // 创建新的收藏记录
        Favorite favorite = new Favorite(userId, postId);
        // 注意：Favorite构造函数已经设置了createdAt，不需要再次设置
        int result = favoriteMapper.insert(favorite);
        // 插入成功后直接返回1作为成功标识，因为我们不依赖于具体的ID值
        return result > 0 ? 1 : 0;
    }
    
    /**
     * 取消收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功取消
     */
    public boolean removeFavorite(Long userId, Long postId) {
        int result = favoriteMapper.deleteByUserAndPost(userId, postId);
        return result > 0;
    }
    
    /**
     * 查询单个内容收藏状态
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    public boolean isFavorited(Long userId, Long postId) {
        Favorite favorite = favoriteMapper.findByUserAndPost(userId, postId);
        return favorite != null;
    }
    
    /**
     * 获取用户收藏列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteMapper.findByUserId(userId);
    }
    
    /**
     * 分页获取用户收藏列表
     * @param userId 用户ID
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 收藏列表
     */
    public List<Favorite> getUserFavoritesWithPagination(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return favoriteMapper.findByUserIdWithPagination(userId, offset, size);
    }
    
    /**
     * 统计用户的收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    public int countUserFavorites(Long userId) {
        return favoriteMapper.countByUserId(userId);
    }
}