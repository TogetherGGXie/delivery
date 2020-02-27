package com.delivery.main.common.persistence.controller;


import com.delivery.main.common.persistence.service.CategoryService;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Controller
@RequestMapping("/category")
@Api("菜单管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取商店菜单")
    @RequestMapping(path = "/getCategories/{restaurantId}", method = RequestMethod.GET)
    public Result getCategories(@PathVariable Integer restaurantId,
                                HttpServletRequest request) {
        Result res = new Result();
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            res.setStatus(200);
            res.setMessage("获取菜单分类成功");
            res.setData(categoryService.getCategories(restaurantId));
            return res;
        }
    }
}

