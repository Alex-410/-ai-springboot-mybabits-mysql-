package com.example.foodforum.controller;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FavoriteControllerTest {

    @Mock
 private FavoriteService favoriteService;

    @InjectMocks
 private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFavorite_Success() {
        // 准备测试数据
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 1L);
        request.put("postId", 1L);
        
        // 设置模拟行为
        when(favoriteService.addFavorite(1L, 1L)).thenReturn(1L);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.addFavorite(request);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        verify(favoriteService, times(1)).addFavorite(1L, 1L);
    }

    @Test
    void addFavorite_AlreadyExists_ReturnsBadRequest() {
        // 准备测试数据
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 1L);
        request.put("postId", 1L);
        
        // 设置模拟行为
        when(favoriteService.addFavorite(1L, 1L)).thenReturn(-1L);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.addFavorite(request);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        verify(favoriteService, times(1)).addFavorite(1L, 1L);
    }

    @Test
    void addFavorite_InvalidParameters_ReturnsBadRequest() {
        // 准备测试数据
        Map<String, Long> request = new HashMap<>(); // 空请求
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.addFavorite(request);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void removeFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteService.removeFavorite(userId, postId)).thenReturn(true);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.removeFavorite(userId, postId);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        verify(favoriteService, times(1)).removeFavorite(userId, postId);
    }

    @Test
    void removeFavorite_NotFound_ReturnsBadRequest() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteService.removeFavorite(userId, postId)).thenReturn(false);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.removeFavorite(userId, postId);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        verify(favoriteService, times(1)).removeFavorite(userId, postId);
    }

    @Test
    void removeFavorite_InvalidParameters_ReturnsBadRequest() {
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.removeFavorite(null, null);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void getFavoriteStatus_FavoriteExists_ReturnsTrue() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteService.isFavorited(userId, postId)).thenReturn(true);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.getFavoriteStatus(userId, postId);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue((Boolean) response.getBody().get("favorited"));
        verify(favoriteService, times(1)).isFavorited(userId, postId);
    }

    @Test
    void getFavoriteStatus_FavoriteNotExists_ReturnsFalse() {
        // 准备测试数据
        Long userId = 1L;
        Long postId = 1L;
        
        // 设置模拟行为
        when(favoriteService.isFavorited(userId, postId)).thenReturn(false);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.getFavoriteStatus(userId, postId);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertFalse((Boolean) response.getBody().get("favorited"));
        verify(favoriteService, times(1)).isFavorited(userId, postId);
    }

    @Test
    void getFavoriteStatus_InvalidParameters_ReturnsBadRequest() {
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.getFavoriteStatus(null, null);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void getUserFavorites_Success() {
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
        when(favoriteService.getUserFavoritesWithPagination(userId, page, size)).thenReturn(favorites);
        when(favoriteService.countUserFavorites(userId)).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.getUserFavorites(userId, page, size);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(1, ((List<?>) response.getBody().get("data")).size());
        verify(favoriteService, times(1)).getUserFavoritesWithPagination(userId, page, size);
        verify(favoriteService, times(1)).countUserFavorites(userId);
    }

    @Test
    void getUserFavorites_InvalidUserId_ReturnsBadRequest() {
        // 执行测试
        ResponseEntity<Map<String, Object>> response = favoriteController.getUserFavorites(null, 1, 10);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
    }
}