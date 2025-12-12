package com.example.foodforum.controller;

import com.example.foodforum.entity.User;
import com.example.foodforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @PostMapping
    public String createUser(@RequestBody User user) {
        int result = userService.insert(user);
        if (result > 0) {
            return "用户创建成功";
        } else {
            return "用户创建失败";
        }
    }
    
    // 管理员更新用户信息
    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        int result = userService.update(user);
        if (result > 0) {
            return "用户更新成功";
        } else {
            return "用户更新失败";
        }
    }
    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        int result = userService.deleteById(id);
        if (result > 0) {
            return "用户删除成功";
        } else {
            return "用户删除失败";
        }
    }
    
    // 更新用户状态
    @PutMapping("/{id}/status")
    public Map<String, Object> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> requestBody) {
        Map<String, Object> result = new HashMap<>();
        Integer status = requestBody.get("status");
        
        if (status == null) {
            result.put("success", false);
            result.put("message", "状态值不能为空");
            return result;
        }
        
        boolean success = userService.updateUserStatus(id, status);
        if (success) {
            result.put("success", true);
            result.put("message", "用户状态更新成功");
        } else {
            result.put("success", false);
            result.put("message", "用户状态更新失败");
        }
        
        return result;
    }
    
    // 获取当前登录用户的信息
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 创建一个不包含密码的新用户对象
            User userWithoutPassword = new User();
            userWithoutPassword.setId(user.getId());
            userWithoutPassword.setUsername(user.getUsername());
            userWithoutPassword.setEmail(user.getEmail());
            userWithoutPassword.setNickname(user.getNickname());
            userWithoutPassword.setAvatar(user.getAvatar());
            return ResponseEntity.ok(userWithoutPassword);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 用户更新自己的信息
    @PutMapping("/current/{id}")
    public ResponseEntity<String> updateCurrentUser(@PathVariable Long id, @RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录");
        }
        
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权修改其他用户的信息");
        }
        
        // 调用服务层更新用户信息
        boolean success = userService.updateUserInfo(id, user.getNickname(), user.getEmail());
        if (success) {
            // 更新session中的用户信息
            currentUser.setNickname(user.getNickname());
            currentUser.setEmail(user.getEmail());
            session.setAttribute("user", currentUser);
            return ResponseEntity.ok("用户信息更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户信息更新失败");
        }
    }

    // 获取当前登录用户的个人资料
    @GetMapping("/profile")
    public Map<String, Object> getCurrentUserProfile(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户未登录");
            return result;
        }
        
        result.put("success", true);
        result.put("user", user);
        return result;
    }
    
    /**
     * 更新用户个人资料（极度简化版，直接通过ID更新）
     */
    @PutMapping("/profile")
    public Map<String, Object> updateCurrentUserProfile(@RequestBody User updatedUser) {
        Map<String, Object> result = new HashMap<>();
        
        // 直接通过ID更新用户，不进行任何安全检查
        if (updatedUser.getId() == null) {
            result.put("success", false);
            result.put("message", "用户ID不能为空");
            return result;
        }
        
        // 查找用户
        User userToUpdate = userService.findById(updatedUser.getId());
        if (userToUpdate == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        
        // 只更新提供的字段（如果有提供的话）
        if (updatedUser.getNickname() != null) {
            userToUpdate.setNickname(updatedUser.getNickname());
        }
        if (updatedUser.getEmail() != null) {
            userToUpdate.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAvatar() != null) {
            userToUpdate.setAvatar(updatedUser.getAvatar());
        }
        if (updatedUser.getGender() != null) {
            userToUpdate.setGender(updatedUser.getGender());
        }
        if (updatedUser.getBirthday() != null) {
            userToUpdate.setBirthday(updatedUser.getBirthday());
        }
        if (updatedUser.getSignature() != null) {
            userToUpdate.setSignature(updatedUser.getSignature());
        }
        if (updatedUser.getPhone() != null) {
            userToUpdate.setPhone(updatedUser.getPhone());
        }
        
        // 执行更新
        int updateResult = userService.update(userToUpdate);
        if (updateResult > 0) {
            result.put("success", true);
            result.put("message", "个人资料更新成功");
            result.put("user", userToUpdate);
        } else {
            result.put("success", false);
            result.put("message", "个人资料更新失败");
        }
        
        return result;
    }
}