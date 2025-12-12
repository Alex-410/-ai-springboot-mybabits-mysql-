package com.example.foodforum.mapper;

import com.example.foodforum.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    
    /**
     * 插入新的收藏记录
     * @param favorite 收藏实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO favorites(user_id, post_id, created_at) VALUES(#{userId}, #{postId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Favorite favorite);
    
    /**
     * 根据ID删除收藏记录
     * @param id 收藏ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM favorites WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据用户ID和帖子ID删除收藏记录（取消收藏）
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM favorites WHERE user_id = #{userId} AND post_id = #{postId}")
    int deleteByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
    
    /**
     * 根据ID查询收藏记录
     * @param id 收藏ID
     * @return 收藏实体
     */
    @Select("SELECT * FROM favorites WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    Favorite findById(Long id);
    
    /**
     * 根据用户ID和帖子ID查询收藏记录
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏实体
     */
    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND post_id = #{postId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    Favorite findByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
    
    /**
     * 查询用户的所有收藏记录
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Select("SELECT * FROM favorites WHERE user_id = #{userId} ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Favorite> findByUserId(Long userId);
    
    /**
     * 分页查询用户的收藏记录
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 收藏列表
     */
    @Select("SELECT * FROM favorites WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Favorite> findByUserIdWithPagination(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计用户的收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Select("SELECT COUNT(*) FROM favorites WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}