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
@TableName("food")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "food_id", type = IdType.AUTO)
    private Integer foodId;
    /**
     * 店家id
     */
    @TableField("restaurant_id")
    private Integer restaurantId;
    /**
     * 分类id
     */
    @TableField("category_id")
    private Integer categoryId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 评论数量
     */
    @TableField("comment_num")
    private Integer commentNum;
    /**
     * 评分
     */
    private BigDecimal score;
    /**
     * 图片
     */
    private String picture;
    /**
     * 描述
     */
    private String description;
    /**
     * 月售量
     */
    @TableField("month_sale")
    private Integer monthSale;
    /**
     * 标签
     */
    private String tag;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 促销情况
     */
    @TableField("promotion_info")
    private String promotionInfo;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最近编辑时间
     */
    @TableField("last_upd_time")
    private Date lastUpdTime;
    /**
     * 状态 -1删除 0禁用 1 启用
     */
    private Integer status;


    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMonthSale() {
        return monthSale;
    }

    public void setMonthSale(Integer monthSale) {
        this.monthSale = monthSale;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(String promotionInfo) {
        this.promotionInfo = promotionInfo;
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
        return "Food{" +
        ", foodId=" + foodId +
        ", restaurantId=" + restaurantId +
        ", categoryId=" + categoryId +
        ", name=" + name +
        ", commentNum=" + commentNum +
        ", score=" + score +
        ", picture=" + picture +
        ", description=" + description +
        ", monthSale=" + monthSale +
        ", tag=" + tag +
        ", price=" + price +
        ", promotionInfo=" + promotionInfo +
        ", createTime=" + createTime +
        ", lastUpdTime=" + lastUpdTime +
        ", status=" + status +
        "}";
    }
}
