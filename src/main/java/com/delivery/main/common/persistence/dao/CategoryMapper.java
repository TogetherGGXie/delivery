package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.Category;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("SELECT\n" +
            "\tcategory_id,\n" +
            "\tname,\n" +
            "\ticon\n" +
            "FROM\n" +
            "\t`category`\n" +
            "WHERE\n" +
            "\trestaurant_id = #{restaurantId}\n" +
            "\tand status = 1\n" +
            "ORDER BY\n" +
            "\tcategory_id")
    @Results(id="categoryLists",value={
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "icon", column = "icon"),
    })
    List<HashMap<String, Object>> getCagegories(@Param("restaurantId") Integer restaurantId);

    @Select("SELECT\n" +
            "\tcategory_id,\n" +
            "\tname,\n" +
            "\ticon,\n" +
            "\tcreate_time,\n" +
            "\tstatus\n" +
            "FROM\n" +
            "\t`category`\n" +
            "WHERE\n" +
            "\trestaurant_id = #{restaurantId}\n" +
            "ORDER BY\n" +
            "\tcreate_time")
    @Results(id="categoryList",value={
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "icon", column = "icon"),
            @Result(property = "create_time", column = "create_time"),
            @Result(property = "status", column = "status"),
    })
    List<HashMap<String, Object>> getCategoryList(Pagination pagination, @Param("restaurantId") Integer restaurantId);
}
