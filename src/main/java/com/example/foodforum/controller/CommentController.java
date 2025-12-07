package com.example.foodforum.controller;

import com.example.foodforum.entity.Comment;
import com.example.foodforum.service.CommentService;
import com.example.foodforum.dto.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 获取所有评论
     */
    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.findAll();
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
}