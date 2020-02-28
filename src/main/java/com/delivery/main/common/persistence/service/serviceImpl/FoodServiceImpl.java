package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.CategoryMapper;
import com.delivery.main.common.persistence.dao.FoodMapper;
import com.delivery.main.common.persistence.service.FoodService;
import com.delivery.main.common.persistence.template.modal.Food;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<HashMap<String, Object>> getFoods(Integer restaurantId) {
        List<HashMap<String, Object>> categories = new ArrayList<>();
        List<HashMap<String, Object>> foods = new ArrayList<>();
        categories = categoryMapper.getCagegories(restaurantId);
        List<Integer> categoryIds = new ArrayList<>();
        List<HashMap<String, Object>> object = new ArrayList<>();
        for (HashMap<String, Object> ca : categories) {
            if (!categoryIds.contains(ca.get("categoryId"))) {
                categoryIds.add((Integer) ca.get("categoryId"));
                HashMap<String, Object> cate = new HashMap<>();
                cate.put("category", ca);
                object.add(cate);
            }
        }
        foods = foodMapper.getFoods(categoryIds);
        for (HashMap<String, Object> o : object) {
            List<HashMap<String, Object>> foodList = new ArrayList<>();
            for (HashMap<String, Object> f : foods) {
                if (f.get("categoryId").equals((((HashMap) o.get("category")).get("categoryId")))) {
                    foodList.add(f);
                }
            }
            o.put("foodList", foodList);
        }
        return object;
    }
}
