package com.example.foodforum.mapper;

import com.example.foodforum.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    
    @Insert("INSERT INTO favorites (user_id, post_id) VALUES (#{userId}, #{postId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Favorite favorite);
    
    @Delete("DELETE FROM favorites WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM favorites WHERE user_id = #{userId} AND post_id = #{postId}")
    int deleteByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
    
    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND post_id = #{postId}")
    Favorite findByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
    
    @Select("SELECT * FROM favorites WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Favorite> findByUserId(Long userId);
    
    @Select("SELECT * FROM favorites WHERE id = #{id}")
    Favorite findById(Long id);
    
    @Select("SELECT COUNT(*) FROM favorites WHERE post_id = #{postId}")
    int countByPostId(Long postId);
}