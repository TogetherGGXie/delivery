package com.delivery.main.common.persistence.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
public interface RestaurantService extends IService<Restaurant> {

    Page<HashMap<String, Object>> getRestaurantList(Page<HashMap<String, Object>> page);
}
