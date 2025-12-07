package com.example.foodforum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源放行
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        
        // 添加上传文件的静态资源映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}