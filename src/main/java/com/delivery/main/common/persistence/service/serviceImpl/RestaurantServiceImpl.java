package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.RestaurantMapper;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RestaurantServiceImpl extends ServiceImpl<RestaurantMapper, Restaurant> implements RestaurantService {

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Override
    public Page<HashMap<String, Object>> getRestaurantList(Page<HashMap<String, Object>> pager) {
        Page<HashMap<String, Object>> page = new Page<>(pager.getCurrent(),pager.getSize());
        return page.setRecords(restaurantMapper.getRestaurantList(page));
    }
}
