package com.example.foodforum.controller;

import com.example.foodforum.entity.User;
import com.example.foodforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    // 头像上传目录
    private static final String AVATAR_UPLOAD_DIR = "uploads/avatars/";

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        
        // 检查用户名是否已存在
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        // 检查两次输入的密码是否一致
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            result.put("success", false);
            result.put("message", "两次输入的密码不一致");
            return result;
        }
        
        // 设置用户状态为正常
        user.setStatus(1);
        
        // 插入用户到数据库
        int insertResult = userService.insert(user);
        if (insertResult > 0) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }
        
        return result;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginUser, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // 根据用户名查找用户
        User user = userService.findByUsername(loginUser.getUsername());
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        
        // 验证密码（使用明文比较）
        if (!user.getPassword().equals(loginUser.getPassword())) {
            result.put("success", false);
            result.put("message", "密码错误");
            return result;
        }
        
        // 登录成功，将用户信息保存到session中
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("user", user);
        
        return result;
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // 清除session
        session.invalidate();
        
        result.put("success", true);
        result.put("message", "登出成功");
        
        return result;
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/status")
    public Map<String, Object> status(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user != null) {
            result.put("loggedIn", true);
            result.put("user", user);
        } else {
            result.put("loggedIn", false);
        }
        
        return result;
    }
    
    /**
     * 上传用户头像
     */
    @PostMapping("/upload-avatar")
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要上传的文件");
                return result;
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                result.put("success", false);
                result.put("message", "只能上传图片文件");
                return result;
            }
            
            // 获取当前登录用户
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                result.put("success", false);
                result.put("message", "请先登录");
                return result;
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(AVATAR_UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.write(filePath, file.getBytes());
            
            // 构建访问URL
            String avatarUrl = "/" + AVATAR_UPLOAD_DIR + uniqueFilename;
            
            // 更新用户头像信息
            currentUser.setAvatar(avatarUrl);
            userService.update(currentUser);
            
            // 更新session中的用户信息
            session.setAttribute("user", currentUser);
            
            result.put("success", true);
            result.put("message", "头像上传成功");
            result.put("avatarUrl", avatarUrl);
            
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "头像上传失败: " + e.getMessage());
        }
        
        return result;
    }
}