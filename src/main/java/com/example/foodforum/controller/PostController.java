package com.example.foodforum.controller;

import com.example.foodforum.entity.Post;
import com.example.foodforum.service.PostService;
import com.example.foodforum.dto.PageResult;
import com.example.foodforum.dto.PostWithUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Post> getAllPosts() {
        return postService.findAll();
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
    public List<Post> searchPosts(@RequestParam String keyword) {
        return postService.searchByKeyword(keyword);
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
    public String updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        int result = postService.update(post);
        if (result > 0) {
            return "帖子更新成功";
        } else {
            return "帖子更新失败";
        }
    }
    
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        int result = postService.deleteById(id);
        if (result > 0) {
            return "帖子删除成功";
        } else {
            return "帖子删除失败";
        }
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
}