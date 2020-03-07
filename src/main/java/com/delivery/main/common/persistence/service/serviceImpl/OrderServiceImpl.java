package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.OrderMapper;
import com.delivery.main.common.persistence.dao.RestaurantMapper;
import com.delivery.main.common.persistence.service.OrderService;
import com.delivery.main.common.persistence.template.modal.Order;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RestaurantMapper restaurantMapper;

    @Override
    public HashMap<String, Object> queryOrder(Order order) {
        Integer restaurantId = order.getRestaurantId();
        String foodDetails = order.getFoodDetails();
        String address = order.getAddress();
        Restaurant restaurant = restaurantMapper.selectById(restaurantId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("restaurantInfo",restaurant);
        hashMap.put("foodList",foodDetails);
        hashMap.put("address",address);
        hashMap.put("orderInfo",order);
        return hashMap;
    }

    @Override
    public Order queryOne(Integer orderId) {
        HashMap<String, Object> o = orderMapper.queryOne(orderId);
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId((Integer)(o.get("userId")));
        order.setRestaurantId((Integer)(o.get("restaurantId")));
        return order;
    }
}
