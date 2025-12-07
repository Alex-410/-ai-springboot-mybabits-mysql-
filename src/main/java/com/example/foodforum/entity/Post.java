package com.example.foodforum.entity;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Post {
    private Long id;
    private Long userId;
    private Integer categoryId;
    private String title;
    private String content;
    private String images; // 存储图片URL列表的JSON字符串
    private List<String> imageUrls; // 用于前端传输的图片URL列表
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Integer isTop;
    private Integer isEssence;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 获取图片URL列表的JSON字符串（用于数据库存储）
    public String getImages() {
        return images;
    }

    // 设置图片URL列表的JSON字符串（用于数据库存储）
    public void setImages(String images) {
        this.images = images;
    }

    // 获取图片URL列表（用于前端传输）
    @JsonIgnore
    public List<String> getImageUrls() {
        return imageUrls;
    }

    // 设置图片URL列表（用于前端传输）
    @JsonIgnore
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(Integer isEssence) {
        this.isEssence = isEssence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}