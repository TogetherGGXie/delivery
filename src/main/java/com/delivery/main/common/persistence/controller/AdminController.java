package com.delivery.main.common.persistence.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.service.*;
import com.delivery.main.common.persistence.template.modal.*;
import com.delivery.main.util.Result;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
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
    private FoodorderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

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
            HashMap<String, Object> data = new HashMap<>();
            data.put("restaurantId", admin.getRestaurantId());
            data.put("adminType", admin.getAdminType());
            data.put("adminName", admin.getAdminName());
            data.put("adminId", admin.getAdminId());
            res.setData(data);
            request.getSession().setAttribute("admin", admin);
        }
        return res;
    }

    @ApiOperation("管理员注销登录")
    @RequestMapping(value = "/signout")
    public Result adminLogin(HttpServletRequest request) {
        Result res = new Result();
        res.setStatus(200);
        request.getSession().invalidate();
        res.setMessage("注销成功");
        return res;
    }

    @ApiOperation("管理员获取个人信息")
    @RequestMapping(value = "/adminInfo")
    public Result adminInfo(HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        res.setStatus(200);
        res.setMessage("获取个人信息成功");
        res.setData(admin);
        return res;
    }

    @ApiOperation("管理员更新个人信息")
    @ApiImplicitParam(name = "adminForm", value = "{adminName:admin, password:admin, city: xxx, avatar:xxx}")
    @RequestMapping(value = "/updateInfo")
    public Result updateInfo(@RequestBody HashMap<String, Object> adminForm, HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        admin.setAvatar(adminForm.get("avatar").toString());
        admin.setAdminName(adminForm.get("adminName").toString());
        admin.setPassword(adminForm.get("password").toString());
        if (adminService.updateById(admin)) {
            res.setStatus(200);
            res.setMessage("获取个人信息成功");
            res.setData(admin);
            return res;
        } else {
            return new Result(-1, "修改个人信息失败");
        }

    }

    @ApiOperation("查询用户名是否可用")
    @ApiImplicitParam(name = "registerForm", value = "{adminName:admin}")
    @RequestMapping(value = "/checkAdminName")
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
    @RequestMapping(value = "/getCategories/{restaurantId}")
    @ApiImplicitParam(name = "page", value = "{page:1,pageSize:5}")
    public Result getCategories(@PathVariable Integer restaurantId,
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
    public Result addCategory(@RequestBody Category category,
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
    @RequestMapping(value = "/getComments")
    @ApiImplicitParam(name = "commentForm", value = "{restaurantId:1, page:1, pageSize:1 }")
    public Result getComments(@RequestBody HashMap<String, String> commentForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        Integer restaurantId = commentForm.get("restaurantId") == null ? null :Integer.valueOf(commentForm.get("restaurantId"));;
        Integer page = commentForm.get("page") == null ? null :Integer.valueOf(commentForm.get("page"));
        Integer pageSize = commentForm.get("pageSize") == null ? null :Integer.valueOf(commentForm.get("pageSize"));;
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
    @RequestMapping(value = "/getUsers")
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
    public Result updateUser(@RequestBody User user,
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

    @ApiOperation("店铺管理员删除用户")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ApiImplicitParam(name = "User", value = "{userId:1,status:-1}")
    public Result deleteUser(@RequestBody User user,
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
            user.setStatus(-1);
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
    @RequestMapping(value = "/getRestaurants")
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
    public Result addRestaurant(@RequestBody Restaurant restaurant,
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
                admin.setRestaurantId(restaurant.getRestaurantId());
                adminService.updateById(admin);
                request.getSession().setAttribute("admin", admin);
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
    public Result updateRestaurant(@RequestBody Restaurant restaurant,
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

    @ApiOperation("店铺管理员获取本店订单列表")
    @RequestMapping(value = "/getOrders")
    @ApiImplicitParam(name = "page", value = "{page:1, pageSize:1, restaurantId:非必须，超管可选}")
    public Result getMyOrders(@RequestBody HashMap<String, Object> pager,
                                 HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && admin.getRestaurantId() == null) {
           return new Result(-1, "您未绑定店铺！");
        } else {
            Integer page = (Integer) pager.get("page");
            Integer pageSize = (Integer) pager.get("pageSize");
            Integer restaurantId = admin.getAdminType() == 1 ? admin.getRestaurantId() : (Integer)pager.get("restaurantId");
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> p = orderService.getMyOrders(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取订单列表成功");
            res.setData(p);
            return res;
        }
    }


    @ApiOperation("店铺管理员获取本店订单列表")
    @RequestMapping(value = "/getAllOrders")
    @ApiImplicitParam(name = "page", value = "{page:1, pageSize:1, restaurantId:非必须，超管可选}")
    public Result getAllOrders(@RequestBody HashMap<String, Object> pager,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1) {
            return new Result(-1, "您的权限不足！");
        } else {
            Integer page = (Integer) pager.get("page");
            Integer pageSize = (Integer) pager.get("pageSize");
            Integer restaurantId = admin.getAdminType() == 1 ? admin.getRestaurantId() : (Integer)pager.get("restaurantId");
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> p = orderService.getAllOrders(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取订单列表成功");
            res.setData(p);
            return res;
        }
    }

    @ApiOperation("店铺管理员修改订单状态")
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @ApiImplicitParam(name = "order", value = "{orderId:1, status:1}")
    public Result updateOrder(@RequestBody Foodorder order,
                          HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1 && admin.getRestaurantId().equals(null) ) {
            res.setStatus(-1);
            res.setMessage("您未绑定店铺");
            return res;
        } else if (order.getOrderId() == null) {
            return new Result(-1, "您未选择订单");
        } else {
            Foodorder o = orderService.queryOne(order.getOrderId());
            if (o == null) {
                res.setStatus(-1);
                res.setMessage("订单不存在，修改失败");
                return res;
            } else {
                o.setStatus(order.getStatus());
                if (orderService.updateById(o)){
                    return new Result(200, "修改成功");
                } else {
                    return new Result(-1, "修改失败");
                }
            }
        }
    }


    @ApiOperation("管理员获取店铺管理员列表")
    @RequestMapping(value = "/getAdmins")
    @ApiImplicitParam(name = "page", value = "{page:1, pageSize:5 }")
    public Result getAdminList(@RequestBody HashMap<String, String> adminForm,
                              HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        Integer page = Integer.valueOf(adminForm.get("page"));
        Integer pageSize = Integer.valueOf(adminForm.get("pageSize"));;
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
            Page<HashMap<String, Object>> pager = adminService.getAdminList(new Page<>(page,pageSize));
            res.setStatus(200);
            res.setMessage("获取管理员成功");
            res.setData(pager);
            return res;
        }
    }

    @ApiOperation("店铺管理员修改店铺管理员")
    @RequestMapping(value = "/updateAdmin", method = RequestMethod.POST)
    @ApiImplicitParam(name = "Admin")
    public Result updateAdmin(@RequestBody Admin newAdmin,
                             HttpServletRequest request) {
        Result res = new Result();
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else if (admin.getAdminType() == 1) {
            res.setStatus(-1);
            res.setMessage("权限不足，修改管理员失败");
            return res;
        } else {
            if (adminService.updateById(newAdmin)) {
                res.setStatus(200);
                res.setMessage("修改管理员成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("修改管理员失败");
                return res;
            }
        }
    }

    @ApiOperation("店铺管理员删除店铺管理员")
    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
    @ApiImplicitParam(name = "admin", value = "{adminId:1,status:-1}")
    public Result deleteUser(@RequestBody Admin deleteAdmin,
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
            deleteAdmin.setStatus(-1);
            if (adminService.updateById(deleteAdmin)) {
                res.setStatus(200);
                res.setMessage("删除管理员成功");
                return res;
            } else {
                res.setStatus(-1);
                res.setMessage("删除管理员失败");
                return res;
            }
        }
    }

    @RequestMapping(value = "/getToken")
    @ApiOperation("获取上传token")
    public Result getToken(HttpServletRequest request) {
        Admin admin = (Admin)request.getSession().getAttribute("admin");
        if (admin == null) {
            return new Result(-1, "登录状态失效");
        }
        Auth auth = Auth.create(accessKey, secretKey);
        HashMap<String, String> object = new HashMap<>();
        object.put("token", auth.uploadToken(bucket));
        object.put("uuid", IdUtil.simpleUUID());

        return new Result(200, "获取token成功", object);
    }



}

