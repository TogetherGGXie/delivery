package com.delivery.main.common.persistence.dao;

import com.delivery.main.common.persistence.template.modal.Order;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("Select order_id, user_id, restaurant_id from `order` where order_id = #{orderId}")
    @Results(id="OrderResult",value={
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "restaurantId", column = "restaurant_id")
    })
    public HashMap<String, Object> queryOne(@Param("orderId") Integer orderId);
}
