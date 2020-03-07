package com.delivery.main.common.persistence.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.service.*;
import com.delivery.main.common.persistence.template.modal.*;
import io.swagger.annotations.*;
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
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiImplicitParam(name = "loginForm", value = "{adminName:admin,password:admin}")
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
    @ApiImplicitParam(name = "registerForm", value = "{adminName:admin}")
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
    @ApiImplicitParam(name = "registerForm", value = "{adminName:admin,password:admin,avatar:xxxx,city:xxx}")
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

    @ApiOperation("管理员获取分类列表")
    @RequestMapping(value = "/getCategories/{restaurantId}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "page", value = "{page:1,pageSize:5}")
    public Result getComments(@PathVariable Integer restaurantId,
                              @RequestBody HashMap<String, Object> pager,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 2 && restaurantId == null ) {
            res.setStatus(-1);
            res.setMessage("请选择商店");
            return res;
        } else if (admin.getAdminType() == 1 && (!restaurantId.equals(admin.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("您无权查看分类");
            return res;
        } else {
            Integer page = (Integer) pager.get("page");
            Integer pageSize = (Integer) pager.get("pageSize");
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> p = categoryService.getCategoryList(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取分类成功");
            res.setData(p);
            return res;
        }
    }

    @ApiOperation("店铺管理员添加分类")
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ApiImplicitParam(name = "category", value = "{restaurantId:1,name:主食,icon:xxxx}")
    public Result addFood(@RequestBody Category category,
                          HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 2 && category.getRestaurantId() == null ) {
            res.setStatus(-1);
            res.setMessage("请选择商店");
            return res;
        } else if (admin.getAdminType() == 1 && (!category.getRestaurantId().equals(admin.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("您无权添加分类");
            return res;
        }else {
            Restaurant restaurant = restaurantService.selectById(admin.getAdminType() == 1 ? admin.getRestaurantId() : category.getRestaurantId());
            if (restaurant == null) {
                res.setStatus(-1);
                res.setMessage("该商店不存在");
                return res;
            }
            category.setCreateTime(new Date());
            category.setStatus(1);
            if (categoryService.insert(category)) {
                res.setStatus(200);
                res.setMessage("添加分类成功");
                res.setData(category);
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("添加分类失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员修改分类")
    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    @ApiImplicitParam(name = "category", value = "{categoryId:1, restaurantId:1,name:主食,icon:xxxx,status:1}")
    public Result updateCategory(@RequestBody Category category,
                             HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(category.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("权限不足，修改商品失败");
            return res;
        } else {
            if (categoryService.updateById(category)) {
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

    @ApiOperation("店铺管理员删除分类")
    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
    @ApiImplicitParam(name = "category", value = "{categoryId:1,status:-1}")
    public Result deleteCategory(@RequestBody HashMap<String, String> categoryForm,
                             HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            Category category = categoryService.selectById(Integer.valueOf(categoryForm.get("categoryId")));
            if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(category.getRestaurantId()))) {
                res.setStatus(-1);
                res.setMessage("权限不足，删除分类失败");
                return res;
            } else {
                category.setStatus(-1);
                if (categoryService.updateById(category)) {
                    res.setStatus(200);
                    res.setMessage("删除分类成功");
                    return res;
                } else {
                    res.setStatus(-1);
                    res.setMessage("删除分类失败");
                    return res;
                }
            }
        }
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

    @ApiOperation("管理员获取商品列表")
    @RequestMapping(value = "/getFoods/{restaurantId}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "page", value = "{page:1,pageSize:5}")
    public Result getFoodList(@PathVariable Integer restaurantId,
                              @RequestBody HashMap<String, Object> pager,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 2 && restaurantId == null ) {
            res.setStatus(-1);
            res.setMessage("请选择商店");
            return res;
        } else if (admin.getAdminType() == 1 && (!restaurantId.equals(admin.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("您无权查看商品");
            return res;
        } else {
            Integer page = (Integer) pager.get("page");
            Integer pageSize = (Integer) pager.get("pageSize");
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> p = foodService.getFoodList(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取分类成功");
            res.setData(p);
            return res;
        }
    }

    @ApiOperation("店铺管理员添加商品")
    @RequestMapping(value = "/addFood", method = RequestMethod.POST)
    @ApiImplicitParam(name = "food", value = "{restaurantId:1, categoryId:1, name:主食," +
                                    "price:1, picture:xxxx, description:xxx, tag:xxx, promotionInfo:xxx }")
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
    @ApiImplicitParam(name = "food", value = "{restaurantId:1, categoryId:1, name:主食, price:1, " +
                                    "picture:xxxx, description:xxx, tag:xxx, promotionInfo:xxx,status:1 }")
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
    @ApiImplicitParam(name = "food", value = "{restaurantId:1, foodId:1 }")
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
    @ApiImplicitParam(name = "commentForm", value = "{restaurantId:1, page:1, pageSize:1 }")
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
    @ApiImplicitParam(name = "commentForm", value = "{restaurantId:1, page:1, pageSize:1 }")
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
    @ApiImplicitParam(name = "replyForm", value = "{commentId:1}")
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

    @ApiOperation("管理员获取所有用户列表")
    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    @ApiImplicitParam(name = "page", value = "{page:1, pageSize:5 }")
    public Result getUserList(@RequestBody HashMap<String, String> userForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        Integer page = Integer.valueOf(userForm.get("page"));
        Integer pageSize = Integer.valueOf(userForm.get("pageSize"));;
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        }
        if (admin.getAdminType() == 1 ) {
            res.setStatus(-1);
            res.setMessage("您的权限不足");
            return res;
        } else {
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> pager = userService.getUserList(new Page<>(page,pageSize));
            res.setStatus(200);
            res.setMessage("获取用户成功");
            res.setData(pager);
            return res;
        }
    }

    @ApiOperation("店铺管理员修改用户")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiImplicitParam(name = "User", value = "{userId:1, phone:1, currentAddressId:1 }")
    public Result updateFood(@RequestBody User user,
                             HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1) {
            res.setStatus(-1);
            res.setMessage("权限不足，修改用户失败");
            return res;
        } else {
            if (userService.updateById(user)) {
                res.setStatus(200);
                res.setMessage("修改用户成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("修改用户失败");
                return res;
            }
        }
    }

    @ApiOperation("管理员获取店铺列表")
    @RequestMapping(value = "/getRestaurants", method = RequestMethod.GET)
    @ApiImplicitParam(name = "page", value = "{page:1, pageSize:1 }")
    public Result getRestaurants(@RequestBody HashMap<String, Object> pager,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1) {
            res.setStatus(-1);
            res.setMessage("您的权限不足");
            return res;
        } else {
            Integer page = (Integer) pager.get("page");
            Integer pageSize = (Integer) pager.get("pageSize");
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> p = restaurantService.getRestaurantList(new Page<>(page,pageSize));
            res.setStatus(200);
            res.setMessage("获取商店列表成功");
            res.setData(p);
            return res;
        }
    }

    @ApiOperation("店铺管理员添加商店")
    @RequestMapping(value = "/addRestaurant", method = RequestMethod.POST)
    @ApiImplicitParam(name = "restaurant", value = "{restaurantId:1, name:1, picture:1, deliveryTime:10," +
                                         " deliveryFee:5, minPriceTip:10, businessTIme: , businessTimeStart:" +
                                         " businessTImeEnd: ,notice: ,background: , address: , phone: ," +
                                         " lng: , lat: }")
    public Result addFood(@RequestBody Restaurant restaurant,
                          HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && !admin.getRestaurantId().equals(null) ) {
            res.setStatus(-1);
            res.setMessage("您已注册过商铺");
            return res;
        } else {
            restaurant.setUserId(admin.getAdminId());
            restaurant.setOrderScore(new BigDecimal(0));
            restaurant.setDeliveryScore(new BigDecimal(0));
            restaurant.setPackageScore(new BigDecimal(0));
            restaurant.setCommentNumber(0);
            restaurant.setCreateTime(new Date());
            restaurant.setLastUpdTime(new Date());
            restaurant.setStatus(1);
            if (restaurantService.insert(restaurant)) {
                res.setStatus(200);
                res.setMessage("添加商铺成功");
                res.setData(restaurant);
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("添加商铺失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员修改商铺")
    @RequestMapping(value = "/updateRestaurant", method = RequestMethod.POST)
    @ApiImplicitParam(name = "restaurant", value = "{restaurantId:1, name:1, picture:1, deliveryTime:10," +
            " deliveryFee:5, minPriceTip:10, businessTIme: , businessTimeStart:" +
            " businessTImeEnd: ,notice: ,background: , address: , phone: ," +
            " lng: , lat: }")
    public Result updateCategory(@RequestBody Restaurant restaurant,
                                 HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(restaurant.getRestaurantId()))) {
            res.setStatus(-1);
            res.setMessage("权限不足，修改店铺失败");
            return res;
        } else {
            restaurant.setLastUpdTime(new Date());
            if (restaurantService.updateById(restaurant)) {
                res.setStatus(200);
                res.setMessage("修改店铺成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("修改店铺失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员删除店铺")
    @RequestMapping(value = "/deleteRestaurant", method = RequestMethod.POST)
    @ApiImplicitParam(name = "restaurant", value = "{restaurantId:1}")
    public Result deleteRestaurant(@RequestBody HashMap<String, String> restaurantForm,
                                 HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            Restaurant restaurant = restaurantService.selectById(Integer.valueOf(restaurantForm.get("restaurantId")));
            if (admin.getAdminType() == 1 && (!admin.getRestaurantId().equals(restaurant.getRestaurantId()))) {
                res.setStatus(-1);
                res.setMessage("权限不足，删除店铺失败");
                return res;
            } else {
                restaurant.setStatus(-1);
                if (restaurantService.updateById(restaurant)) {
                    res.setStatus(200);
                    res.setMessage("删除店铺成功");
                    return res;
                } else {
                    res.setStatus(-1);
                    res.setMessage("删除店铺失败");
                    return res;
                }
            }
        }
    }
}

