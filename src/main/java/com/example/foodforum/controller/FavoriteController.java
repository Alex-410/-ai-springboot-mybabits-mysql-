package com.example.foodforum.controller;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    // 添加收藏
    @PostMapping
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody Map<String, Long> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.ok(response);
        }
        
        Long postId = payload.get("postId");
        if (postId == null) {
            response.put("success", false);
            response.put("message", "帖子ID不能为空");
            return ResponseEntity.ok(response);
        }
        
        boolean success = favoriteService.addFavorite(userId, postId);
        response.put("success", success);
        if (success) {
            response.put("message", "收藏成功");
            response.put("favoriteCount", favoriteService.getFavoriteCount(postId));
        } else {
            response.put("message", "已收藏过该帖子");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 移除收藏
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> removeFavorite(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.ok(response);
        }
        
        // 可以添加权限检查，确保用户只能删除自己的收藏
        boolean success = favoriteService.removeFavorite(userId, id);
        response.put("success", success);
        if (success) {
            response.put("message", "取消收藏成功");
        } else {
            response.put("message", "取消收藏失败");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 移除收藏（通过帖子ID）
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> removeFavoriteByPostId(@RequestBody Map<String, Long> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.ok(response);
        }
        
        Long postId = payload.get("postId");
        if (postId == null) {
            response.put("success", false);
            response.put("message", "帖子ID不能为空");
            return ResponseEntity.ok(response);
        }
        
        boolean success = favoriteService.removeFavorite(userId, postId);
        response.put("success", success);
        if (success) {
            response.put("message", "取消收藏成功");
        } else {
            response.put("message", "取消收藏失败");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 查询收藏状态
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFavorite(@RequestParam Long postId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.put("isFavorited", false);
            response.put("favoriteCount", favoriteService.getFavoriteCount(postId));
            return ResponseEntity.ok(response);
        }
        
        boolean isFavorited = favoriteService.isFavorite(userId, postId);
        response.put("isFavorited", isFavorited);
        response.put("favoriteCount", favoriteService.getFavoriteCount(postId));
        
        return ResponseEntity.ok(response);
    }
    
    // 获取用户收藏列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFavorites(@PathVariable Long userId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long currentUserId = (Long) session.getAttribute("userId");
        
        if (currentUserId == null || !currentUserId.equals(userId)) {
            response.put("success", false);
            response.put("message", "无权访问");
            return ResponseEntity.ok(response);
        }
        
        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        response.put("success", true);
        response.put("favorites", favorites);
        
        return ResponseEntity.ok(response);
    }
}