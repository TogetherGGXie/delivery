package com.delivery.main.common.persistence.service.serviceImpl;

import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.delivery.main.common.persistence.dao.RestaurantMapper;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
