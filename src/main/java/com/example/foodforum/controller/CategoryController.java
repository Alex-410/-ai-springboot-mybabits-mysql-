package com.example.foodforum.controller;

import com.example.foodforum.entity.Category;
import com.example.foodforum.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }
    
    @GetMapping("/active")
    public List<Category> getActiveCategories() {
        return categoryService.findActiveCategories();
    }
}