package com.example.foodforum.service;

import com.example.foodforum.mapper.CategoryMapper;
import com.example.foodforum.mapper.CommentMapper;
import com.example.foodforum.mapper.PostMapper;
import com.example.foodforum.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public int getUserCount() {
        // 这里可以使用更精确的计数方法，如果mapper中有count方法的话
        return userMapper.findAll().size();
    }

    public int getPostCount() {
        return postMapper.countAll();
    }

    public int getCommentCount() {
        // 如果CommentMapper中有count方法，可以直接使用
        return commentMapper.findAll().size();
    }

    public int getCategoryCount() {
        return categoryMapper.findAll().size();
    }
}