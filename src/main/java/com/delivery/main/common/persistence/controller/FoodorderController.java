package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.CommentService;
import com.delivery.main.common.persistence.service.FoodorderService;
import com.delivery.main.common.persistence.service.PindanService;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.delivery.main.common.persistence.template.modal.*;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@RestController
public class FoodorderController {
    @Autowired
    private FoodorderService foodorderService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PindanService pindanService;

    @ApiOperation("查询订单")
    @RequestMapping(value = "v1/order/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryOrders(@PathVariable Integer orderId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录失败");
        } else {
            Integer userId = user.getUserId();
            Foodorder foodorder = foodorderService.selectOne(new EntityWrapper<Foodorder>().eq("order_id",orderId));

            if (foodorder == null) {
                return new Result(-1, "查询订单失败");
            } else {
                String pinLength = foodorder.getPinLength();
                String pinOrderId = foodorder.getPinOrderId();
                Integer restaurantId = foodorder.getRestaurantId();
                BigDecimal totalPrice = foodorder.getTotalPrice();
                List<Pindan> list = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinOrderId));
                Pindan pindan = pindanService.selectOne(new EntityWrapper<Pindan>().eq("pinId", pinOrderId).eq("other", 0));
                Restaurant restaurant = restaurantService.selectOne(new EntityWrapper<Restaurant>().eq("restaurant_id", restaurantId));
                String picture = restaurant.getPicture();
                String name = restaurant.getName();
                HashMap<String,Object> hashMap1 = new HashMap<>();
                hashMap1.put("id",restaurantId);
                hashMap1.put("pic_url",picture);
                hashMap1.put("name",name);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("groupPinList",list);
                hashMap.put("time",pindan.getTime());
                hashMap.put("pinId",pinOrderId);
                HashMap<String, Object> orderInfo = new  HashMap<>();
                orderInfo.put("id",orderId);
                orderInfo.put("total_price",totalPrice);
                orderInfo.put("create_time",foodorder.getCreateTime());
                orderInfo.put("pinLength",pinLength);
                orderInfo.put("pinGroup",hashMap);
                orderInfo.put("restaurantInfo",hashMap1);
                orderInfo.put("foodsList",foodorder.getFoodDetails());
                orderInfo.put("address",foodorder.getAddress());
                return new Result(200, "获取指定订单成功", orderInfo);
            }

        }

    }

    @ApiOperation("提交订单")
    @RequestMapping(value = "v1/wxorder", method = RequestMethod.POST)
    @ResponseBody
    public Result submitOrders(@RequestParam Integer restaurant_id,
                               @RequestParam String foods,
                               @RequestParam String address_id,
                               @RequestParam String pinId,
                               @RequestParam String pinLength,
                               @RequestParam BigDecimal totalPrice,
                               HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录失败");
        }else {
            Integer userId = user.getUserId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        Foodorder wxOrder = new Foodorder();
        wxOrder.setRestaurantId(restaurant_id);
        wxOrder.setFoodDetails(foods);
        wxOrder.setAddress(address_id);
        wxOrder.setTotalPrice(totalPrice);
        wxOrder.setPinLength(pinLength);
        wxOrder.setCreateTime(time);
        wxOrder.setStatus(2);
            wxOrder.setUserId(userId);
        if (pinId == null || " ".equals(pinId)) {
            wxOrder.setIsPindan(0);
        } else {
            wxOrder.setPinOrderId(pinId);
            wxOrder.setIsPindan(1);
        }
        Pindan pindan = new Pindan();
        pindan.setPinId(pinId);
        pindan.setStatus("1");
        pindanService.updateById(pindan);
        List<Pindan> pindanList = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinId));
        if (pindanList.size() == 6) {
            for (Pindan i : pindanList) {
                i.setGroupStatus("1");
            }
            pindanService.updateBatchById(pindanList);
        }
        boolean orderResult = foodorderService.insert(wxOrder);
        if (orderResult) {
            Integer orderId = wxOrder.getOrderId();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("order_id", orderId);
            hashMap.put("total_price", totalPrice);
            return new Result(200, "提交订单成功", hashMap);
        } else {
            return new Result(-1, "提交订单失败");
        }

//            if (orderResult) {
//                String foodDetails = orderDetail.getFoodDetails();
//                HashMap<String, String> hashMap = new HashMap<String, String>();
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = new JSONArray(foodDetails);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = jsonArray.getJSONObject(i);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        String totalPrice = jsonObject.getString("total_price");
//
//                        hashMap.put("total_price", totalPrice);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                return new Result(200,"提交订单成功",hashMap);
//
//            }else {
//                return new Result(-1,"提交订单失败");
//            }
//            List<Order> orders = orderService.selectList(new EntityWrapper<Order>().eq("user_id", order.getUserId()));
//            Result  result = new Result();
//            result.setData(orders);
//            return new Result(200, "添加成功",orders);

        }
    }

    @ApiOperation("获取用户所有的订单")
    @RequestMapping(value = "/v1/orders", method = RequestMethod.GET)
    @ResponseBody
    public Result getAllOrders(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录失败");
        } else {
            Integer userId = user.getUserId();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_id", userId);
            List<Foodorder> orders = foodorderService.selectByMap(hashMap);
            List<HashMap<String,Object>> list = new ArrayList<>();
            for (Foodorder i : orders){
                HashMap<String,Object> orderDetail = new HashMap<>();
                Integer restaurantId = i.getRestaurantId();
                Integer orderId = i.getOrderId();
                List<Comment> commentList = commentService.selectList(new EntityWrapper<Comment>().eq("order_id", orderId));

                Restaurant restaurant = restaurantService.selectOne(new EntityWrapper<Restaurant>().eq("restaurant_id", restaurantId));
                String picture = restaurant.getPicture();

                String name = restaurant.getName();
                HashMap<String,Object> hashMap1 = new HashMap<>();
                hashMap1.put("id",restaurantId);
                hashMap1.put("pic_url",picture);
                hashMap1.put("name",name);
                orderDetail.put("id",i.getOrderId());
                orderDetail.put("pinId",i.getPinOrderId());
                orderDetail.put("total_price",i.getTotalPrice());
                if(commentList.size() != 0){
                    orderDetail.put("has_comment",true);
                }else {
                    orderDetail.put("has_comment",false);
                }
                orderDetail.put("restairant",hashMap1);
                orderDetail.put("food",i.getFoodDetails());
                list.add(orderDetail);

            }
            return new Result(-1, "获取我的订单列表成功", orders);
        }
    }

}

