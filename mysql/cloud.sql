/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : cloud

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 09/05/2020 21:21:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `parent_id` int(0) NOT NULL COMMENT '父类别ID：id=0是根节点，一级类别',
  `user_id` int(0) NOT NULL COMMENT '创建的用户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
  `status` tinyint(1) NOT NULL COMMENT '类别状态：1-正常，2-废弃',
  `type` int(0) NOT NULL COMMENT '内容类型：0-文件加，1-音乐文件，2-文本类文件，3-视频文件，4-压缩包，5-图片文件',
  `sort_order` int(0) NOT NULL COMMENT '排序编号：同类展示顺序，数值相等则自然排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 0, 1, '文件夹', 1, 0, 0, '2020-04-17 19:43:17', '2020-04-17 19:43:19');
INSERT INTO `category` VALUES (2, 0, 1, '0547c3610efc396f22acb795d97edcad', 1, 5, 1, '2020-04-24 21:03:54', '2020-04-24 21:03:54');
INSERT INTO `category` VALUES (3, 0, 1, '00032', 1, 5, 1, '2020-04-25 17:38:51', '2020-04-25 17:38:51');
INSERT INTO `category` VALUES (4, 0, 1, '00049', 1, 5, 1, '2020-04-25 19:01:00', '2020-04-25 19:01:00');
INSERT INTO `category` VALUES (5, 0, 1, '00049', 1, 5, 1, '2020-04-25 21:07:14', '2020-04-25 21:07:14');
INSERT INTO `category` VALUES (6, 0, 1, '00049', 1, 5, 1, '2020-04-25 21:08:45', '2020-04-25 21:08:45');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` bigint(0) NOT NULL COMMENT '订单号',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `shipping_id` int(0) NOT NULL,
  `payment` decimal(20, 2) NOT NULL COMMENT '实际付款金额,单位是元,保留两位小数',
  `payment_type` int(0) NOT NULL COMMENT '支付类型,1-在线支付',
  `postage` int(0) NOT NULL COMMENT '运费,单位是元',
  `status` int(0) NOT NULL COMMENT '订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭',
  `payment_time` datetime(0) NOT NULL COMMENT '支付时间',
  `send_time` datetime(0) NOT NULL COMMENT '发货时间',
  `end_time` datetime(0) NOT NULL COMMENT '交易完成时间',
  `close_time` datetime(0) NOT NULL COMMENT '交易关闭时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_index`(`order_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_info
-- ----------------------------
DROP TABLE IF EXISTS `pay_info`;
CREATE TABLE `pay_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `order_no` bigint(0) NOT NULL COMMENT '订单号',
  `pay_platform` int(0) NOT NULL COMMENT '支付平台：1-支付宝，2-微信',
  `platform_number` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付宝支付流水号',
  `platform_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付宝支付状态',
  `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品详情',
  `price` decimal(20, 2) NOT NULL COMMENT '价格,单位-元保留两位小数',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '商品状态.1-在售 2-下架 3-删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (30, 'vip1', '提升5次上传', 10.00, 1, '2020-04-30 10:29:18', '2020-04-30 10:29:20');
INSERT INTO `product` VALUES (31, 'vip2', '提升10次上传', 15.00, 1, '2020-04-30 10:30:06', '2020-04-30 10:30:08');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '角色id\r\n',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色英文名',
  `name_zh` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色中文名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'admin', '管理员');
INSERT INTO `roles` VALUES (2, 'user', '用户');
INSERT INTO `roles` VALUES (3, 'vip1', 'vip1');
INSERT INTO `roles` VALUES (4, 'vip2', 'vip2');

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_user`;
CREATE TABLE `roles_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `roles_id` int(0) NOT NULL COMMENT '角色id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles_user
-- ----------------------------
INSERT INTO `roles_user` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `create_time` datetime(0) NOT NULL COMMENT '用户创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '用户最后一次更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'tom', '123@qq.com', '202cb962ac59075b964b07152d234b70', '2020-04-14 17:01:11', '2020-04-14 17:01:13');

SET FOREIGN_KEY_CHECKS = 1;
