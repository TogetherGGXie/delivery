package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.service.CommentService;
import com.delivery.main.common.persistence.service.OrderService;
import com.delivery.main.common.persistence.service.RestaurantService;
import com.delivery.main.common.persistence.template.modal.Comment;
import com.delivery.main.common.persistence.template.modal.Order;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/comment")
@Api("评论管理器")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;


    @ApiOperation("获取店铺评论")
    @RequestMapping("/getComments/{restaurantId}")
    public Result getComments(@PathVariable Integer restaurantId,
                              @RequestParam(value = "pageNumber", required = false) Integer page,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                              HttpServletRequest request) {
        Result res = new Result();
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            if(page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            Page<HashMap<String, Object>> pager = commentService.getComments(new Page<>(page,pageSize), restaurantId);
            res.setStatus(200);
            res.setMessage("获取评论成功");
            res.setData(pager);
            return res;
        }
    }

    @ApiOperation("评论订单")
    @RequestMapping("/comment")
    public Result comment(@RequestParam(name = "restaurantId") Integer restaurantId,
                          @RequestParam(name = "orderId") Integer orderId,
                          @RequestParam(name = "comment") String  comment,
                          @RequestParam(name = "orderScore") Integer orderScore,
                          @RequestParam(name = "picture") String picture,
                              HttpServletRequest request) {
        Result res = new Result();
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            res.setStatus(-1);
            res.setMessage("登录状态失效");
            return res;
        } else {
            Comment c = commentService.selectOne(new EntityWrapper<Comment>().eq("order_id", orderId));
            Order ord = orderService.queryOne(orderId);
            System.out.println(ord.toString());
            Order o = orderService.queryOne(orderId);
            System.out.println(o.toString());
            if (o == null) {
                res.setStatus(-1);
                res.setMessage("该订单不存在");
                return res;
            }else if (c != null) {
                res.setStatus(-1);
                res.setMessage("您已评论过该订单");
                return res;
            } else if (!o.getUserId().equals(user.getUserId())) {
                System.out.println("o.userId" + o.getUserId());
                System.out.println("user.userId" + user.getUserId());
                res.setStatus(-1);
                res.setMessage("您无法评价该订单");
                return res;
            } else {
                Restaurant restaurant = restaurantService.selectById(restaurantId);
                Integer commentNum = restaurant.getCommentNumber();
                restaurant.setOrderScore(restaurant.getOrderScore().multiply(new BigDecimal(commentNum)).add(new BigDecimal(orderScore)).divide(new BigDecimal(commentNum + 1)));
                restaurant.setCommentNumber(commentNum + 1);
                Comment co = new Comment();
                co.setUserId(user.getUserId());
                co.setAvatar(user.getAvatar());
                co.setUserName(user.getUsername());
                co.setRestaurantId(restaurantId);
                co.setComment(comment);
                co.setCommentTime(new Date());
                co.setOrderId(orderId);
                co.setOrderScore(orderScore);
                co.setHasReply(0);
                co.setPicture(picture);
                if (restaurantService.updateById(restaurant) && commentService.insert(co)) {
                    res.setStatus(200);
                    res.setMessage("评论成功");
                    return res;
                } else {
                    res.setStatus(-1);
                    res.setMessage("评论失败");
                    return res;
                }
            }
        }
    }
}

