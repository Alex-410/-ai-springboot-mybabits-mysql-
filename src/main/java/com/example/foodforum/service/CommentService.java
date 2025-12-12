package com.example.foodforum.service;

import com.example.foodforum.dto.PageResult;
import com.example.foodforum.entity.Comment;
import com.example.foodforum.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    public Comment findById(Long id) {
        return commentMapper.findById(id);
    }
    
    public List<Comment> findByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }
    
    public List<Comment> findByParentId(Long parentId) {
        return commentMapper.findByParentId(parentId);
    }
    
    public PageResult<Comment> findByPostIdWithPagination(Long postId, int page, int size) {
        int offset = (page - 1) * size;
        List<Comment> comments = commentMapper.findByPostIdWithPagination(postId, offset, size);
        long total = commentMapper.countByPostId(postId);
        return new PageResult<>(comments, total, page, size);
    }
    
    public List<Comment> findByUserId(Long userId) {
        return commentMapper.findByUserId(userId);
    }
    
    public PageResult<Comment> findByUserIdWithPagination(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<Comment> comments = commentMapper.findByUserIdWithPagination(userId, offset, size);
        long total = commentMapper.countByUserId(userId);
        return new PageResult<>(comments, total, page, size);
    }
    
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }
    
    public int insert(Comment comment) {
        return commentMapper.insert(comment);
    }
    
    public int update(Comment comment) {
        return commentMapper.update(comment);
    }
    
    public int deleteById(Long id) {
        return commentMapper.deleteById(id);
    }
    
    // 更新评论状态
    public boolean updateCommentStatus(Long id, Integer status) {
        return commentMapper.updateCommentStatus(id, status) > 0;
    }
}