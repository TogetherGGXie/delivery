package com.delivery.main.common.persistence.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.delivery.main.common.persistence.template.modal.Foodorder;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
public interface FoodorderService extends IService<Foodorder> {
    HashMap<String,Object> queryOrder(Foodorder foodorder);
    Foodorder queryOne(Integer orderId);
    Page<HashMap<String, Object>> getMyOrders(Page<HashMap<String, Object>> page, Integer restaurantId);
    Page<HashMap<String, Object>> getAllOrders(Page<HashMap<String, Object>> page, Integer restaurantId);
}
