package com.example.foodforum.controller;

import com.example.foodforum.entity.Category;
import com.example.foodforum.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public List<Category> getAllCategories(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return categoryService.searchByKeyword(keyword);
        } else {
            return categoryService.findAll();
        }
    }
    
    @GetMapping("/active")
    public List<Category> getActiveCategories() {
        return categoryService.findActiveCategories();
    }
    
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }
    
    @PostMapping
    public Map<String, Object> createCategory(@RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        // 设置默认值
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        int insertResult = categoryService.insert(category);
        if (insertResult > 0) {
            result.put("success", true);
            result.put("message", "分类添加成功");
        } else {
            result.put("success", false);
            result.put("message", "分类添加失败");
        }
        return result;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        category.setId(id);
        
        int updateResult = categoryService.update(category);
        if (updateResult > 0) {
            result.put("success", true);
            result.put("message", "分类更新成功");
        } else {
            result.put("success", false);
            result.put("message", "分类更新失败");
        }
        return result;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        
        int deleteResult = categoryService.deleteById(id);
        if (deleteResult > 0) {
            result.put("success", true);
            result.put("message", "分类删除成功");
        } else {
            result.put("success", false);
            result.put("message", "分类删除失败");
        }
        return result;
    }
}