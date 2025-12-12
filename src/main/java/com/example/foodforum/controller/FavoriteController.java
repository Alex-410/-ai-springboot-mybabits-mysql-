package com.example.foodforum.controller;

import com.example.foodforum.entity.Favorite;
import com.example.foodforum.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 添加收藏接口
     * @param request 包含userId和postId的请求体
     * @return 响应结果
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody Map<String, Long> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Long userId = request.get("userId");
            Long postId = request.get("postId");
            
            // 参数校验
            if (userId == null || postId == null) {
                result.put("success", false);
                result.put("message", "用户ID和帖子ID不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            long favoriteId = favoriteService.addFavorite(userId, postId);
            
            if (favoriteId > 0) {
                result.put("success", true);
                result.put("message", "收藏成功");
                result.put("favoriteId", favoriteId);
                return ResponseEntity.ok(result);
            } else if (favoriteId == -1) {
                result.put("success", false);
                result.put("message", "已收藏该帖子");
                return ResponseEntity.badRequest().body(result);
            } else {
                result.put("success", false);
                result.put("message", "收藏失败");
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
    
    /**
     * 取消收藏接口
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 响应结果
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> removeFavorite(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (userId == null || postId == null) {
                result.put("success", false);
                result.put("message", "用户ID和帖子ID不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            boolean success = favoriteService.removeFavorite(userId, postId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "取消收藏成功");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "取消收藏失败，可能未收藏该帖子");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
    
    /**
     * 查询单个内容收藏状态接口
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 响应结果
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getFavoriteStatus(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (userId == null || postId == null) {
                result.put("success", false);
                result.put("message", "用户ID和帖子ID不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            boolean isFavorited = favoriteService.isFavorited(userId, postId);
            
            result.put("success", true);
            result.put("favorited", isFavorited);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
    
    /**
     * 获取用户收藏列表接口（支持分页）
     * @param userId 用户ID
     * @param page 页码（从1开始，默认为1）
     * @param size 每页大小（默认为10）
     * @return 响应结果
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (userId == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 限制分页参数
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            if (size > 100) size = 100; // 限制最大每页数量
            
            List<Favorite> favorites = favoriteService.getUserFavoritesWithPagination(userId, page, size);
            int total = favoriteService.countUserFavorites(userId);
            
            result.put("success", true);
            result.put("data", favorites);
            result.put("page", page);
            result.put("size", size);
            result.put("total", total);
            result.put("totalPages", (int) Math.ceil((double) total / size));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}