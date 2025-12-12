package com.example.foodforum.mapper;

import com.example.foodforum.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    
    @Select("SELECT * FROM users")
    List<User> findAll();
    
    @Insert("INSERT INTO users(username, email, password, nickname, avatar, gender, birthday, signature, phone, status) " +
            "VALUES(#{username}, #{email}, #{password}, #{nickname}, #{avatar}, #{gender}, #{birthday}, #{signature}, #{phone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Update("UPDATE users SET username=#{username}, email=#{email}, nickname=#{nickname}, avatar=#{avatar}, " +
            "gender=#{gender}, birthday=#{birthday}, signature=#{signature}, phone=#{phone}, status=#{status}, updated_at=NOW() " +
            "WHERE id=#{id}")
    int update(User user);
    
    @Update("UPDATE users SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateUserStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);
}