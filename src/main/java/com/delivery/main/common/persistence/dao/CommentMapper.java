package com.delivery.main.common.persistence.dao;

import com.delivery.main.common.persistence.template.modal.Comment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

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
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT\n" +
            "\tuser_name,\n" +
            "\tavatar,\n" +
            "\tcomment_time,\n" +
            "\tcomment,\n" +
            "\torder_score,\n" +
            "\tpackage_score,\n" +
            "\tdelivery_score,\n" +
            "\tpicture \n" +
            "FROM\n" +
            "\t`comment`\n" +
            "WHERE\n" +
            "\trestaurant_id = #{restaurantId}\n" +
            "ORDER BY\n" +
            "\tcomment_time")
    @Results(id="commentLists",value={
            @Result(property = "userName", column = "user_name"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "commentTime", column = "comment_time"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "orderScore", column = "order_score"),
            @Result(property = "packageScore", column = "package_score"),
            @Result(property = "deliveryScore", column = "delivery_score"),
            @Result(property = "picture", column = "picture"),
    })
    List<HashMap<String, Object>> getComments(@Param("restaurantId") Integer restaurantId);
}
