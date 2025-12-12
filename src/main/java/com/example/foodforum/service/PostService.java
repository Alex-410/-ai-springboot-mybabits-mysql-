package com.example.foodforum.service;

import com.example.foodforum.entity.Post;
import com.example.foodforum.entity.User;
import com.example.foodforum.mapper.PostMapper;
import com.example.foodforum.mapper.UserMapper;
import com.example.foodforum.dto.PageResult;
import com.example.foodforum.dto.PostWithUserDto;
import com.example.foodforum.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
public class PostService {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CategoryService categoryService;
    
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
    
    public List<Post> searchByKeywordAndCategoryId(String keyword, Integer categoryId) {
        List<Post> posts = postMapper.searchByKeywordAndCategoryId(keyword, categoryId);
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
    
    // 更新帖子状态
    public int updatePostStatus(Long id, Integer status) {
        return postMapper.updatePostStatus(id, status);
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
        
        // 获取所有分类信息并建立ID到名称的映射
        List<Category> categories = categoryService.findAll();
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        
        // 为每个帖子获取用户信息和分类名称
        List<PostWithUserDto> postWithUserDtos = posts.stream().map(post -> {
            User user = userMapper.findById(post.getUserId());
            String categoryName = categoryMap.getOrDefault(post.getCategoryId(), "未知分类");
            return new PostWithUserDto(post, user, categoryName);
        }).collect(Collectors.toList());
        
        return new PageResult<>(postWithUserDtos, total, page, size);
    }
    
    public int incrementLikeCount(Long id) {
        return postMapper.incrementLikeCount(id);
    }
    
    public int incrementFavoriteCount(Long id) {
        return postMapper.incrementFavoriteCount(id);
    }
    
    // 新增支持分页的搜索方法
    public PageResult<PostWithUserDto> searchWithPagination(String keyword, Integer categoryId, int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts;
        int total;
        
        if (keyword != null && !keyword.isEmpty() && categoryId != null) {
            posts = postMapper.searchByKeywordAndCategoryIdWithPagination(keyword, categoryId, offset, size);
            total = postMapper.countByKeywordAndCategoryId(keyword, categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            posts = postMapper.searchByKeywordWithPagination(keyword, offset, size);
            total = postMapper.countByKeyword(keyword);
        } else if (categoryId != null) {
            posts = postMapper.findByCategoryIdWithPagination(categoryId, offset, size);
            total = postMapper.countByCategoryId(categoryId);
        } else {
            return findAllWithUserPagination(page, size); // 如果没有搜索条件，则返回所有帖子
        }
        
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
        
        // 获取所有分类信息并建立ID到名称的映射
        List<Category> categories = categoryService.findAll();
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        
        // 为每个帖子获取用户信息和分类名称
        List<PostWithUserDto> postWithUserDtos = posts.stream().map(post -> {
            User user = userMapper.findById(post.getUserId());
            String categoryName = categoryMap.getOrDefault(post.getCategoryId(), "未知分类");
            return new PostWithUserDto(post, user, categoryName);
        }).collect(Collectors.toList());
        
        return new PageResult<>(postWithUserDtos, total, page, size);
    }
}