package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.OrderMapper;
import com.delivery.main.common.persistence.dao.RestaurantMapper;
import com.delivery.main.common.persistence.service.OrderService;
import com.delivery.main.common.persistence.template.modal.Order;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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
        order.setAddress(o.get("address").toString());
        order.setTotalPrice(new BigDecimal(o.get("totalPrice").toString()));
        order.setCreateTime((Date)o.get("createTime"));
        order.setDeliveryFee(new BigDecimal(o.get("deliveryFee").toString()));
        order.setFoodDetails(o.get("totalPrice").toString());
        order.setIsPindan((Integer)(o.get("isPindan")));
        order.setPinOrderId((o.get("pinOrderId").toString()));
        order.setRemarks(o.get("remarks").toString());
        order.setStatus((Integer)(o.get("status")));
        return order;
    }

    @Override
    public Page<HashMap<String, Object>> getMyOrders(Page<HashMap<String, Object>> page, Integer restaurantId) {
        Page<HashMap<String, Object>> pager = new Page<>(page.getCurrent(),page.getSize());
        String Rid = restaurantId == null ? "[0-9]*" : restaurantId.toString();
        return pager.setRecords(orderMapper.getMyOrders(page, Rid));
    }
}
