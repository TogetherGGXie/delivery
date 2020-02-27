package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.FoodMapper;
import com.delivery.main.common.persistence.service.FoodService;
import com.delivery.main.common.persistence.template.modal.Food;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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

    @Autowired
    private FoodMapper foodMapper;

    @Override
    public List<HashMap<String, Object>> getFoods(List<Integer> categoryIds) {
        return foodMapper.getFoods(categoryIds);
    }
}
