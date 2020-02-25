package com.delivery.main.common.persistence.template.modal;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@TableName("order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 店铺id
     */
    @TableField("restaurant_id")
    private Integer restaurantId;
    /**
     * 总费用
     */
    @TableField("total_price")
    private BigDecimal totalPrice;
    /**
     * 商品详情  json串        {
            foods_id: ,
            num: Number,
            price: Number,
            name: String,
            pic_url: String,
            total_price: String,
            spec: String
        }
     */
    @TableField("food_details")
    private String foodDetails;
    /**
     * 配送费
     */
    @TableField("delivery_fee")
    private BigDecimal deliveryFee;
    /**
     * 地址
     */
    private String address;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 订单状态 -1 删除 0 创建 1支付 2接单 3配送 4 完成
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 是否是拼单
     */
    @TableField("is_pindan")
    private Integer isPindan;
    /**
     * 拼单主id
     */
    @TableField("pin_order_id")
    private Integer pinOrderId;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFoodDetails() {
        return foodDetails;
    }

    public void setFoodDetails(String foodDetails) {
        this.foodDetails = foodDetails;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsPindan() {
        return isPindan;
    }

    public void setIsPindan(Integer isPindan) {
        this.isPindan = isPindan;
    }

    public Integer getPinOrderId() {
        return pinOrderId;
    }

    public void setPinOrderId(Integer pinOrderId) {
        this.pinOrderId = pinOrderId;
    }

    @Override
    public String toString() {
        return "Order{" +
        ", orderId=" + orderId +
        ", userId=" + userId +
        ", restaurantId=" + restaurantId +
        ", totalPrice=" + totalPrice +
        ", foodDetails=" + foodDetails +
        ", deliveryFee=" + deliveryFee +
        ", address=" + address +
        ", remarks=" + remarks +
        ", status=" + status +
        ", createTime=" + createTime +
        ", isPindan=" + isPindan +
        ", pinOrderId=" + pinOrderId +
        "}";
    }
}
