package com.example.foodforum.mapper;

import com.example.foodforum.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.id = #{id}")
    Post findById(Long id);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.status = 1 ORDER BY p.created_at DESC")
    List<Post> findAll();
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.user_id = #{userId} AND p.status = 1 ORDER BY p.created_at DESC")
    List<Post> findByUserId(Long userId);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.category_id = #{categoryId} AND p.status = 1 ORDER BY p.created_at DESC")
    List<Post> findByCategoryId(Integer categoryId);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE (p.title LIKE CONCAT('%',#{keyword},'%') OR p.content LIKE CONCAT('%',#{keyword},'%')) AND p.status = 1 ORDER BY p.created_at DESC")
    List<Post> searchByKeyword(String keyword);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE (p.title LIKE CONCAT('%',#{keyword},'%') OR p.content LIKE CONCAT('%',#{keyword},'%')) AND p.category_id = #{categoryId} AND p.status = 1 ORDER BY p.created_at DESC")
    List<Post> searchByKeywordAndCategoryId(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);
    
    @Select("SELECT COUNT(*) FROM posts WHERE status = 1")
    int countAll();
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.status = 1 ORDER BY p.created_at DESC LIMIT #{offset}, #{limit}")
    List<Post> findAllWithPagination(int offset, int limit);
    
    @Insert("INSERT INTO posts(user_id, category_id, title, content, images, status) " +
            "VALUES(#{userId}, #{categoryId}, #{title}, #{content}, #{images}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);
    
    @Update("UPDATE posts SET title=#{title}, content=#{content}, images=#{images}, category_id=#{categoryId}, " +
            "status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Post post);
    
    @Update("UPDATE posts SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updatePostStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Delete("DELETE FROM posts WHERE id = #{id}")
    int deleteById(Long id);
    
    @Update("UPDATE posts SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    
    @Update("UPDATE posts SET favorite_count = favorite_count + 1 WHERE id = #{id}")
    int incrementFavoriteCount(Long id);
    
    // 新增支持分页的搜索方法
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.title LIKE CONCAT('%',#{keyword},'%') OR p.content LIKE CONCAT('%',#{keyword},'%') ORDER BY p.created_at DESC LIMIT #{offset}, #{limit}")
    List<Post> searchByKeywordWithPagination(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE (p.title LIKE CONCAT('%',#{keyword},'%') OR p.content LIKE CONCAT('%',#{keyword},'%')) AND p.category_id = #{categoryId} ORDER BY p.created_at DESC LIMIT #{offset}, #{limit}")
    List<Post> searchByKeywordAndCategoryIdWithPagination(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count FROM posts p WHERE p.category_id = #{categoryId} ORDER BY p.created_at DESC LIMIT #{offset}, #{limit}")
    List<Post> findByCategoryIdWithPagination(@Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM posts WHERE title LIKE CONCAT('%',#{keyword},'%') OR content LIKE CONCAT('%',#{keyword},'%')")
    int countByKeyword(@Param("keyword") String keyword);
    
    @Select("SELECT COUNT(*) FROM posts WHERE (title LIKE CONCAT('%',#{keyword},'%') OR content LIKE CONCAT('%',#{keyword},'%')) AND category_id = #{categoryId}")
    int countByKeywordAndCategoryId(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);
    
    @Select("SELECT COUNT(*) FROM posts WHERE category_id = #{categoryId}")
    int countByCategoryId(@Param("categoryId") Integer categoryId);
    
    @Select({"<script>",
             "SELECT p.id, p.user_id, p.category_id, p.title, p.content, p.images, p.view_count, p.like_count, p.comment_count, p.is_top, p.is_essence, p.status, p.created_at, p.updated_at, ",
             "       (SELECT COUNT(*) FROM favorites WHERE post_id = p.id) as favorite_count ",
             "FROM posts p ",
             "WHERE p.id IN ",
             "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
             "#{id}",
             "</foreach>",
             " ORDER BY p.created_at DESC",
             "</script>"})
    List<Post> findByIds(@Param("ids") List<Long> ids);
}