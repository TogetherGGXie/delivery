package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @ApiOperation("添加餐馆")
    @ResponseBody
    @RequestMapping(value = "addRestaurant" ,method = RequestMethod.POST)
    public Result addReataurant(@RequestBody Restaurant restaurant){
        boolean addRestaurant = restaurantService.insert(restaurant);
        if(addRestaurant){
            return new Result(200,"添加成功");
        }else {
            return  new Result(201,"添加失败");
        }
    }

    @ApiOperation("查询店铺信息")
    @RequestMapping(value = "order/{restaurantId}",method = RequestMethod.GET)
    @ResponseBody
    public Result getReataurant(@PathVariable Integer restaurantId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null ){
            return new Result(-1,"登录已过期");
        }else{
            Restaurant restaurant = restaurantService.selectById(restaurantId);
            return  new Result(200,"获得店铺信息成功",restaurant);
        }
    }

    @ApiOperation("获取多家餐馆")
    @RequestMapping(value = "restaurants",method = RequestMethod.GET)
    @ResponseBody
    public Result getRestaurants(@RequestParam(value = "lat" ,required = false) String lat,
                                 @RequestParam(value = "lng" ,required = false) String lng,
                                 HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null ){
            return new Result(-1,"登录已过期");
        }else {
            List<Restaurant> restaurants = restaurantService.queryList(lng,lat);
            return new Result(200, "获得店铺信息成功", restaurants);
        }
    }

    @ApiOperation("根据关键字搜索餐馆")
    @RequestMapping(value = "search/restaurant" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getRestaurantByKey(@RequestParam(value = "keyword",required = false) String keyWord,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null ){
            return new Result(-1,"登录已过期");
        }else {
            List<Restaurant> restaurant = restaurantService.selectList(new EntityWrapper<Restaurant>().like("name", keyWord));
            return new Result(200, "获得店铺信息成功", restaurant);
        }
    }
}

