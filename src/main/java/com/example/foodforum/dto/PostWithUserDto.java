package com.example.foodforum.dto;

import com.example.foodforum.entity.Post;
import com.example.foodforum.entity.User;

public class PostWithUserDto {
    private Post post;
    private User user;
    
    public PostWithUserDto() {}
    
    public PostWithUserDto(Post post, User user) {
        this.post = post;
        this.user = user;
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
}