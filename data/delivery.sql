/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : delivery

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 27/02/2020 23:44:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `address_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `user_id` int(255) NOT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` int(1) NULL DEFAULT NULL COMMENT '性别 0：女士、1：先生',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维度',
  PRIMARY KEY (`address_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '管理员Id',
  `admin_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员名称',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `admin_type` int(1) NOT NULL DEFAULT 1 COMMENT '管理员类型 1:普通管理、 2:超级管理员',
  `status` int(1) NULL DEFAULT 1 COMMENT '账号状态 0禁用 1启用',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员头像',
  `city` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员所在城市',
  `restaurant_id` int(255) NULL DEFAULT NULL COMMENT '普通管理所属店铺',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '目录id',
  `restaurant_id` int(255) NULL DEFAULT NULL COMMENT '餐馆id',
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图表地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 -1删除 0禁用 1启用',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `user__id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `restaurant_id` int(11) NULL DEFAULT NULL COMMENT '商铺id',
  `comment_time` datetime(0) NULL DEFAULT NULL COMMENT '评论时间',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `order_id` int(255) NULL DEFAULT NULL COMMENT '订单id',
  `order_score` int(5) NULL DEFAULT NULL COMMENT '订单评分',
  `delivery_score` int(255) NULL DEFAULT NULL COMMENT '配送评分',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`  (
  `food_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `restaurant_id` int(255) NULL DEFAULT NULL COMMENT '店家id',
  `category_id` int(255) NULL DEFAULT NULL COMMENT '分类id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `comment_num` int(255) NULL DEFAULT NULL COMMENT '评论数量',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '评分',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `month_sale` int(255) NULL DEFAULT NULL COMMENT '月售量',
  `tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `promotion_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '促销情况',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_upd_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最近编辑时间',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 -1删除 0禁用 1 启用',
  PRIMARY KEY (`food_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `order_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` int(255) NULL DEFAULT NULL COMMENT '用户id',
  `restaurant_id` int(255) NULL DEFAULT NULL COMMENT '店铺id',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总费用',
  `food_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品详情  json串        {\r\n            foods_id: ,\r\n            num: Number,\r\n            price: Number,\r\n            name: String,\r\n            pic_url: String,\r\n            total_price: String,\r\n            spec: String\r\n        }',
  `delivery_fee` decimal(5, 2) NULL DEFAULT NULL COMMENT '配送费',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(1) NULL DEFAULT NULL COMMENT '订单状态 -1 删除 0 创建 1支付 2接单 3配送 4 完成',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `is_pindan` int(1) NULL DEFAULT NULL COMMENT '是否是拼单',
  `pin_order_id` int(255) NULL DEFAULT NULL COMMENT '拼单主id',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant`  (
  `restaurant_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '餐馆id',
  `user_id` int(255) NULL DEFAULT NULL COMMENT '所属用户id',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店家图片地址',
  `month_sales` int(255) NOT NULL DEFAULT 0 COMMENT '月售量',
  `month_sales_tip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '月售量',
  `restaurant_score` decimal(3, 1) NULL DEFAULT NULL COMMENT '店铺评分',
  `delivery_score` decimal(3, 1) NULL DEFAULT NULL COMMENT '配送评分',
  `quality_score` decimal(3, 1) NULL DEFAULT NULL COMMENT '质量评分',
  `distance` decimal(10, 2) NULL DEFAULT NULL COMMENT '距离',
  `delivery_time` int(10) NULL DEFAULT NULL COMMENT '配送时间',
  `delivery_fee` decimal(3, 1) NULL DEFAULT NULL COMMENT '配送费',
  `min_price_tip` decimal(4, 2) NULL DEFAULT NULL COMMENT '起送费',
  `average_price_tip` decimal(4, 2) NULL DEFAULT NULL COMMENT '平均消费提示',
  `third_category` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_time` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业时间',
  `business_time_start` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始营业时间',
  `business_time_end` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束营业时间',
  `avg_delivery_time` int(10) NULL DEFAULT NULL COMMENT '平均送达时间',
  `notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `background` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景图',
  `comment_number` int(255) NULL DEFAULT NULL COMMENT '评论数',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_upd_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最近修改时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 -1删除 0禁用 1 启用',
  PRIMARY KEY (`restaurant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(40) NOT NULL AUTO_INCREMENT COMMENT '用户Id',
  `username` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phone` bigint(255) NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户openId',
  `gender` int(1) NULL DEFAULT NULL COMMENT '性别 0：女 、1：男',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `current_address_id` int(255) NULL DEFAULT NULL COMMENT '当前地址id',
  `create__time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '账号状态0：禁用、 1：启用',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
