package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.Food;
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
public interface FoodMapper extends BaseMapper<Food> {

    @Select("<script>SELECT \n" +
            "\tfood_id, \n" +
            "\tcategory_id, \n" +
            "\tname,\n" +
            "\tcomment_num,\n" +
            "\tscore,\n" +
            "\tpicture,\n" +
            "\tdescription,\n" +
            "\tmonth_sale,\n" +
            "\ttag,\n" +
            "\tpromotion_info,\n" +
            "\tprice\n" +
            "FROM\n" +
            "\t`food`\n" +
            "\twhere status = 1 and category_id in \n" +
            "<foreach collection='categoryIds' item='categoryId' index='index'  open = '(' close = ')' separator=','> \n" +
            "#{categoryId} \n" +
            "</foreach>\n" +
            "\tORDER BY\n" +
            "\tcategory_id </script> ")
    @Results(id="FoodsResultMap",value={
            @Result(property = "foodId", column = "food_id"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "commentNum", column = "comment_num"),
            @Result(property = "score", column = "score"),
            @Result(property = "picture", column = "picture"),
            @Result(property = "description", column = "description"),
            @Result(property = "monthSale", column = "month_sale"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "promotionInfo", column = "promotion_info"),
            @Result(property = "price", column = "price")
    })
    List<HashMap<String, Object>> getFoods(@Param("categoryIds") List<Integer> categoryIds);

    @Select("SELECT \n" +
            "\tfood.food_id, \n" +
            "\tfood.category_id, \n" +
            "\tcategory.name as category_name, \n" +
            "\tfood.name,\n" +
            "\tfood.comment_num,\n" +
            "\tfood.score,\n" +
            "\tfood.picture,\n" +
            "\tfood.description,\n" +
            "\tfood.month_sale,\n" +
            "\tfood.tag,\n" +
            "\tfood.promotion_info,\n" +
            "\tfood.price,\n" +
            "\tfood.create_time,\n" +
            "\tfood.last_upd_time,\n" +
            "\tfood.status\n" +
            "FROM\n" +
            "\t`food` join `category` on food.category_id = category.category_id\n" +
            "\twhere food.restaurant_id = #{restaurantId}\n" +
            "\tORDER BY\n" +
            "\tfood.create_time")
    @Results(id="FoodListResultMap",value={
            @Result(property = "foodId", column = "food_id"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "categoryName", column = "category_name"),
            @Result(property = "name", column = "name"),
            @Result(property = "commentNum", column = "comment_num"),
            @Result(property = "score", column = "score"),
            @Result(property = "picture", column = "picture"),
            @Result(property = "description", column = "description"),
            @Result(property = "monthSale", column = "month_sale"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "promotionInfo", column = "promotion_info"),
            @Result(property = "price", column = "price"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "lasUpdTime", column = "last_upd_time"),
            @Result(property = "status", column = "status")
    })
    List<HashMap<String, Object>> getFoodList(Pagination pagination, @Param("restaurantId") Integer restaurantId);
}
