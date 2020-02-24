package com.delivery.main.common.persistence.service.serviceImpl;

import com.delivery.main.common.persistence.template.modal.Food;
import com.delivery.main.common.persistence.dao.FoodMapper;
import com.delivery.main.common.persistence.service.FoodService;
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
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

}
