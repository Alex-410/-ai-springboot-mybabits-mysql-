package com.example.foodforum.mapper;

import com.example.foodforum.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    @Select("SELECT c.*, u.nickname AS userNickname, u.avatar AS userAvatar " +
           "FROM comments c " +
           "JOIN users u ON c.user_id = u.id " +
           "WHERE c.id = #{id}")
    Comment findById(Long id);
    
    @Select("SELECT c.*, u.nickname AS userNickname, u.avatar AS userAvatar " +
           "FROM comments c " +
           "JOIN users u ON c.user_id = u.id " +
           "WHERE c.post_id = #{postId} " +
           "ORDER BY c.created_at ASC")
    List<Comment> findByPostId(Long postId);
    
    @Select("SELECT c.*, u.nickname AS userNickname, u.avatar AS userAvatar " +
           "FROM comments c " +
           "JOIN users u ON c.user_id = u.id " +
           "WHERE c.post_id = #{postId} " +
           "ORDER BY c.created_at ASC " +
           "LIMIT #{offset}, #{limit}")
    List<Comment> findByPostIdWithPagination(Long postId, int offset, int limit);
    
    @Select("SELECT COUNT(*) " +
           "FROM comments c " +
           "WHERE c.post_id = #{postId}")
    long countByPostId(Long postId);
    
    @Select("SELECT c.*, u.nickname AS userNickname, u.avatar AS userAvatar " +
           "FROM comments c " +
           "JOIN users u ON c.user_id = u.id " +
           "WHERE c.user_id = #{userId} " +
           "ORDER BY c.created_at DESC")
    List<Comment> findByUserId(Long userId);
    
    @Select("SELECT c.*, u.nickname AS userNickname, u.avatar AS userAvatar " +
           "FROM comments c " +
           "JOIN users u ON c.user_id = u.id " +
           "ORDER BY c.created_at DESC")
    List<Comment> findAll();
    
    @Insert("INSERT INTO comments(post_id, user_id, content, parent_id, status) " +
            "VALUES(#{postId}, #{userId}, #{content}, #{parentId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);
    
    @Update("UPDATE comments SET post_id=#{postId}, user_id=#{userId}, content=#{content}, " +
            "parent_id=#{parentId}, status=#{status}, updated_at=NOW() " +
            "WHERE id=#{id}")
    int update(Comment comment);
    
    @Delete("DELETE FROM comments WHERE id = #{id}")
    int deleteById(Long id);
}