package com.example.foodforum.controller;

import com.example.foodforum.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", statsService.getUserCount());
        stats.put("postCount", statsService.getPostCount());
        stats.put("commentCount", statsService.getCommentCount());
        stats.put("categoryCount", statsService.getCategoryCount());
        return stats;
    }
}