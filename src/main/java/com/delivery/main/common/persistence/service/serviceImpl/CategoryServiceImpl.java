package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.CategoryMapper;
import com.delivery.main.common.persistence.service.CategoryService;
import com.delivery.main.common.persistence.template.modal.Category;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<HashMap<String, Object>> getCategories(Integer restaurantId) {
        return categoryMapper.getCagegories(restaurantId);
    }

    @Override
    public Page<HashMap<String, Object>> getCategoryList(Page<HashMap<String, Object>> pager, Integer restaurantId) {
        Page<HashMap<String, Object>> page = new Page<>(pager.getCurrent(),pager.getSize());
        return page.setRecords(categoryMapper.getCategoryList(page, restaurantId));
    }
}
