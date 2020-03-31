package com.delivery.main.common.persistence.controller;


import com.delivery.main.common.persistence.service.FoodorderService;
import com.delivery.main.common.persistence.template.modal.Foodorder;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
public class FoodorderController {
    @Autowired
    private FoodorderService foodorderService;
    @ApiOperation("查询订单")
    @RequestMapping(value = "v1/order/{orderId}", method= RequestMethod.GET)
    @ResponseBody
    public Result queryOrders(@PathVariable Integer orderId ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录失败");
        }else{
            Foodorder foodorder = foodorderService.queryOne(orderId);
            if(foodorder== null){
                return new Result(-1,"查询订单失败");
            }else{
                HashMap<String,Object> orderInfo = foodorderService.queryOrder(foodorder);
                return new Result(200,"获取指定订单成功",orderInfo);
            }

        }

    }

    @ApiOperation("提交订单")
    @RequestMapping(value = "v1/wxorder", method= RequestMethod.POST)
    @ResponseBody
    public Result submitOrders(@RequestParam Integer restaurant_id,
                               @RequestParam String foods,
                               @RequestParam String address_id,
                               @RequestParam String pinId,
                               @RequestParam BigDecimal totalPrice,
                               HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        if(user == null){
//            return new Result(-1,"用户登录失败");
//        }else {
//            Integer userId = user.getUserId();
        Foodorder wxOrder = new Foodorder();
            wxOrder.setRestaurantId(restaurant_id);
        wxOrder.setFoodDetails(foods);
            wxOrder.setAddress(address_id);
        wxOrder.setTotalPrice(totalPrice);
//            wxOrder.setUserId(userId);
        if(pinId == null || " ".equals(pinId)){
            wxOrder.setIsPindan(0);
        }else {
            wxOrder.setPinOrderId(pinId);
            wxOrder.setIsPindan(1);
        }

        boolean orderResult = foodorderService.insert(wxOrder);
        if(orderResult){
            Integer orderId = wxOrder.getOrderId();
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("order_id",orderId);
            hashMap.put("total_price",totalPrice);
            return new Result(200,"提交订单成功",hashMap);
        }else {
            return  new Result(-1,"提交订单失败");
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

//        }
    }

    @ApiOperation("获取用户所有的订单")
    @RequestMapping(value = "/v1/orders", method= RequestMethod.GET)
    @ResponseBody
    public Result getAllOrders(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录失败");
        }else{
            Integer userId = user.getUserId();
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("user_id",userId);
            List<Foodorder> orders = foodorderService.selectByMap(hashMap);
            return new Result(-1,"获取我的订单列表成功",orders);
        }
    }

}

