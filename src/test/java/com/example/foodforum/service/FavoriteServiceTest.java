package com.example.foodforum.service;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.mapper.FavoriteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @Mock
    private FavoriteMapper favoriteMapper;

    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteMapper.findByUserAndPost(userId, postId)).thenReturn(null);
        when(favoriteMapper.insert(any(Favorite.class))).thenReturn(1);
        
        // 执行测试
        long result = favoriteService.addFavorite(userId, postId);
        
        // 验证结果
        assertTrue(result > 0);
        verify(favoriteMapper, times(1)).findByUserAndPost(userId, postId);
        verify(favoriteMapper, times(1)).insert(any(Favorite.class));
    }

    @Test
    void addFavorite_AlreadyExists_ReturnsNegative() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        Favorite existingFavorite = new Favorite();
        existingFavorite.setId(1L);
        existingFavorite.setUserId(userId);
        existingFavorite.setPostId(postId);
        existingFavorite.setCreatedAt(LocalDateTime.now());
        
        // 设置模拟行为
        when(favoriteMapper.findByUserAndPost(userId, postId)).thenReturn(existingFavorite);
        
        // 执行测试
        long result = favoriteService.addFavorite(userId, postId);
        
        // 验证结果
        assertEquals(-1, result);
        verify(favoriteMapper, times(1)).findByUserAndPost(userId, postId);
        verify(favoriteMapper, never()).insert(any(Favorite.class));
    }

    @Test
    void removeFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteMapper.deleteByUserAndPost(userId, postId)).thenReturn(1);
        
        // 执行测试
        boolean result = favoriteService.removeFavorite(userId, postId);
        
        // 验证结果
        assertTrue(result);
        verify(favoriteMapper, times(1)).deleteByUserAndPost(userId, postId);
    }

    @Test
    void removeFavorite_NotFound_ReturnsFalse() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteMapper.deleteByUserAndPost(userId, postId)).thenReturn(0);
        
        // 执行测试
        boolean result = favoriteService.removeFavorite(userId, postId);
        
        // 验证结果
        assertFalse(result);
        verify(favoriteMapper, times(1)).deleteByUserAndPost(userId, postId);
    }

    @Test
    void isFavorited_FavoriteExists_ReturnsTrue() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        Favorite existingFavorite = new Favorite();
        existingFavorite.setId(1L);
        existingFavorite.setUserId(userId);
        existingFavorite.setPostId(postId);
        existingFavorite.setCreatedAt(LocalDateTime.now());
        
        // 设置模拟行为
        when(favoriteMapper.findByUserAndPost(userId, postId)).thenReturn(existingFavorite);
        
        // 执行测试
        boolean result = favoriteService.isFavorited(userId, postId);
        
        // 验证结果
        assertTrue(result);
        verify(favoriteMapper, times(1)).findByUserAndPost(userId, postId);
    }

    @Test
    void isFavorited_FavoriteNotExists_ReturnsFalse() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteMapper.findByUserAndPost(userId, postId)).thenReturn(null);
        
        // 执行测试
        boolean result = favoriteService.isFavorited(userId, postId);
        
        // 验证结果
        assertFalse(result);
        verify(favoriteMapper, times(1)).findByUserAndPost(userId, postId);
    }

    @Test
    void getUserFavorites_ReturnsFavoritesList() {
        // 准备测试数据
        Long userId = 1L;
        List<Favorite> favorites = new ArrayList<>();
        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUserId(userId);
        favorite.setPostId(1L);
        favorite.setCreatedAt(LocalDateTime.now());
        favorites.add(favorite);
        
        // 设置模拟行为
        when(favoriteMapper.findByUserId(userId)).thenReturn(favorites);
        
        // 执行测试
        List<Favorite> result = favoriteService.getUserFavorites(userId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(favorite.getId(), result.get(0).getId());
        verify(favoriteMapper, times(1)).findByUserId(userId);
    }

    @Test
    void getUserFavoritesWithPagination_ReturnsFavoritesList() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 10;
        List<Favorite> favorites = new ArrayList<>();
        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUserId(userId);
        favorite.setPostId(1L);
        favorite.setCreatedAt(LocalDateTime.now());
        favorites.add(favorite);
        
        // 设置模拟行为
        when(favoriteMapper.findByUserIdWithPagination(userId, (page - 1) * size, size)).thenReturn(favorites);
        
        // 执行测试
        List<Favorite> result = favoriteService.getUserFavoritesWithPagination(userId, page, size);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(favorite.getId(), result.get(0).getId());
        verify(favoriteMapper, times(1)).findByUserIdWithPagination(userId, (page - 1) * size, size);
    }

    @Test
    void countUserFavorites_ReturnsCorrectCount() {
        // 准备测试数据
        Long userId = 1L;
        int expectedCount = 5;
        
        // 设置模拟行为
        when(favoriteMapper.countByUserId(userId)).thenReturn(expectedCount);
        
        // 执行测试
        int result = favoriteService.countUserFavorites(userId);
        
        // 验证结果
        assertEquals(expectedCount, result);
        verify(favoriteMapper, times(1)).countByUserId(userId);
    }
}