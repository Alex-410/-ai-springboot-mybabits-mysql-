package com.example.foodforum.controller;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/admin/manage/files")
public class FileManagementController {

    private static final String UPLOAD_DIR = "uploads";
    
    /**
     * 检查管理员权限
     */
    private boolean checkAdminPermission(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        return isAdmin != null && isAdmin;
    }

    /**
     * 获取所有上传文件列表
     */
    @GetMapping("/")
    public Map<String, Object> getAllFiles(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File[] files = uploadDir.listFiles();
            result.put("success", true);
            result.put("data", Arrays.asList(files != null ? files : new File[0]));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取文件列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除指定文件
     */
    @DeleteMapping("/{filename}")
    public Map<String, Object> deleteFile(@PathVariable String filename, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (!checkAdminPermission(session)) {
            result.put("success", false);
            result.put("message", "未授权访问");
            return result;
        }
        
        try {
            // 防止路径遍历攻击
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                result.put("success", false);
                result.put("message", "无效的文件名");
                return result;
            }
            
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                result.put("success", true);
                result.put("message", "文件删除成功");
            } else {
                result.put("success", false);
                result.put("message", "文件不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除文件失败: " + e.getMessage());
        }
        
        return result;
    }
}