package com.delivery.main.common.persistence.template.modal;

import com.baomidou.mybatisplus.activerecord.Model;
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
 * @author wzj123
 * @since 2020-03-08
 */
@TableName("user")
public class User extends Model<User> {

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
    private String nickName;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;


    public Integer getUserId() {
        return userId;
    }

    public User setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getPhone() {
        return phone;
    }

    public User setPhone(Long phone) {
        this.phone = phone;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public User setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public Integer getGender() {
        return gender;
    }

    public User setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Integer getCurrentAddressId() {
        return currentAddressId;
    }

    public User setCurrentAddressId(Integer currentAddressId) {
        this.currentAddressId = currentAddressId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public User setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public User setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public User setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public User setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCity() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public User setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public User setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public User setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
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
                ", nickName=" + nickName +
                ", language=" + language +
                ", city=" + city +
                ", province=" + province +
                ", country=" + country +
                ", avatarUrl=" + avatarUrl +
                "}";
    }
}
