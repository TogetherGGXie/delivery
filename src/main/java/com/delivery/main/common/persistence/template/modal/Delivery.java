package com.delivery.main.common.persistence.template.modal;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2020-05-11
 */
@TableName("delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送ID
     */
    @TableId(value = "delivery_id", type = IdType.AUTO)
    private Integer deliveryId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private Integer orderId;
    /**
     * 配送员ID
     */
    @TableField("horseman_id")
    private Integer horsemanId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 状态
     */
    private Integer status;


    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getHorsemanId() {
        return horsemanId;
    }

    public void setHorsemanId(Integer horsemanId) {
        this.horsemanId = horsemanId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Delivery{" +
        ", deliveryId=" + deliveryId +
        ", orderId=" + orderId +
        ", horsemanId=" + horsemanId +
        ", createTime=" + createTime +
        ", status=" + status +
        "}";
    }
}
