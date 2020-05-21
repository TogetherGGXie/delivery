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
@TableName("horseman")
public class Horseman implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送员id
     */
    @TableId(value = "horseman_id", type = IdType.AUTO)
    private Integer horsemanId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_at")
    private Date createAt;


    public Integer getHorsemanId() {
        return horsemanId;
    }

    public void setHorsemanId(Integer horsemanId) {
        this.horsemanId = horsemanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Horseman{" +
        ", horsemanId=" + horsemanId +
        ", name=" + name +
        ", phone=" + phone +
        ", avatar=" + avatar +
        ", status=" + status +
        ", createAt=" + createAt +
        "}";
    }
}
