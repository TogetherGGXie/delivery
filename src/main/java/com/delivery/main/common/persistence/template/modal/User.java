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
 * @since 2020-02-24
 */
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private Long phone;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户openId
     */
    @TableField("open_id")
    private String openId;
    /**
     * 性别 0：女 、1：男
     */
    private Integer gender;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 当前地址id
     */
    @TableField("current_address_id")
    private Integer currentAddressId;
    /**
     * 创建时间
     */
    @TableField("create__time")
    private Date createTime;
    /**
     * 账号状态0：禁用、 1：启用
     */
    private Integer status;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCurrentAddressId() {
        return currentAddressId;
    }

    public void setCurrentAddressId(Integer currentAddressId) {
        this.currentAddressId = currentAddressId;
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
        return "User{" +
        ", userId=" + userId +
        ", username=" + username +
        ", phone=" + phone +
        ", password=" + password +
        ", openId=" + openId +
        ", gender=" + gender +
        ", avatar=" + avatar +
        ", currentAddressId=" + currentAddressId +
        ", createTime=" + createTime +
        ", status=" + status +
        "}";
    }
}
