package com.example.foodforum.service;

import com.example.foodforum.entity.User;
import com.example.foodforum.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User findById(Long id) {
        return userMapper.findById(id);
    }
    
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    public List<User> searchByKeyword(String keyword) {
        return userMapper.searchByKeyword(keyword);
    }
    
    public int insert(User user) {
        return userMapper.insert(user);
    }
    
    public int update(User user) {
        return userMapper.update(user);
    }
    
    public int deleteById(Long id) {
        return userMapper.deleteById(id);
    }
    
    // 更新用户信息（仅更新昵称和邮箱）
    public boolean updateUserInfo(Long id, String nickname, String email) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setNickname(nickname);
            user.setEmail(email);
            return userMapper.update(user) > 0;
        }
        return false;
    }
    
    // 更新用户个人资料（包括头像、生日、签名、电话等）
    public boolean updateUserProfile(Long id, String nickname, String email, String avatar, 
                                   Integer gender, String birthday, String signature, String phone) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setNickname(nickname);
            user.setEmail(email);
            user.setAvatar(avatar);
            user.setGender(gender);
            // 将字符串类型的生日转换为Date类型
            if (birthday != null && !birthday.isEmpty()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    user.setBirthday(sdf.parse(birthday));
                } catch (Exception e) {
                    user.setBirthday(null);
                }
            } else {
                user.setBirthday(null);
            }
            user.setSignature(signature);
            user.setPhone(phone);
            return userMapper.update(user) > 0;
        }
        return false;
    }
    
    // 更新用户状态
    public boolean updateUserStatus(Long id, Integer status) {
        return userMapper.updateUserStatus(id, status) > 0;
    }
}