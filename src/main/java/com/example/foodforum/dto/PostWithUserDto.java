package com.example.foodforum.dto;

import com.example.foodforum.entity.Post;
import com.example.foodforum.entity.User;

public class PostWithUserDto {
    private Post post;
    private User user;
    private String categoryName;
    
    public PostWithUserDto() {}
    
    public PostWithUserDto(Post post, User user) {
        this.post = post;
        this.user = user;
    }
    
    public PostWithUserDto(Post post, User user, String categoryName) {
        this.post = post;
        this.user = user;
        this.categoryName = categoryName;
    }
    
    // Getters and Setters
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}