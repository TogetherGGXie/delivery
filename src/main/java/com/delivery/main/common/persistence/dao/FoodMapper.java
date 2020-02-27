package com.delivery.main.common.persistence.dao;

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
@Repository
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
            "\tsequence\n" +
            "FROM\n" +
            "\t`food`\n" +
            "\twhere status = 1 and categoryId in \n" +
            "<foreach collection='categoryIds' item='categoryId' index='index'  open = '(' close = ')' separator=','> \n" +
            "#{categoryId} \n" +
            "</foreach>\n" +
            "\tORDER BY\n" +
            "\tcategoryId </script> ")
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
}
