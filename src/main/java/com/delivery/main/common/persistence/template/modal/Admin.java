package com.delivery.main.common.persistence.template.modal;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @since 2020-02-24
 */
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员Id
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;
    /**
     * 管理员名称
     */
    @TableField("admin_name")
    private String adminName;
    /**
     * 管理员密码
     */
    private String password;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
    /**
     * 管理员类型 1:普通管理、 2:超级管理员
     */
    @TableField("admin_type")
    private Integer adminType;
    /**
     * 账号状态 0禁用 1启用
     */
    private Integer status;
    /**
     * 管理员头像
     */
    private String avatar;
    /**
     * 管理员所在城市
     */
    private String city;
    /**
     * 普通管理员所属店铺
     */
    @TableField("restaurant_id")
    private Integer restaurantId;


    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAdminType() {
        return adminType;
    }

    public void setAdminType(Integer adminType) {
        this.adminType = adminType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", adminType=" + adminType +
                ", status=" + status +
                ", avatar='" + avatar + '\'' +
                ", city='" + city + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
