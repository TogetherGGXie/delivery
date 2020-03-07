package com.delivery.main.common.persistence.controller;


import com.delivery.main.common.persistence.service.OrderService;
import com.delivery.main.common.persistence.template.modal.Order;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
public class OrderController {
    @Autowired
    private OrderService orderService;
    @ApiOperation("查询订单")
    @RequestMapping(value = "v1/order/{orderId}", method= RequestMethod.GET)
    @ResponseBody
    public Result queryOrders(@PathVariable Integer orderId ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录失败");
        }else{
            Order order = orderService.queryOne(orderId);
            if(order== null){
                return new Result(-1,"查询订单失败");
            }else{
                HashMap<String,Object> orderInfo = orderService.queryOrder(order);
                return new Result(200,"获取指定订单成功",orderInfo);
            }

        }

    }

    @ApiOperation("提交订单")
    @RequestMapping(value = "v1/wxorder", method= RequestMethod.POST)
    @ResponseBody
    public Result submitOrders(@RequestParam Order orderDetail, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录失败");
        }else {
            boolean orderResult = orderService.insert(orderDetail);
            if (orderResult) {
                String foodDetails = orderDetail.getFoodDetails();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(foodDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        String totalPrice = jsonObject.getString("total_price");

                        hashMap.put("total_price", totalPrice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return new Result(200,"提交订单成功",hashMap);

            }else {
                return new Result(-1,"提交订单失败");
            }
//            List<Order> orders = orderService.selectList(new EntityWrapper<Order>().eq("user_id", order.getUserId()));
//            Result  result = new Result();
//            result.setData(orders);
//            return new Result(200, "添加成功",orders);

        }
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
            List<Order> orders = orderService.selectByMap(hashMap);
            return new Result(-1,"获取我的订单列表成功",orders);
        }
    }
}

