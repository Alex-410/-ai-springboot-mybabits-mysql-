package com.example.foodforum.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // 获取文件列表
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFiles() {
        List<Map<String, Object>> fileList = new ArrayList<>();
        
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        File[] files = uploadDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("name", file.getName());
                    fileInfo.put("size", file.length());
                    fileInfo.put("uploadTime", file.lastModified());
                    fileInfo.put("url", "/" + uploadDir + "/" + file.getName());
                    fileList.add(fileInfo);
                }
            }
        }
        
        return ResponseEntity.ok(fileList);
    }

    // 上传文件
    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    File dest = new File(uploadDirectory, fileName);
                    file.transferTo(dest);
                }
            }
            response.put("success", true);
            response.put("message", "文件上传成功");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 删除文件
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        
        File file = new File(uploadDir, fileName);
        if (file.exists() && file.isFile()) {
            try {
                Files.delete(file.toPath());
                response.put("success", true);
                response.put("message", "文件删除成功");
                return ResponseEntity.ok(response);
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "文件删除失败: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "文件不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}