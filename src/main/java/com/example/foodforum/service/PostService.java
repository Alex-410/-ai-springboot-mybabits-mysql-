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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

@Service
public class PostService {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public Post findById(Long id) {
        Post post = postMapper.findById(id);
        // 将images字段的JSON字符串转换为imageUrls列表
        if (post != null && post.getImages() != null && !post.getImages().isEmpty()) {
            try {
                List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                post.setImageUrls(imageUrls);
            } catch (JsonProcessingException e) {
                // 如果转换失败，设置为空列表
                post.setImageUrls(new ArrayList<>());
            }
        }
        return post;
    }
    
    public List<Post> findAll() {
        List<Post> posts = postMapper.findAll();
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
        return posts;
    }
    
    public List<Post> findByUserId(Long userId) {
        List<Post> posts = postMapper.findByUserId(userId);
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
        return posts;
    }
    
    public List<Post> findByCategoryId(Integer categoryId) {
        List<Post> posts = postMapper.findByCategoryId(categoryId);
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
        return posts;
    }
    
    public List<Post> searchByKeyword(String keyword) {
        List<Post> posts = postMapper.searchByKeyword(keyword);
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
        return posts;
    }
    
    public int insert(Post post) {
        // 将imageUrls列表转换为JSON字符串存储到images字段
        if (post.getImageUrls() != null && !post.getImageUrls().isEmpty()) {
            try {
                String imagesJson = objectMapper.writeValueAsString(post.getImageUrls());
                post.setImages(imagesJson);
            } catch (JsonProcessingException e) {
                // 如果转换失败，设置为空字符串
                post.setImages("");
            }
        }
        return postMapper.insert(post);
    }
    
    public int update(Post post) {
        // 将imageUrls列表转换为JSON字符串存储到images字段
        if (post.getImageUrls() != null && !post.getImageUrls().isEmpty()) {
            try {
                String imagesJson = objectMapper.writeValueAsString(post.getImageUrls());
                post.setImages(imagesJson);
            } catch (JsonProcessingException e) {
                // 如果转换失败，设置为空字符串
                post.setImages("");
            }
        }
        return postMapper.update(post);
    }
    
    public int deleteById(Long id) {
        return postMapper.deleteById(id);
    }
    
    public PageResult<Post> findAllWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.findAllWithPagination(offset, size);
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
        int total = postMapper.countAll();
        return new PageResult<>(posts, total, page, size);
    }
    
    public PageResult<PostWithUserDto> findAllWithUserPagination(int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.findAllWithPagination(offset, size);
        // 将images字段的JSON字符串转换为imageUrls列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    List<String> imageUrls = objectMapper.readValue(post.getImages(), new TypeReference<List<String>>() {});
                    post.setImageUrls(imageUrls);
                } catch (JsonProcessingException e) {
                    // 如果转换失败，设置为空列表
                    post.setImageUrls(new ArrayList<>());
                }
            }
        }
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