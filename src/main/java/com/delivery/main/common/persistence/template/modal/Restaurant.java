package com.delivery.main.common.persistence.template.modal;

import com.alibaba.fastjson.annotation.JSONField;
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
@TableName("restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 餐馆id
     */
    @TableId(value = "restaurant_id", type = IdType.AUTO)
    private Integer restaurantId;
    /**
     * 所属用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店家图片地址
     */
    private String picture;
    /**
     * 月售量
     */
    @TableField("month_sales")
    private Integer monthSales;
    /**
     * 月售量
     */
    @TableField("month_sales_tip")
    private String monthSalesTip;
    /**
     * 订单评分
     */
    @TableField("order_score")
    private BigDecimal orderScore;
    /**
     * 配送评分
     */
    @TableField("delivery_score")
    private BigDecimal deliveryScore;
    /**
     * 包装评分
     */
    @TableField("package_score")
    private BigDecimal packageScore;
    /**
     * 距离
     */
    private BigDecimal distance;
    /**
     * 配送时间
     */
    @TableField("delivery_time")
    private Integer deliveryTime;
    /**
     * 配送费
     */
    @TableField("delivery_fee")
    private BigDecimal deliveryFee;
    /**
     * 起送费
     */
    @TableField("min_price_tip")
    private BigDecimal minPriceTip;
    /**
     * 平均消费提示
     */
    @TableField("average_price_tip")
    private BigDecimal averagePriceTip;
    @TableField("third_category")
    private String thirdCategory;
    /**
     * 营业时间
     */
    @TableField("business_time")
    private String businessTime;
    /**
     * 开始营业时间
     */
    @TableField("business_time_start")
    private String businessTimeStart;
    /**
     * 结束营业时间
     */
    @TableField("business_time_end")
    private String businessTimeEnd;
    /**
     * 平均送达时间
     */
    @TableField("avg_delivery_time")
    private Integer avgDeliveryTime;
    /**
     * 公告
     */
    private String notice;
    /**
     * 背景图
     */
    private String background;
    /**
     * 评论数
     */
    @TableField("comment_number")
    private Integer commentNumber;
    /**
     * 地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 维度
     */
    private String lng;
    /**
     * 经度
     */
    private String lat;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最近修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_upd_time")
    private Date lastUpdTime;
    /**
     * 状态 -1删除 0禁用 1 启用
     */
    private Integer status;


    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Integer monthSales) {
        this.monthSales = monthSales;
    }

    public String getMonthSalesTip() {
        return monthSalesTip;
    }

    public void setMonthSalesTip(String monthSalesTip) {
        this.monthSalesTip = monthSalesTip;
    }

    public BigDecimal getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(BigDecimal orderScore) {
        this.orderScore = orderScore;
    }

    public BigDecimal getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(BigDecimal deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public BigDecimal getPackageScore() {
        return packageScore;
    }

    public void setPackageScore(BigDecimal packageScore) {
        this.packageScore = packageScore;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getMinPriceTip() {
        return minPriceTip;
    }

    public void setMinPriceTip(BigDecimal minPriceTip) {
        this.minPriceTip = minPriceTip;
    }

    public BigDecimal getAveragePriceTip() {
        return averagePriceTip;
    }

    public void setAveragePriceTip(BigDecimal averagePriceTip) {
        this.averagePriceTip = averagePriceTip;
    }

    public String getThirdCategory() {
        return thirdCategory;
    }

    public void setThirdCategory(String thirdCategory) {
        this.thirdCategory = thirdCategory;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getBusinessTimeStart() {
        return businessTimeStart;
    }

    public void setBusinessTimeStart(String businessTimeStart) {
        this.businessTimeStart = businessTimeStart;
    }

    public String getBusinessTimeEnd() {
        return businessTimeEnd;
    }

    public void setBusinessTimeEnd(String businessTimeEnd) {
        this.businessTimeEnd = businessTimeEnd;
    }

    public Integer getAvgDeliveryTime() {
        return avgDeliveryTime;
    }

    public void setAvgDeliveryTime(Integer avgDeliveryTime) {
        this.avgDeliveryTime = avgDeliveryTime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdTime() {
        return lastUpdTime;
    }

    public void setLastUpdTime(Date lastUpdTime) {
        this.lastUpdTime = lastUpdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
        ", restaurantId=" + restaurantId +
        ", userId=" + userId +
        ", name=" + name +
        ", picture=" + picture +
        ", monthSales=" + monthSales +
        ", monthSalesTip=" + monthSalesTip +
        ", orderScore=" + orderScore +
        ", deliveryScore=" + deliveryScore +
        ", packageScore=" + packageScore +
        ", distance=" + distance +
        ", deliveryTime=" + deliveryTime +
        ", deliveryFee=" + deliveryFee +
        ", minPriceTip=" + minPriceTip +
        ", averagePriceTip=" + averagePriceTip +
        ", thirdCategory=" + thirdCategory +
        ", businessTime=" + businessTime +
        ", businessTimeStart=" + businessTimeStart +
        ", businessTimeEnd=" + businessTimeEnd +
        ", avgDeliveryTime=" + avgDeliveryTime +
        ", notice=" + notice +
        ", background=" + background +
        ", commentNumber=" + commentNumber +
        ", address=" + address +
        ", phone=" + phone +
        ", lng=" + lng +
        ", lat=" + lat +
        ", createTime=" + createTime +
        ", lastUpdTime=" + lastUpdTime +
        ", status=" + status +
        "}";
    }
}
