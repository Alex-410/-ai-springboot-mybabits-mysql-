package com.example.foodforum.controller;

import com.example.foodforum.entity.User;
import com.example.foodforum.entity.Comment;
import com.example.foodforum.entity.Category;
import com.example.foodforum.entity.Post;
import com.example.foodforum.service.UserService;
import com.example.foodforum.service.CommentService;
import com.example.foodforum.service.CategoryService;
import com.example.foodforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin/manage")
public class AdminManagementController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private PostService postService;

    /**
     * 检查管理员权限
     */
    private boolean checkAdminPermission(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        return isAdmin != null && isAdmin;
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/users")
    public Map<String, Object> getAllUsers(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            List<User> users = userService.findAll();
            result.put("success", true);
            result.put("data", users);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/users/{userId}/status")
    public Map<String, Object> updateUserStatus(@PathVariable Long userId, 
                                               @RequestBody Map<String, Integer> statusRequest,
                                               HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            Integer status = statusRequest.get("status");
            userService.updateUserStatus(userId, status);
            result.put("success", true);
            result.put("message", "用户状态更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新用户状态失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            userService.deleteById(userId);
            result.put("success", true);
            result.put("message", "用户删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有评论列表
     */
    @GetMapping("/comments")
    public Map<String, Object> getAllComments(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            List<Comment> comments = commentService.findAll();
            result.put("success", true);
            result.put("data", comments);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取评论列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新评论状态
     */
    @PutMapping("/comments/{commentId}/status")
    public Map<String, Object> updateCommentStatus(@PathVariable Long commentId, 
                                                  @RequestBody Map<String, Integer> statusRequest,
                                                  HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            Integer status = statusRequest.get("status");
            commentService.updateCommentStatus(commentId, status);
            result.put("success", true);
            result.put("message", "评论状态更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新评论状态失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{commentId}")
    public Map<String, Object> deleteComment(@PathVariable Long commentId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            commentService.deleteById(commentId);
            result.put("success", true);
            result.put("message", "评论删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除评论失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有分类列表
     */
    @GetMapping("/categories")
    public Map<String, Object> getAllCategories(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            List<Category> categories = categoryService.findAll();
            result.put("success", true);
            result.put("data", categories);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取分类列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建新分类
     */
    @PostMapping("/categories")
    public Map<String, Object> createCategory(@RequestBody Category category, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            categoryService.insert(category);
            result.put("success", true);
            result.put("message", "分类创建成功");
            result.put("data", category);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建分类失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新分类
     */
    @PutMapping("/categories/{categoryId}")
    public Map<String, Object> updateCategory(@PathVariable Integer categoryId, 
                                             @RequestBody Category category,
                                             HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            category.setId(categoryId);
            categoryService.update(category);
            result.put("success", true);
            result.put("message", "分类更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新分类失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/categories/{categoryId}")
    public Map<String, Object> deleteCategory(@PathVariable Integer categoryId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            categoryService.deleteById(categoryId);
            result.put("success", true);
            result.put("message", "分类删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除分类失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有帖子列表
     */
    @GetMapping("/posts")
    public Map<String, Object> getAllPosts(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            List<Post> posts = postService.findAll();
            result.put("success", true);
            result.put("data", posts);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取帖子列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新帖子状态
     */
    @PutMapping("/posts/{postId}/status")
    public Map<String, Object> updatePostStatus(@PathVariable Long postId, 
                                               @RequestBody Map<String, Integer> statusRequest,
                                               HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            Integer status = statusRequest.get("status");
            postService.updatePostStatus(postId, status);
            result.put("success", true);
            result.put("message", "帖子状态更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新帖子状态失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/posts/{postId}")
    public Map<String, Object> deletePost(@PathVariable Long postId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            postService.deleteById(postId);
            result.put("success", true);
            result.put("message", "帖子删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除帖子失败: " + e.getMessage());
        }
        
        return result;
    }
}