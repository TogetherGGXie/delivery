package com.delivery.main.common.persistence.service;

import com.baomidou.mybatisplus.service.IService;
import com.delivery.main.common.persistence.template.modal.Order;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
public interface OrderService extends IService<Order> {
    HashMap<String,Object> queryOrder(Order order);
    Order queryOne(Integer orderId);
}
