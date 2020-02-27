package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.*;
import com.delivery.main.common.persistence.template.modal.Admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.delivery.main.util.Result;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Controller
@RequestMapping("/admin")
@Api("管理员管理")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;


    @ApiOperation("管理员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result adminLogin(@RequestBody HashMap<String, String> loginForm,
                             HttpServletRequest request) {
        Result res = new Result();
        String adminName = loginForm.get("adminName");
        String password = loginForm.get("password");
        HashMap<String,Object> map = new HashMap<>();
        Admin admin = adminService.selectOne(new EntityWrapper<Admin>().eq("admin_name", adminName));
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("用户名错误，登陆失败");
        } else if(!password.equals(admin.getPassword())) {
            res.setStatus(-1);
            res.setMessage("密码错误，登陆失败");
        }else {
            res.setStatus(200);
            res.setMessage("登陆成功");
            request.getSession().setAttribute("admin", admin);
        }
        return res;
    }

    @ApiOperation("管理员注销登录")
    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public Result adminLogin(HttpServletRequest request) {
        Result res = new Result();
        res.setStatus(200);
        request.getSession().invalidate();
        res.setMessage("注销成功");
        return res;
    }

    @ApiOperation("查询用户名是否可用")
    @RequestMapping(value = "/checkAdminName", method = RequestMethod.GET)
    public Result checkAdminName(@RequestBody HashMap<String, String> registerForm) {
        Result res = new Result();
        String adminName = registerForm.get("adminName");
        if (adminName == null || adminName.equals("")) {
            res.setStatus(200);
            res.setMessage("账号为空");
            res.setData(false);
            return res;
        }
        Admin admin = adminService.selectOne(new EntityWrapper<Admin>().eq("admin_name",adminName));
        if(admin != null) {
            res.setStatus(200);
            res.setMessage("该账号已被注册");
            res.setData(false);
        } else {
            res.setStatus(200);
            res.setMessage("该账号可以使用");
            res.setData(true);
        }
        return res;
    }

    @ApiOperation("管理员注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result adminRegister(@RequestBody HashMap<String, String> registerForm) {
        Result res = new Result();
        String adminName = registerForm.get("adminName");
        String password = registerForm.get("password");
        String avatar = registerForm.get("avatar");
        String city = registerForm.get("city");
        Admin ad = adminService.selectOne(new EntityWrapper<Admin>().eq("admin_name",adminName));
        if (ad != null) {
            res.setStatus(-1);
            res.setMessage("该账号已被注册");
            return res;
        }
        Admin admin = new Admin();
        admin.setAdminName(adminName);
        admin.setPassword(password);
        admin.setAvatar(avatar);
        admin.setCity(city);
        admin.setAdminType(1);
        admin.setCreateTime(new Date());
        admin.setStatus(1);
        adminService.insert(admin);
        res.setMessage("注册成功");
        res.setStatus(200);
        return res;
    }

    @ApiOperation("店铺管理员获取所有菜单及商品")
    @RequestMapping("/getAllFoods")
    public Result getAllFoods(HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            List<HashMap<String, Object>> categories = new ArrayList<>();
            List<HashMap<String, Object>> foods = new ArrayList<>();
            categories = categoryService.getCategories(admin.getRestaurantId());
            List<Integer> categoryIds = new ArrayList<>();
            List<HashMap<String, Object>> object = new ArrayList<>();
            for (HashMap<String, Object> ca : categories) {
                if(!categoryIds.contains(ca.get("categoryId"))){
                    categoryIds.add((Integer)ca.get("categoryId"));
                    HashMap<String, Object> cate = new HashMap<>();
                    cate.put("category", ca);
                    object.add(cate);
                }
            }
            foods = foodService.getFoods(categoryIds);
            for (HashMap<String, Object> o: object) {
                List<HashMap<String, Object>> foodList = new ArrayList<>();
                for (HashMap<String, Object> f: foods) {
                    if (f.get("categoryId").equals(o.get("categoryId"))) {
                        foodList.add(f);
                    }
                }
                o.put("foodList", foodList);
            }
            res.setStatus(200);
            res.setMessage("获取菜品成功");
            res.setData(object);
            return res;
        }
    }

}

