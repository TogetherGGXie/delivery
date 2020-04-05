package com.delivery.main.common.persistence.template.modal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;
    /**
     * 用户id
     */
    @TableField("user__id")
    private Integer userId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 商铺id
     */
    @TableField("restaurant_id")
    private Integer restaurantId;
    /**
     * 评论时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("comment_time")
    private Date commentTime;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Integer orderId;
    /**
     * 订单评分
     */
    @TableField("order_score")
    private Integer orderScore;
    /**
     * 配送评分
     */
    @TableField("delivery_score")
    private Integer deliveryScore;
    /**
     * 包装评分
     */
    @TableField("package_score")
    private Integer packageScore;
    /**
     * 图片
     */
    private String picture;
    /**
     * 商家回复
     */
    @TableField("reply")
    private String reply;
    /**
     * 商家回复时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("reply_time")
    private Date replyTime;
    /**
     * 是否已回复
     */
    @TableField("has_reply")
    private Integer hasReply;


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(Integer orderScore) {
        this.orderScore = orderScore;
    }

    public Integer getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Integer deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getHasReply() {
        return hasReply;
    }

    public void setHasReply(Integer hasReply) {
        this.hasReply = hasReply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Integer getPackageScore() {
        return packageScore;
    }

    public void setPackageScore(Integer packageScore) {
        this.packageScore = packageScore;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", restaurantId=" + restaurantId +
                ", commentTime=" + commentTime +
                ", comment='" + comment + '\'' +
                ", orderId=" + orderId +
                ", orderScore=" + orderScore +
                ", deliveryScore=" + deliveryScore +
                ", packageScore=" + packageScore +
                ", picture='" + picture + '\'' +
                ", reply='" + reply + '\'' +
                ", replyTime=" + replyTime +
                ", hasReply=" + hasReply +
                '}';
    }
}
