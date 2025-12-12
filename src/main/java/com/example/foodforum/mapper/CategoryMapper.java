package com.example.foodforum.mapper;

import com.example.foodforum.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    @Select("SELECT * FROM categories")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Category> findAll();
    
    @Select("SELECT * FROM categories WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    Category findById(Integer id);
    
    @Select("SELECT * FROM categories WHERE status = 1 ORDER BY sort_order ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Category> findActiveCategories();
    
    @Insert("INSERT INTO categories(name, description, sort_order, status, created_at) " +
            "VALUES(#{name}, #{description}, #{sortOrder}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);
    
    @Update("UPDATE categories SET name=#{name}, description=#{description}, " +
            "sort_order=#{sortOrder}, status=#{status} WHERE id=#{id}")
    int update(Category category);
    
    @Delete("DELETE FROM categories WHERE id = #{id}")
    int deleteById(Integer id);
}