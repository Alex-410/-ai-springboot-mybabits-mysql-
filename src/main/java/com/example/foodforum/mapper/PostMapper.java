package com.example.foodforum.mapper;

import com.example.foodforum.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    
    @Select("SELECT * FROM posts WHERE id = #{id}")
    Post findById(Long id);
    
    @Select("SELECT * FROM posts ORDER BY created_at DESC")
    List<Post> findAll();
    
    @Select("SELECT * FROM posts WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Post> findByUserId(Long userId);
    
    @Select("SELECT * FROM posts WHERE category_id = #{categoryId} ORDER BY created_at DESC")
    List<Post> findByCategoryId(Integer categoryId);
    
    @Select("SELECT * FROM posts WHERE title LIKE CONCAT('%',#{keyword},'%') OR content LIKE CONCAT('%',#{keyword},'%') ORDER BY created_at DESC")
    List<Post> searchByKeyword(String keyword);
    
    @Select("SELECT COUNT(*) FROM posts")
    int countAll();
    
    @Select("SELECT * FROM posts ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<Post> findAllWithPagination(int offset, int limit);
    
    @Insert("INSERT INTO posts(user_id, category_id, title, content, images, status) " +
            "VALUES(#{userId}, #{categoryId}, #{title}, #{content}, #{images}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);
    
    @Update("UPDATE posts SET title=#{title}, content=#{content}, images=#{images}, category_id=#{categoryId}, " +
            "status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Post post);
    
    @Delete("DELETE FROM posts WHERE id = #{id}")
    int deleteById(Long id);
    
    @Update("UPDATE posts SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    
    @Update("UPDATE posts SET favorite_count = favorite_count + 1 WHERE id = #{id}")
    int incrementFavoriteCount(Long id);
}