package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.CategoryMapper;
import com.delivery.main.common.persistence.service.CategoryService;
import com.delivery.main.common.persistence.template.modal.Category;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
