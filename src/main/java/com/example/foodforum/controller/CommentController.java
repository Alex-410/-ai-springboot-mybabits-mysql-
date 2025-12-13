package com.example.foodforum.controller;

import com.example.foodforum.entity.Comment;
import com.example.foodforum.service.CommentService;
import com.example.foodforum.dto.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 根据ID获取评论
     */
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.findById(id);
    }
    
    /**
     * 根据帖子ID获取所有评论
     */
    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.findByPostId(postId);
    }
    
    /**
     * 根据帖子ID获取评论（分页）
     */
    @GetMapping("/post/{postId}/page")
    public PageResult<Comment> getCommentsByPostIdWithPagination(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.findByPostIdWithPagination(postId, page, size);
    }
    
    /**
     * 根据用户ID获取所有评论
     */
    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.findByUserId(userId);
    }
    
    /**
     * 根据用户ID获取评论（分页）
     */
    @GetMapping("/user/{userId}/page")
    public PageResult<Comment> getCommentsByUserIdWithPagination(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.findByUserIdWithPagination(userId, page, size);
    }
    
    /**
     * 根据父评论ID获取所有回复评论
     */
    @GetMapping("/parent/{parentId}")
    public List<Comment> getCommentsByParentId(@PathVariable Long parentId) {
        return commentService.findByParentId(parentId);
    }
    
    /**
     * 获取所有评论
     */
    @GetMapping
    public List<Comment> getAllComments(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return commentService.searchByKeyword(keyword);
        } else {
            return commentService.findAll();
        }
    }
    
    /**
     * 创建新评论
     */
    @PostMapping
    public String createComment(@RequestBody Comment comment) {
        comment.setStatus(1); // 默认状态为正常
        int result = commentService.insert(comment);
        if (result > 0) {
            return "评论创建成功";
        } else {
            return "评论创建失败";
        }
    }
    
    /**
     * 更新评论
     */
    @PutMapping("/{id}")
    public String updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        int result = commentService.update(comment);
        if (result > 0) {
            return "评论更新成功";
        } else {
            return "评论更新失败";
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id) {
        int result = commentService.deleteById(id);
        if (result > 0) {
            return "评论删除成功";
        } else {
            return "评论删除失败";
        }
    }
    
    // 更新评论状态
    @PutMapping("/{id}/status")
    public Map<String, Object> updateCommentStatus(@PathVariable Long id, @RequestBody Map<String, Integer> requestBody) {
        Map<String, Object> result = new HashMap<>();
        Integer status = requestBody.get("status");
        
        if (status == null) {
            result.put("success", false);
            result.put("message", "状态值不能为空");
            return result;
        }
        
        boolean success = commentService.updateCommentStatus(id, status);
        if (success) {
            result.put("success", true);
            result.put("message", "评论状态更新成功");
        } else {
            result.put("success", false);
            result.put("message", "评论状态更新失败");
        }
        
        return result;
    }
}