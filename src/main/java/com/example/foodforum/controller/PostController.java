package com.example.foodforum.controller;

import com.example.foodforum.entity.Post;
import com.example.foodforum.service.PostService;
import com.example.foodforum.dto.PageResult;
import com.example.foodforum.dto.PostWithUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.findById(id);
    }
    
    @GetMapping
    public List<Post> getAllPosts(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return postService.searchByKeyword(keyword);
        } else {
            return postService.findAll();
        }
    }
    
    @GetMapping("/page")
    public PageResult<Post> getAllPostsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size) {  // 默认每页2条记录
        return postService.findAllWithPagination(page, size);
    }
    
    @GetMapping("/page-with-user")
    public PageResult<PostWithUserDto> getAllPostsWithUserPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size) {  // 默认每页2条记录
        return postService.findAllWithUserPagination(page, size);
    }
    
    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam(required = false) String keyword, 
                                 @RequestParam(required = false) Integer categoryId) {
        if (keyword != null && !keyword.isEmpty() && categoryId != null) {
            return postService.searchByKeywordAndCategoryId(keyword, categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            return postService.searchByKeyword(keyword);
        } else if (categoryId != null) {
            return postService.findByCategoryId(categoryId);
        } else {
            return postService.findAll();
        }
    }
    
    @GetMapping("/search/page")
    public PageResult<PostWithUserDto> searchPostsWithPagination(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size) {
        return postService.searchWithPagination(keyword, categoryId, page, size);
    }
    
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {
        return postService.findByUserId(userId);
    }
    
    @GetMapping("/category/{categoryId}")
    public List<Post> getPostsByCategoryId(@PathVariable Integer categoryId) {
        return postService.findByCategoryId(categoryId);
    }
    
    @PostMapping
    public String createPost(@RequestBody Post post) {
        // 设置默认状态为已发布
        if (post.getStatus() == null) {
            post.setStatus(1);
        }
        int result = postService.insert(post);
        if (result > 0) {
            return "帖子创建成功";
        } else {
            return "帖子创建失败";
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Map<String, Object> result = new HashMap<>();
        post.setId(id);
        int updateResult = postService.update(post);
        if (updateResult > 0) {
            result.put("success", true);
            result.put("message", "帖子更新成功");
        } else {
            result.put("success", false);
            result.put("message", "帖子更新失败");
        }
        return result;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> deletePost(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        int deleteResult = postService.deleteById(id);
        if (deleteResult > 0) {
            result.put("success", true);
            result.put("message", "帖子删除成功");
        } else {
            result.put("success", false);
            result.put("message", "帖子删除失败");
        }
        return result;
    }
    
    @PostMapping("/{id}/like")
    public String incrementLikeCount(@PathVariable Long id) {
        int result = postService.incrementLikeCount(id);
        if (result > 0) {
            return "点赞成功";
        } else {
            return "点赞失败";
        }
    }
    
    @PostMapping("/{id}/favorite")
    public String incrementFavoriteCount(@PathVariable Long id) {
        int result = postService.incrementFavoriteCount(id);
        if (result > 0) {
            return "收藏成功";
        } else {
            return "收藏失败";
        }
    }
    
    @GetMapping("/batch")
    public List<PostWithUserDto> getPostsByIds(@RequestParam String ids) {
        // Convert the comma-separated string to a list of Long IDs
        List<Long> postIds = java.util.Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(java.util.stream.Collectors.toList());
        return postService.findByIdsWithDetails(postIds);
    }
}