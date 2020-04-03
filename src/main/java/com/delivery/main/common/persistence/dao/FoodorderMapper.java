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
                "\tfoodorder.order_id, \n" +
                "\tfoodorder.user_id, \n" +
                "\tuser.username, \n" +
                "\tfoodorder.restaurant_id,\n" +
                "\trestaurant.name as restaurant_name,\n" +
                "\tfoodorder.total_price,\n" +
                "\tfoodorder.food_details,\n" +
                "\tfoodorder.delivery_fee,\n" +
                "\tfoodorder.address,\n" +
                "\tfoodorder.remarks,\n" +
                "\tfoodorder.create_time,\n" +
                "\tfoodorder.status,\n" +
                "\tfoodorder.is_pindan,\n" +
                "\tfoodorder.pin_order_id\n" +
                "FROM\n" +
                "\t`foodorder` join `user` on `foodorder`.user_id = `user`.user_id\n" +
                "\tjoin `restaurant` on `foodorder`.restaurant_id = `restaurant`.restaurant_id\n" +
                "\twhere foodorder.restaurant_id REGEXP #{restaurantId}\n" +
                "\tORDER BY\n" +
                "\tfoodorder.create_time desc")
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



        @Select("SELECT \n" +
                "\tfoodorder.order_id, \n" +
                "\tfoodorder.user_id, \n" +
                "\tuser.username, \n" +
                "\tfoodorder.restaurant_id,\n" +
                "\trestaurant.name as restaurant_name,\n" +
                "\tfoodorder.total_price,\n" +
                "\tfoodorder.food_details,\n" +
                "\tfoodorder.delivery_fee,\n" +
                "\tfoodorder.address,\n" +
                "\tfoodorder.remarks,\n" +
                "\tfoodorder.create_time,\n" +
                "\tfoodorder.status,\n" +
                "\tfoodorder.is_pindan,\n" +
                "\tfoodorder.pin_order_id\n" +
                "FROM\n" +
                "\t`foodorder` join `user` on `foodorder`.user_id = `user`.user_id\n" +
                "\tjoin `restaurant` on `foodorder`.restaurant_id = `restaurant`.restaurant_id\n" +
                "\twhere foodorder.restaurant_id REGEXP #{restaurantId}\n" +
                "\tORDER BY\n " +
                "\tfoodorder.restaurant_id asc,\n" +
                "\tfoodorder.create_time desc")
        @Results(id="AllOrderListResultMap",value={
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
        List<HashMap<String, Object>> getAllOrders(Pagination pagination, @Param("restaurantId") String restaurantId);


}
