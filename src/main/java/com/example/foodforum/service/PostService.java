package com.example.foodforum.service;

import com.example.foodforum.entity.Post;
import com.example.foodforum.entity.User;
import com.example.foodforum.mapper.PostMapper;
import com.example.foodforum.mapper.UserMapper;
import com.example.foodforum.dto.PageResult;
import com.example.foodforum.dto.PostWithUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public Post findById(Long id) {
        return postMapper.findById(id);
    }
    
    public List<Post> findAll() {
        return postMapper.findAll();
    }
    
    public List<Post> findByUserId(Long userId) {
        return postMapper.findByUserId(userId);
    }
    
    public List<Post> findByCategoryId(Integer categoryId) {
        return postMapper.findByCategoryId(categoryId);
    }
    
    public List<Post> searchByKeyword(String keyword) {
        return postMapper.searchByKeyword(keyword);
    }
    
    public int insert(Post post) {
        return postMapper.insert(post);
    }
    
    public int update(Post post) {
        return postMapper.update(post);
    }
    
    public int deleteById(Long id) {
        return postMapper.deleteById(id);
    }
    
    public PageResult<Post> findAllWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.findAllWithPagination(offset, size);
        int total = postMapper.countAll();
        return new PageResult<>(posts, total, page, size);
    }
    
    public PageResult<PostWithUserDto> findAllWithUserPagination(int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.findAllWithPagination(offset, size);
        int total = postMapper.countAll();
        
        // 为每个帖子获取用户信息
        List<PostWithUserDto> postWithUserDtos = posts.stream().map(post -> {
            User user = userMapper.findById(post.getUserId());
            return new PostWithUserDto(post, user);
        }).collect(Collectors.toList());
        
        return new PageResult<>(postWithUserDtos, total, page, size);
    }
    
    public int incrementLikeCount(Long id) {
        return postMapper.incrementLikeCount(id);
    }
    
    public int incrementFavoriteCount(Long id) {
        return postMapper.incrementFavoriteCount(id);
    }
}