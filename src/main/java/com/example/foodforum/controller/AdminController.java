package com.example.foodforum.controller;

import com.example.foodforum.entity.User;
import com.example.foodforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Map<String, Object> adminLogin(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        // 检查是否为管理员账户 (root/926953llk)
        if ("root".equals(username) && "926953llk".equals(password)) {
            // 创建管理员用户对象
            User adminUser = new User();
            adminUser.setId(0L); // 特殊ID表示管理员
            adminUser.setUsername("root");
            adminUser.setNickname("系统管理员");
            
            // 将管理员信息保存到session中
            session.setAttribute("admin", adminUser);
            session.setAttribute("isAdmin", true);
            
            result.put("success", true);
            result.put("message", "管理员登录成功");
            result.put("user", adminUser);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        
        return result;
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Map<String, Object> adminLogout(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // 清除管理员session
        session.removeAttribute("admin");
        session.removeAttribute("isAdmin");
        
        result.put("success", true);
        result.put("message", "管理员登出成功");
        
        return result;
    }

    /**
     * 检查管理员登录状态
     */
    @GetMapping("/status")
    public Map<String, Object> adminStatus(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin != null && isAdmin) {
            User adminUser = (User) session.getAttribute("admin");
            result.put("loggedIn", true);
            result.put("user", adminUser);
        } else {
            result.put("loggedIn", false);
        }
        
        return result;
    }
}