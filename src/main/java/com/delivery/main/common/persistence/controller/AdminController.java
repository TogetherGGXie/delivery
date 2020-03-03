package com.delivery.main.common.persistence.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.service.*;
import com.delivery.main.common.persistence.template.modal.Admin;
import com.delivery.main.common.persistence.template.modal.Comment;
import com.delivery.main.common.persistence.template.modal.Food;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
@RestController
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
    @RequestMapping(value = "/getAllFoods/{restaurantId}", method = RequestMethod.GET)
    public Result getAllFoods(@PathVariable Integer restaurantId,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && !admin.getRestaurantId().equals(restaurantId)) {
            res.setStatus(-1);
            res.setMessage("权限不足，获取菜单失败");
            return res;
        } else {
            List<HashMap<String, Object>> object = foodService.getFoods(restaurantId);
            res.setStatus(200);
            res.setMessage("获取菜品成功");
            res.setData(object);
            return res;
        }
    }

    @ApiOperation("店铺管理员添加商品")
    @RequestMapping(value = "/addFood", method = RequestMethod.POST)
    public Result addFood(@RequestBody Food food,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 2 && food.getRestaurantId() == null ) {
            res.setStatus(-1);
            res.setMessage("请选择商店");
            return res;
        } else if (admin.getAdminType() == 1 && (food.getRestaurantId().equals(admin.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("您无权添加商品");
            return res;
        }else {
            Restaurant restaurant = restaurantService.selectById(admin.getAdminType() == 1 ? admin.getRestaurantId() : food.getRestaurantId());
            if (restaurant == null) {
                res.setStatus(-1);
                res.setMessage("该商店不存在");
                return res;
            }
            food.setCommentNum(0);
            food.setScore(new BigDecimal(0));
            food.setMonthSale(0);
            food.setCreateTime(new Date());
            food.setLastUpdTime(new Date());
            if (foodService.insert(food)) {
                res.setStatus(200);
                res.setMessage("添加商品成功");
                res.setData(food);
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("添加商品失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员修改商品")
    @RequestMapping(value = "/updateFood", method = RequestMethod.POST)
    public Result updateFood(@RequestBody Food food,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(food.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("权限不足，修改商品失败");
            return res;
        } else {
            food.setLastUpdTime(new Date());
            if (foodService.updateById(food)) {
                res.setStatus(200);
                res.setMessage("修改商品成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("修改商品失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员删除商品")
    @RequestMapping(value = "/deleteFood", method = RequestMethod.POST)
    public Result deleteFood(@RequestBody HashMap<String, String> foodForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            Food food = foodService.selectById(Integer.valueOf(foodForm.get("foodId")));
            if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(food.getRestaurantId()))) {
                res.setStatus(-1);
                res.setMessage("权限不足，修改商品失败");
                return res;
            } else {
                food.setLastUpdTime(new Date());
                food.setStatus(-1);
                if (foodService.updateById(food)) {
                    res.setStatus(200);
                    res.setMessage("删除商品成功");
                    return res;
                } else {
                    res.setStatus(-1);
                    res.setMessage("删除商品失败");
                    return res;
                }
            }
        }
    }

    @ApiOperation("店铺管理员获取店铺所有评论")
    @RequestMapping(value = "/getComments", method = RequestMethod.GET)
    public Result getComments(@RequestBody HashMap<String, String> commentForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        Integer restaurantId = Integer.valueOf(commentForm.get("restaurantId"));;
        Integer page = Integer.valueOf(commentForm.get("page"));
        Integer pageSize = Integer.valueOf(commentForm.get("pageSize"));;
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        }
        if ((admin.getAdminType() == 2 && restaurantId == null) ) {
            res.setStatus(-1);
            res.setMessage("您未选择相应的店铺");
            return res;
        } else {
            if ((Integer.valueOf(admin.getAdminType()) != 2)) {
                restaurantId = admin.getRestaurantId();
            }
            if (restaurantService.selectById(restaurantId) == null) {
                res.setStatus(-1);
                res.setMessage("该店铺不存在");
                return res;
            }
            if(commentForm.get("page") == null) {
                page = 1;
            }
            if (commentForm.get("pageSize") == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> pager = commentService.getComments(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取评论成功");
            res.setData(pager);
            return res;
        }
    }

    @ApiOperation("店铺管理员回复评论")
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public Result reply(@RequestBody HashMap<String, String> replyForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Integer commentId = Integer.valueOf(replyForm.get("commentId"));
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (commentId == null) {
            res.setStatus(-1);
            res.setMessage("未选中要回复的评论");
            return res;
        } else {
            Comment co = commentService.selectById(commentId);
            if (co == null) {
                res.setStatus(-1);
                res.setMessage("该评论不存在");
                return res;
            }
            if(admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(co.getCommentId()))) {
                res.setStatus(-1);
                res.setMessage("您无法回复该条评论");
                return res;
            }
            if (replyForm.get("reply") == null) {
                res.setStatus(-1);
                res.setMessage("评论失败");
                return res;
            }
            co.setReply(replyForm.get("reply"));
            co.setReplyTime(new Date());
            co.setHasReply(1);
            if (commentService.updateById(co)) {
                res.setStatus(200);
                res.setMessage("回复评论成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("回复评论失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺删除评论")
    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    public Result deleteComment(@RequestBody HashMap<String, String> replyForm,
                        HttpServletRequest request) {
        Result res = new Result();
        Integer commentId = Integer.valueOf(replyForm.get("commentId"));
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (commentId == null) {
            res.setStatus(-1);
            res.setMessage("未选中要删除的评论");
            return res;
        } else {
            Comment co = commentService.selectById(commentId);
            if (co == null) {
                res.setStatus(-1);
                res.setMessage("该评论不存在");
                return res;
            }
            if(admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(co.getCommentId()))) {
                res.setStatus(-1);
                res.setMessage("您无法删除该条评论");
                return res;
            }
            if (commentService.deleteById(co)) {
                res.setStatus(200);
                res.setMessage("删除评论成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("删除评论失败");
                return res;
            }
        }
    }

}

