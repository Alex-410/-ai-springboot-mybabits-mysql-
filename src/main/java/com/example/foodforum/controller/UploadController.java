package com.example.foodforum.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    // 定义上传文件的存储路径
    private static final String UPLOAD_DIR = "uploads/";
    
    // 图片压缩参数
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    private static final float QUALITY = 0.8f;
    
    @PostMapping("/avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 检查文件是否为空
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "上传文件不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // 使用项目根目录下的uploads文件夹
            String rootPath = System.getProperty("user.dir");
            String uploadPath = Paths.get(rootPath, UPLOAD_DIR).toString();
            
            System.out.println("Root path: " + rootPath);
            System.out.println("Upload path: " + uploadPath);
            
            // 创建上传目录（如果不存在）
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                System.out.println("Upload directory does not exist, creating...");
                boolean created = uploadDir.mkdirs();
                System.out.println("Directory creation result: " + created);
            } else {
                System.out.println("Upload directory already exists");
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            System.out.println("Saving file as: " + uniqueFilename);
            
            // 保存文件
            File destFile = new File(uploadPath, uniqueFilename);
            System.out.println("Destination file path: " + destFile.getAbsolutePath());
            file.transferTo(destFile);
            
            // 检查文件是否真的保存成功
            boolean fileExists = destFile.exists();
            System.out.println("File saved successfully: " + fileExists);
            System.out.println("File size: " + (fileExists ? destFile.length() : "N/A"));
            
            // 构建可访问的URL（相对于应用根路径）
            String fileUrl = "/uploads/" + uniqueFilename;
            
            response.put("success", true);
            response.put("message", "头像上传成功");
            response.put("url", fileUrl);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace(); // 打印详细错误信息
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/post-images")
    public ResponseEntity<Map<String, Object>> uploadPostImages(
            @RequestParam("files") MultipartFile[] files) {
        
        Map<String, Object> response = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();
        
        // 检查文件数量
        if (files.length == 0) {
            response.put("success", false);
            response.put("message", "请选择至少一张图片");
            return ResponseEntity.badRequest().body(response);
        }
        
        // 限制最多上传9张图片
        if (files.length > 9) {
            response.put("success", false);
            response.put("message", "最多只能上传9张图片");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // 使用项目根目录下的uploads文件夹
            String rootPath = System.getProperty("user.dir");
            String uploadPath = Paths.get(rootPath, UPLOAD_DIR).toString();
            
            // 创建上传目录（如果不存在）
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    response.put("success", false);
                    response.put("message", "无法创建上传目录");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            }
            
            // 处理每个上传的文件
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 跳过空文件
                }
                
                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = "post_" + UUID.randomUUID().toString() + fileExtension;
                
                // 保存原始文件
                File destFile = new File(uploadPath, uniqueFilename);
                file.transferTo(destFile);
                
                // 创建压缩图片文件名
                String compressedFilename = "compressed_" + uniqueFilename;
                File compressedFile = new File(uploadPath, compressedFilename);
                
                // 压缩图片
                compressImage(destFile.getAbsolutePath(), compressedFile.getAbsolutePath(), MAX_WIDTH, MAX_HEIGHT, QUALITY);
                
                // 构建可访问的URL（相对于应用根路径）
                String fileUrl = "/uploads/" + compressedFilename;
                imageUrls.add(fileUrl);
            }
            
            response.put("success", true);
            response.put("message", "图片上传成功");
            response.put("urls", imageUrls);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 压缩图片
     * @param inputImagePath 输入图片路径
     * @param outputImagePath 输出图片路径
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param quality 图片质量 (0.0 - 1.0)
     * @throws IOException
     */
    private void compressImage(String inputImagePath, String outputImagePath, int maxWidth, int maxHeight, float quality) throws IOException {
        // 读取原始图片
        BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
        
        // 计算压缩后的尺寸
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // 如果原始图片尺寸小于最大尺寸，则不进行缩放
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            // 直接保存原始图片
            ImageIO.write(originalImage, "jpg", new File(outputImagePath));
            return;
        }
        
        // 计算缩放比例
        double ratio = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        // 创建缩放后的图片
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        
        // 保存压缩后的图片
        ImageIO.write(compressedImage, "jpg", new File(outputImagePath));
    }
}