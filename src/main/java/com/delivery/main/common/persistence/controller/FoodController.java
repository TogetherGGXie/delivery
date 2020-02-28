package com.delivery.main.common.persistence.controller;


import cn.hutool.core.collection.CollUtil;
import com.delivery.main.common.persistence.service.CategoryService;
import com.delivery.main.common.persistence.service.FoodService;
import com.delivery.main.common.persistence.template.modal.Category;
import com.delivery.main.common.persistence.template.modal.Food;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@RestController
@RequestMapping("/food")
@Api("商品管理")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取所有商品")
    @RequestMapping(value = "/getfoods/{restaurantId}", method = RequestMethod.GET)
    public Result getFoods(@PathVariable Integer restaurantId,
                           HttpServletRequest request) {
        Result res = new Result();
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            List<HashMap<String, Object>> object = foodService.getFoods(restaurantId);
            res.setStatus(200);
            res.setMessage("获取菜品成功");
            res.setData(object);
            return res;
        }
    }

}

