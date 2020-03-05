package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class RestaurantController {
    private RestaurantService restaurantService;

    @ApiOperation("添加餐馆")
    @ResponseBody
    @RequestMapping(value = "v1/addRestaurant" ,method = RequestMethod.POST)
    public Result addReataurant(@RequestBody Restaurant restaurant){
        boolean addRestaurant = restaurantService.insert(restaurant);
        if(addRestaurant){
            return new Result(200,"添加成功");
        }else {
            return  new Result(201,"添加失败");
        }
    }

    @ApiOperation("查询店铺信息")
    @RequestMapping(value = "/v1/order/{restaurantId}")
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
    @RequestMapping(value = "v1/restaurants",method = RequestMethod.GET)
    @ResponseBody
    public Result getRestaurants(@RequestBody HashMap<String,String> restaurantInfo,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null ){
            return new Result(-1,"登录已过期");
        }else {
            String lng = restaurantInfo.get("lng");
            String lat = restaurantInfo.get("lat");
            List<Restaurant> restaurants = restaurantService.selectList(new EntityWrapper<Restaurant>().eq("lng", lng).eq("lat", lat));
            return new Result(200, "获得店铺信息成功", restaurants);
        }
    }

    @ApiOperation("根据关键字搜索餐馆")
    @RequestMapping(value = "/v1/search/restaurant" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getRestaurantByKey(String keyWord,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null ){
            return new Result(-1,"登录已过期");
        }else {
            List<Restaurant> restaurant = restaurantService.selectList(new EntityWrapper<Restaurant>().like("name", keyWord));
            return new Result(200, "获得店铺信息成功", restaurant);
        }
    }
}

