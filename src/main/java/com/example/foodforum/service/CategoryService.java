package com.example.foodforum.service;

import com.example.foodforum.entity.Category;
import com.example.foodforum.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
    
    public List<Category> searchByKeyword(String keyword) {
        return categoryMapper.searchByKeyword(keyword);
    }
    
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }
    
    public List<Category> findActiveCategories() {
        return categoryMapper.findActiveCategories();
    }
    
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }
    
    public int update(Category category) {
        return categoryMapper.update(category);
    }
    
    public int deleteById(Integer id) {
        return categoryMapper.deleteById(id);
    }
}