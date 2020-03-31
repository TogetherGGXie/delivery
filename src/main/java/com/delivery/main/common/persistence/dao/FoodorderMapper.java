package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.Foodorder;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2020-03-30
 */
@Mapper
public interface FoodorderMapper extends BaseMapper<Foodorder> {

        @Select("Select * from `foodOrder` where order_id = #{orderId}")
        @Results(id="OrderResultMap",value={
                @Result(property = "orderId", column = "order_id"),
                @Result(property = "userId", column = "user_id"),
                @Result(property = "restaurantId", column = "restaurant_id"),
                @Result(property = "totalPrice", column = "total_price"),
                @Result(property = "foodDetails", column = "food_details"),
                @Result(property = "deliveryFee", column = "delivery_fee"),
                @Result(property = "address", column = "address"),
                @Result(property = "remarks", column = "remarks"),
                @Result(property = "status", column = "status"),
                @Result(property = "isPindan", column = "is_pindan"),
                @Result(property = "pinOrderId", column = "pin_order_id"),
                @Result(property = "createTime", column = "create_time"),
        })
        public HashMap<String, Object> queryOne(@Param("orderId") Integer orderId);

        @Select("SELECT \n" +
                "\torder.order_id, \n" +
                "\torder.user_id, \n" +
                "\tuser.username, \n" +
                "\torder.restaurant_id,\n" +
                "\trestaurant.name as restaurant_name,\n" +
                "\torder.total_price,\n" +
                "\torder.food_details,\n" +
                "\torder.delivery_fee,\n" +
                "\torder.address,\n" +
                "\torder.remarks,\n" +
                "\torder.create_time,\n" +
                "\torder.status,\n" +
                "\torder.is_pindan,\n" +
                "\torder.pin_order_id\n" +
                "FROM\n" +
                "\t`foodOrder` join `user` on `order`.user_id = `user`.user_id\n" +
                "\tjoin `restaurant` on `order`.restaurant_id = `restaurant`.restaurant_id\n" +
                "\twhere order.restaurant_id REGEXP #{restaurantId}\n" +
                "\tORDER BY\n" +
                "\torder.create_time desc")
        @Results(id="OrderListResultMap",value={
                @Result(property = "orderId", column = "order_id"),
                @Result(property = "userId", column = "user_id"),
                @Result(property = "username", column = "username"),
                @Result(property = "restaurantId", column = "restaurant_id"),
                @Result(property = "restaurantName", column = "restaurant_name"),
                @Result(property = "totalPrice", column = "total_price"),
                @Result(property = "foodDetails", column = "food_details"),
                @Result(property = "deliveryFee", column = "delivery_fee"),
                @Result(property = "address", column = "address"),
                @Result(property = "remarks", column = "remarks"),
                @Result(property = "status", column = "status"),
                @Result(property = "isPindan", column = "is_pindan"),
                @Result(property = "pinOrderId", column = "pin_order_id"),
                @Result(property = "createTime", column = "create_time"),
        })
        List<HashMap<String, Object>> getMyOrders(Pagination pagination, @Param("restaurantId") String restaurantId);


}
