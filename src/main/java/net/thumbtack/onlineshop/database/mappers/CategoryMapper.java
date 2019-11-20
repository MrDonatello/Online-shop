package net.thumbtack.onlineshop.database.mappers;

import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CategoryMapper {

    @Insert("INSERT INTO category (name, parentid) VALUES " +
            "( #{name}, #{parent.id} )")
    @Options(useGeneratedKeys = true)
    void addCategory(Category category);

    @Select({"SELECT category.name, category.id  FROM category WHERE category.id = #{parent.id}"})
    Category getParent(Category category);

    @Select({"SELECT category.name, category.id  FROM category WHERE category.id = (SELECT category.parentid  FROM category WHERE category.id = #{id})"})
    Category getParentById(Category category);

    @Select({"SELECT category.name, category.id  FROM category WHERE category.id = #{categoryId}"})
    Category getSubcategory(String categoryId);


    @Select("SELECT category.name, category.id, category.parentid AS idd  FROM category WHERE category.id = #{categoryId} ")
    @Results({
            @Result(property = "idd", column = "category.id"),
            @Result(property = "parent", column = "idd", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mappers.CategoryMapper.getParent", fetchType = FetchType.DEFAULT))
    })
    Category getCategory(String categoryId);

    @Select("SELECT category.name FROM category WHERE id = #{i}")
    String getCategoryName(int i);

    @Update("UPDATE category SET category.name = #{name} WHERE category.id = #{id}")
    void updateNameCategory(Category category);

    @Update("UPDATE category SET category.parentid = #{parent.id} WHERE category.id = #{id}")
    void updateParentCategory(Category category);

    @Delete("DELETE  FROM category WHERE  category.id = #{id}")
    void deleteCategory(String id);

    @Delete("DELETE FROM category WHERE  category.parentid = #{id}")
    void deleteSubCategory(String id);

    @Select("SELECT category.name, category.id, category.parentid AS idd  FROM category ")
    @Results({
            @Result(property = "idd", column = "category.id"),
            @Result(property = "parent", column = "idd", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mappers.CategoryMapper.getParent", fetchType = FetchType.DEFAULT))
    })
    List<Category> getAllCategory();
}
