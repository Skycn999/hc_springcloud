/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : oxchains_tx_dev

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2018-11-21 19:11:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tx_account`
-- ----------------------------
DROP TABLE IF EXISTS `tx_account`;
CREATE TABLE `tx_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` varchar(50) NOT NULL DEFAULT '' COMMENT 'uuid',
  `user_id` bigint(20) DEFAULT '0' COMMENT '用户ID',
  `coin_code` varchar(10) DEFAULT '' COMMENT '资产代码',
  `exchange_no` varchar(50) DEFAULT '' COMMENT '交易所编码',
  `exchange_name` varchar(100) DEFAULT '' COMMENT '交易名称',
  `mother_account_id` int(11) DEFAULT '0' COMMENT '母账号ID',
  `mother_account` varchar(100) DEFAULT '' COMMENT '母账号',
  `total` decimal(20,10) unsigned NOT NULL DEFAULT '0.0000000000' COMMENT '总数量',
  `available` decimal(20,10) unsigned NOT NULL DEFAULT '0.0000000000' COMMENT '可用数量',
  `freeze` decimal(20,10) unsigned NOT NULL DEFAULT '0.0000000000' COMMENT '冻结数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(50) DEFAULT '' COMMENT '创建人',
  `updator` varchar(50) DEFAULT '' COMMENT '更新人',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记 0,否:no;1,是:yes',
  PRIMARY KEY (`id`),
  KEY `del_flag` (`del_flag`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='用户资产持仓表';

-- ----------------------------
-- Records of tx_account
-- ----------------------------
INSERT INTO `tx_account` VALUES ('1', 'b03d72d38620436bbce730fd2db4083d', '1', 'BTC', null, null, null, null, '84.0440000000', '84.0440000000', '0.0000000000', '2018-06-02 12:39:33', '2018-06-06 11:04:50', null, null, '0');
INSERT INTO `tx_account` VALUES ('14', 'c427b35d3f924321abc7493b58a05e2c', '3', 'BTC', null, null, null, null, '10.0000000000', '10.0000000000', '0.0000000000', '2018-06-05 21:31:51', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('15', 'd69f3fa2c02e4ce087b0f45e8ca760ec', '3', 'ETH', null, null, null, null, '10.0000000000', '10.0000000000', '0.0000000000', '2018-06-05 21:31:51', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('49', 'f7e7a205f3544b6d80188e6a698c0581', '5', 'BTC', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-11 21:41:48', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('50', 'f380570da16e4ebbb0e78367ffec5b18', '5', 'ETH', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-11 21:41:48', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('51', 'e38ac969888942d6b907d69a66a24623', '6', 'BTC', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-13 17:04:21', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('52', 'cc729d3ac2924b7e8c95054c88fc2cf1', '6', 'ETH', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-13 17:04:21', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('53', '6a0b6171b66c4b2a850f4db37ccd6be9', '9', 'BTC', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-13 22:04:14', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('54', '8aef8ce372b448319d2deee375c067a9', '9', 'ETH', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-13 22:04:14', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('55', '5f45260dbf0a475e88166359590380a1', '1', 'BTC', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-15 11:03:40', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('56', '66dc724aabb2443396ff8c17c1ccf1d0', '1', 'ETH', null, null, null, null, '0.0000000000', '0.0000000000', '0.0000000000', '2018-06-15 11:03:40', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('57', 'b09f638af0024bdca87d54e973a4e6ea', '2', 'BTC', null, null, null, null, '10.0000000000', '5.9000000000', '4.1000000000', '2018-06-15 11:08:15', '2018-06-15 22:28:26', null, null, '0');
INSERT INTO `tx_account` VALUES ('58', 'dbdb464ff1564472b4d0050d3d17c59b', '2', 'ETH', null, null, null, null, '9.8934448780', '6.8933824000', '3.0000624780', '2018-06-15 11:08:15', '2018-06-15 22:27:48', null, null, '0');
INSERT INTO `tx_account` VALUES ('59', '', '2', 'HT', 'huobi', '火币', '0', '火币母账号', '11.9760000000', '11.9760000000', '0.0000000000', '2018-06-15 21:53:15', null, '', '', '0');
INSERT INTO `tx_account` VALUES ('60', 'fe774ccc7f404b7fb3303b4bb587fcea', '4', 'BTC', null, null, null, null, '20.0000000000', '20.0000000000', '0.0000000000', '2018-06-16 16:35:19', null, null, null, '0');
INSERT INTO `tx_account` VALUES ('61', 'fe839a0307094a448d6607f74e4e5d76', '4', 'ETH', null, null, null, null, '19.9091917700', '19.9091917700', '0.0000000000', '2018-06-16 16:35:19', '2018-06-16 19:23:35', null, null, '0');
INSERT INTO `tx_account` VALUES ('62', '', '4', 'HT', 'huobi', '火币', '0', '火币母账号', '7.9840000000', '7.9840000000', '0.0000000000', '2018-06-16 19:19:35', null, '', '', '0');
INSERT INTO `tx_account` VALUES ('63', '', '4', 'EOS', 'huobi', '火币', '0', '火币母账号', '0.9990000000', '0.9990000000', '0.0000000000', '2018-06-16 19:22:09', null, '', '', '0');

-- ----------------------------
-- Table structure for `tx_account_log`
-- ----------------------------
DROP TABLE IF EXISTS `tx_account_log`;
CREATE TABLE `tx_account_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` varchar(32) NOT NULL DEFAULT '' COMMENT 'uuid',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `opt_coin_code` varchar(50) DEFAULT NULL COMMENT '操作的资产代码',
  `opt_amount` decimal(20,10) DEFAULT '0.0000000000' COMMENT '操作数量',
  `before_opt_amount` decimal(20,10) DEFAULT '0.0000000000' COMMENT '操作前总数量',
  `after_opt_amount` decimal(20,10) DEFAULT '0.0000000000' COMMENT '操作后总数量',
  `freeze_amount` decimal(20,10) DEFAULT '0.0000000000' COMMENT '冻结数量',
  `approach` tinyint(4) DEFAULT NULL COMMENT '途径 1,充币:recharge;2,提币:mention;3,扣除:deduct;4,交易冻结:freeze;5,手续费:serviceFee;6,解冻:unfreeze;7,提币冻结:mention_freeze;8,提币解冻:mention_unfreeze;9,交易获取:tx_get',
  `info` varchar(500) DEFAULT NULL COMMENT '变更描述 如使用1个BTC换取100个EOS',
  `create_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updator` varchar(50) DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记 0,否:no;1,是:yes',
  PRIMARY KEY (`id`),
  KEY `del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 COMMENT='资产变更记录表';

-- ----------------------------
-- Records of tx_account_log
-- ----------------------------
INSERT INTO `tx_account_log` VALUES ('1', '9ab8a223ca5b4bb2aa8fefa5aa2bf639', '2', 'USDT', '650.2000000000', '7747.9142503590', '7097.7142503590', '650.2000000000', '4', '使用[USDT]限价买入,以获得[BTC]. 买入数量:0.1, 买入价格:6502,消耗:650.2个[USDT]', '2018-06-14 20:15:09', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('2', 'f3cd8517bc1247349640cd6a946d9588', '2', 'USDT', '1300.2820000000', '7747.9142503590', '6447.6322503590', '1950.4820000000', '4', '使用[USDT]限价买入,以获得[BTC]. 买入数量:0.2, 买入价格:6501.41,消耗:1300.282个[USDT]', '2018-06-14 20:27:31', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('3', 'baf86f909825496e8929f1cf9406e845', '2', 'USDT', '1300.2820000000', '5797.4322503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]买入[BTC],买入数量:0.2000000000,消耗:1300.2820000000个USDT,申请买入失败,解冻用户资产.子委托uuid:b474d6a4d114484688d3765fff4e6819', '2018-06-14 20:27:34', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('4', '84d75494dab048ca9fbadfb0e2d209b9', '2', 'USDT', '1300.0000000000', '7747.9142503590', '6447.9142503590', '1950.2000000000', '4', '使用[USDT]限价买入,以获得[BTC]. 买入数量:0.2, 买入价格:6500,消耗:1300.0个[USDT]', '2018-06-14 20:28:38', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('5', 'b07f0843f0554383acab7bce790201f1', '2', 'USDT', '1300.0000000000', '5797.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]买入[BTC],买入数量:0.2000000000,消耗:1300.0000000000个USDT,申请买入失败,解冻用户资产.子委托uuid:ddf2907e24ab4c9696ff043d48344e5d', '2018-06-14 20:28:40', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('6', 'e2e3109336a5467f9e4cbec4e72d22d0', '2', 'BTC', '1.0000000000', '1492.5756647065', '1491.5756647065', '1.0000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:1, 卖出价格:6504.8352', '2018-06-14 20:29:30', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('7', 'edab7625d78d499eb57387582c92c41a', '2', 'BTC', '1.0000000000', '1491.0016657065', '1492.0016657065', '0.0000000000', '6', '交易货币对[BTC_USDT],卖出[BTC],卖出数量:1.0000000000,卖出失败[未获取到满足条件的母账号:[okex]], 解冻用户资产.主委托uuid:72ab78a442c342d7af899e1637dd0d78', '2018-06-14 20:29:48', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('8', 'e3a2ff39fca240b5a7e4851bfa9f34af', '2', 'BTC', '3.0000000000', '1492.5756647065', '1489.5756647065', '3.0000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:3, 卖出价格:6503.2208', '2018-06-14 20:31:01', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('9', '9918270a30cb40bea7f0c11d82c6a65a', '2', 'BTC', '3.0000000000', '1489.0016657065', '1492.0016657065', '0.0000000000', '6', '交易货币对[BTC_USDT],卖出[BTC],卖出数量:3.0000000000,卖出失败[未获取到满足条件的母账号:[okex]], 解冻用户资产.主委托uuid:98bdbc0dd40440b3962c106dbc976386', '2018-06-14 20:31:02', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('10', '20997b90b7f241a7994bd50fdf2cef05', '2', 'BTC', '2.0000000000', '1492.5756647065', '1490.5756647065', '2.0000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:2, 卖出价格:6504.2099', '2018-06-14 20:31:10', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('11', 'fdc9cd70a05b47e6a04a8944e787432e', '2', 'USDT', '2.0000000000', '1490.0016657065', '1492.0016657065', '0.0000000000', '6', '使用[BTC]限价卖出[USDT],卖出数量:2.0000000000,卖出失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009]解冻用户资产.子委托uuid:7a440904075843aeac27c8094a02a08b', '2018-06-14 20:31:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('12', '526f856e6266479da01747fa4bdbe4bd', '2', 'BTC', '5.0000000000', '1492.5756647065', '1487.5756647065', '5.0000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:5, 卖出价格:6504.5202', '2018-06-14 20:32:17', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('13', 'fafc6a5995c445629d8d4a633fcffae0', '2', 'USDT', '5.0000000000', '1487.0016657065', '1492.0016657065', '0.0000000000', '6', '使用[BTC]限价卖出[USDT],卖出数量:5.0000000000,卖出失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009]解冻用户资产.子委托uuid:53ecce7132d34e1585d64b270920eb14', '2018-06-14 20:32:18', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('14', '7307600c8c9c4495be02dececf9402dd', '2', 'USDT', '0.3000000000', '7747.9142503590', '7747.6142503590', '650.5000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:0.3个USDT', '2018-06-14 20:34:46', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('15', 'b384c0da5d144e8baedc8854d7be48c5', '2', 'USDT', '0.3000000000', '7097.4142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:0.3000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:3b01f668b7b5473ba8bd71898aaf0501', '2018-06-14 20:34:51', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('16', '94727aab23334beb9808d376796359b6', '2', 'USDT', '0.4000000000', '7747.9142503590', '7747.5142503590', '650.6000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:0.4个USDT', '2018-06-14 20:35:00', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('17', '0ce4b87adcb34d02b9ca4cda66cb48a7', '2', 'USDT', '0.4000000000', '7097.3142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:0.4000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:e21b7100d51146b08082dc8cd8d7556e', '2018-06-14 20:35:04', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('18', 'cbd23b8a69cd480a95f854fd61733995', '2', 'USDT', '1.0000000000', '7747.9142503590', '7746.9142503590', '651.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:1个USDT', '2018-06-14 20:36:46', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('19', '5a16ecde252347898f2252a5da383997', '2', 'USDT', '1.0000000000', '7096.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:1.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:5f7f8a58e75a4a479f62fc019989c001', '2018-06-14 20:36:50', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('20', '06a6b57c432642da8e34b9e780c883c6', '2', 'USDT', '2.0000000000', '7747.9142503590', '7745.9142503590', '652.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:2个USDT', '2018-06-14 20:36:55', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('21', 'd3aa76b0884b46a683df3e7a8928cfb8', '2', 'USDT', '2.0000000000', '7095.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:2.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:9b313ebd57a64f2bb105ca90022c8f13', '2018-06-14 20:36:57', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('22', '53d8ce9e40da4a9986262c30d4084bc5', '2', 'USDT', '3.0000000000', '7747.9142503590', '7744.9142503590', '653.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:3个USDT', '2018-06-14 20:37:02', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('23', '5b6d4dce08fc425192647c628f98dc4b', '2', 'USDT', '3.0000000000', '7094.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:3.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:65a01d1409824ad2b0525aaf4a280790', '2018-06-14 20:37:04', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('24', '166daaf28b7b4238a397a5e0be9b6c6f', '2', 'USDT', '4.0000000000', '7747.9142503590', '7743.9142503590', '654.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:4个USDT', '2018-06-14 20:37:06', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('25', '1e694989673646f48164597eceea011a', '2', 'USDT', '4.0000000000', '7093.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:4.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:68742843441e4fb19db9e2163cc2a199', '2018-06-14 20:37:08', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('26', 'c95ee6cbecf2458e9107c47f215ba4cf', '2', 'USDT', '1.0000000000', '7747.9142503590', '7746.9142503590', '651.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:1个USDT', '2018-06-14 20:37:48', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('27', '2166b8e2f4bf4f87b4ea20aa6f69b56b', '2', 'USDT', '1.0000000000', '7096.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:1.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:b37e03c4f41e4fa38e0a4ccd9eb242dc', '2018-06-14 20:37:50', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('28', '1ddd7d70dfa24c1791623f36630e0d0a', '2', 'USDT', '1.0000000000', '7747.9142503590', '7746.9142503590', '651.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:1个USDT', '2018-06-14 20:40:13', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('29', '58fd120303ab4f11a613bbc8f26d6de2', '2', 'USDT', '1.0000000000', '7096.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:1.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:b1c26fb13adb4563b6eda01f782ff22f', '2018-06-14 20:40:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('30', 'e2384d8842b6436caf2b1cdb35e06ce1', '2', 'USDT', '1.0000000000', '7747.9142503590', '7746.9142503590', '651.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:1个USDT', '2018-06-14 20:40:30', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('31', 'a48c6c455ad4482c89bd86941e0cc2df', '2', 'USDT', '1.0000000000', '7096.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:1.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:a5f0a8608dd34f768d56f5641057d984', '2018-06-14 20:40:32', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('32', '82c3963a835845ddbeac96771f0f79b9', '2', 'USDT', '2.0000000000', '7747.9142503590', '7745.9142503590', '652.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:2个USDT', '2018-06-14 20:40:35', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('33', 'fd50c7874a354110b55f3f23ec6153fc', '2', 'USDT', '2.0000000000', '7095.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:2.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:341c151596964e3e985f64b1bfd041e2', '2018-06-14 20:40:36', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('34', '117af9ab0ea8426992707394653fae5a', '2', 'USDT', '3.0000000000', '7747.9142503590', '7744.9142503590', '653.2000000000', '4', '使用[USDT]市价买入[BTC]. 交易额:3个USDT', '2018-06-14 20:40:38', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('35', '604f0bc97ec04a67a17fb95b1daa6b95', '2', 'USDT', '3.0000000000', '7094.7142503590', '7097.7142503590', '650.2000000000', '6', '使用[USDT]市价买入[BTC],交易额:3.0000000000. 买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:1006da9705184b6993fe9f965b0e2a4a', '2018-06-14 20:40:40', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('36', '87c13c5d81ee4ec7ab528910078b1628', '2', 'BTC', '3.0000000000', '1492.5756647065', '1489.5756647065', '3.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:3', '2018-06-14 20:40:51', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('37', '768dde5c84f24401b7e2cb506dd75c18', '2', 'BTC', '3.0000000000', '1489.0016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:3.0000000000,卖出失败[未获取到满足条件的母账号:[okex]], 解冻用户资产.主委托uuid:719109ad9d56422e9944e76ca4e05f69', '2018-06-14 20:40:57', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('38', 'a16bb55105c54d7a825598c2aab7100a', '2', 'BTC', '2.0000000000', '1492.5756647065', '1490.5756647065', '2.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:2', '2018-06-14 20:47:19', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('39', '12c6c21566d044caa2c6065d3948d8df', '2', 'BTC', '2.0000000000', '1490.0016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:2.0000000000,卖出失败[未获取到满足条件的母账号:[okex]], 解冻用户资产.主委托uuid:8201ac43e10f487d9c251614ab6e1214', '2018-06-14 20:48:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('40', '897ebd0ad00c4198a81398cfa0882b6e', '2', 'BTC', '4.0000000000', '1492.5756647065', '1488.5756647065', '4.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:4', '2018-06-14 20:57:29', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('41', '9ae1cb869d264e54a5f05b4e4a2fe324', '2', 'BTC', '4.0000000000', '1488.0016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:4.0000000000,卖出失败[系统异常,拆单线程执行失败,原因:系统异常], 解冻用户资产.主委托uuid:4808872f996b43fe83c9347aa47be7f8', '2018-06-14 20:58:53', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('42', '5d627e4bb86943639416ba8c29f2cd98', '2', 'BTC', '6.0000000000', '1492.5756647065', '1486.5756647065', '6.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:6', '2018-06-14 21:04:30', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('43', 'f7df6fa344db47d9861da845408b1d6f', '2', 'BTC', '6.0000000000', '1486.0016657065', '1492.0016657065', '0.0000000000', '6', '使用[USDT]市价卖出[BTC],买入数量:6.0000000000,买入失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009],子委托uuid:accc4e89a692426eb0013329ced362b4', '2018-06-14 21:04:42', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('44', 'aae36773ad624c3ba49bad0b3006fa01', '2', 'BTC', '7.0000000000', '1492.5756647065', '1485.5756647065', '7.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:7', '2018-06-14 21:07:31', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('45', 'd3425d2eb5ff49fa91231057f8993ae2', '2', 'BTC', '7.0000000000', '1485.0016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:7.0000000000,卖出失败[未获取到满足条件的母账号:[okex]], 解冻用户资产.主委托uuid:5f14effa20344e6daaf2a36dda979f49', '2018-06-14 21:07:34', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('46', 'abfaa2dc884b407c97b08d279d6742cb', '2', 'BTC', '23.0000000000', '1492.5756647065', '1469.5756647065', '23.0000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:23', '2018-06-14 21:09:25', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('47', '84b18597bd524268b88e57af538dc8bf', '2', 'BTC', '23.0000000000', '1469.0016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:23.0000000000,卖出失败[null], 解冻用户资产.主委托uuid:afff3106be31477dba202d78ce3721c7', '2018-06-14 21:09:31', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('48', 'd3c5838517d948da9cd95497bf58ab3c', '2', 'BTC', '0.2000000000', '1492.5756647065', '1492.3756647065', '0.2000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:0.2', '2018-06-14 21:11:34', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('49', '78b7cea92b414f4494c794b96b898ddb', '2', 'BTC', '0.2000000000', '1491.8016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:0.2000000000,卖出失败[null], 解冻用户资产.主委托uuid:7a8e0766cb9148d7a88b13689aebc546', '2018-06-14 21:11:59', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('50', 'b1ad1969895c414a988970d0e74564da', '2', 'BTC', '0.5000000000', '1492.5756647065', '1492.0756647065', '0.5000000000', '4', '使用[BTC]市价卖出,以获得[USDT].卖出量:0.5', '2018-06-14 21:12:56', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('51', '77af4dfc65de454684c2c1edfc5fc45b', '2', 'BTC', '0.5000000000', '1491.5016657065', '1492.0016657065', '0.0000000000', '6', '市价卖出交易货币对[BTC_USDT],卖出[BTC],卖出数量:0.5000000000,卖出失败[系统异常,拆单线程执行失败,原因:系统异常], 解冻用户资产.主委托uuid:af82d29f79304a6b8546b93e1fc27fc4', '2018-06-14 21:14:48', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('52', '8e6afd7ae8d7469f8d27cb1e85c41cb7', '2', 'BTC', '0.1000000000', '10.0000000000', '9.9000000000', '0.1000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:0.1, 卖出价格:6600.5525', '2018-06-15 11:36:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('53', '0e477ba9ab3041b5b4ef7a1357470b5e', '2', 'USDT', '0.1000000000', '9.9000000000', '10.0000000000', '0.0000000000', '6', '使用[BTC]限价卖出[USDT],卖出数量:0.1000000000,卖出失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009]解冻用户资产.子委托uuid:1e74ebbb9db94953a21fd3ddd0a99b28', '2018-06-15 11:36:24', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('54', '20f9d9e6e3524b64ad1b641650fd2674', '2', 'BTC', '0.4000000000', '10.0000000000', '9.6000000000', '0.4000000000', '4', '使用[BTC]限价卖出,以获得[USDT].卖出量:0.4, 卖出价格:6600', '2018-06-15 11:37:40', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('55', '99a636d23fcb42178a4ad322896d9f99', '2', 'USDT', '0.3479690000', '9.6000000000', '9.9479690000', '0.0520310000', '6', '使用[BTC]限价卖出[USDT],卖出数量:0.3479690000,卖出失败-失败原因: api-signature-not-valid:Signature not valid: 公钥错误 [2009]解冻用户资产.子委托uuid:da54bd48f2fa46119fc6c1382a336150', '2018-06-15 11:37:50', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('56', '9f0d8ee2afdd45d69758964a96d32799', '2', 'USDT', '0.0520310000', '9.9479690000', '10.0000000000', '0.0000000000', '6', '使用[BTC]限价卖出[USDT],卖出数量:0.0520310000,卖出失败-失败原因: 交易所应答异常[-1100:Illegal characters found in parameter \'price\'; legal range is \'^([0-9]{1,20})(\\.[0-9]{1,20})?$\'.] [2009]解冻用户资产.子委托uuid:f9ca127de4df459caea93dc1aa9cb538', '2018-06-15 11:37:52', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('57', '7ddeb060db72441cb6c442d3a5984fb7', '2', 'ETH', '0.1066176000', '10.0000000000', '9.8933824000', '0.1066176000', '4', '使用[ETH]限价买入,以获得[HT]. 买入数量:12, 买入价格:0.0088848,消耗:0.1066176个[ETH]', '2018-06-15 21:53:02', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('58', '66e499f7d0f74a529ecb4ac8e81bde58', '2', 'ETH', '0.1065551220', '9.8933824000', '9.8933824000', '0.0000624780', '3', '限价买入订单成交，扣除冻结数量', '2018-06-15 21:53:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('59', '3c9ef1ee046049bda1363cce797ad33f', '2', 'HT', '11.9760000000', '0.0000000000', '11.9760000000', '0.0000000000', '9', '限价买入订单成交,新增资产持仓', '2018-06-15 21:53:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('60', '30d50b74ece8480f9e9a8c2c160050e5', '2', 'ETH', '1.0000000000', '9.8933824000', '8.8933824000', '1.0000624780', '7', '用户提币冻结:ETH货币1个', '2018-06-15 22:27:48', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('61', '6c0c94beaa27433cbc9d4a33a352e3b6', '2', 'ETH', '2.0000000000', '8.8933824000', '6.8933824000', '3.0000624780', '7', '用户提币冻结:ETH货币1个', '2018-06-15 22:27:48', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('62', '39629c507d9a46e08cf1dda436285043', '2', 'BTC', '2.0000000000', '10.0000000000', '8.0000000000', '2.0000000000', '7', '用户提币冻结:BTC货币2个', '2018-06-15 22:28:26', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('63', '3219bb395ca74616ad31f308fb5492fb', '2', 'BTC', '2.1000000000', '8.0000000000', '5.9000000000', '4.1000000000', '7', '用户提币冻结:BTC货币2个', '2018-06-15 22:28:26', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('64', 'd2d942b09cec4a39beafa71291fbdd36', '4', 'ETH', '0.0695200000', '20.0000000000', '19.9304800000', '0.0695200000', '4', '使用[ETH]限价买入,以获得[HT]. 买入数量:8, 买入价格:0.00869,消耗:0.06952个[ETH]', '2018-06-16 19:19:27', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('65', '2780cdf615254f4cb55ffcbebcffc92b', '4', 'ETH', '0.0695200000', '19.9304800000', '19.9304800000', '0.0000000000', '3', '限价买入订单成交，扣除冻结数量', '2018-06-16 19:19:35', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('66', '27cae855d5bc4d6995f106f72d0628cc', '4', 'HT', '7.9840000000', '0.0000000000', '7.9840000000', '0.0000000000', '9', '限价买入订单成交,新增资产持仓', '2018-06-16 19:19:35', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('67', '11c5d64904b242f5a61a0f67a59eb1e7', '4', 'ETH', '0.0212766600', '19.9304800000', '19.9092033400', '0.0212766600', '4', '使用[ETH]限价买入,以获得[EOS]. 买入数量:1, 买入价格:0.02127666,消耗:0.02127666个[ETH]', '2018-06-16 19:20:15', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('68', '2a8ed683cde2465a8addfb33d9ae9c9f', '4', 'ETH', '0.0212766600', '19.9092033400', '19.9304800000', '0.0000000000', '6', '使用[ETH]买入[EOS],买入数量:1.0000000000,消耗:0.0212766600个ETH,申请买入失败,解冻用户资产.主委托uuid:3244db73f29e4c5199bcff38cf804e8a', '2018-06-16 19:20:16', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('69', '4008b3addac24d66bf3dcd686ab33852', '4', 'ETH', '0.0212882300', '19.9304800000', '19.9091917700', '0.0212882300', '4', '使用[ETH]限价买入,以获得[EOS]. 买入数量:1, 买入价格:0.02128823,消耗:0.02128823个[ETH]', '2018-06-16 19:21:21', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('70', '8b951c6b4bdd44479c4f2c6513c456f6', '4', 'ETH', '0.0212882300', '19.9091917700', '19.9091917700', '0.0000000000', '3', '限价买入订单成交，扣除冻结数量', '2018-06-16 19:22:09', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('71', '3d1068684d164c5aa8ec8b497707feda', '4', 'EOS', '0.9990000000', '0.0000000000', '0.9990000000', '0.0000000000', '9', '限价买入订单成交,新增资产持仓', '2018-06-16 19:22:09', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('72', '450c2c2876a44aaa9ad0242f06e13560', '4', 'ETH', '1.1932468800', '19.9091917700', '18.7159448900', '1.1932468800', '4', '使用[ETH]限价买入,以获得[EOS]. 买入数量:56, 买入价格:0.02130798,消耗:1.19324688个[ETH]', '2018-06-16 19:23:28', null, null, null, '0');
INSERT INTO `tx_account_log` VALUES ('73', '51f1c5828b7c4f72af01b7d8fb2d65d4', '4', 'ETH', '1.1932468800', '18.7159448900', '19.9091917700', '0.0000000000', '6', '使用[ETH]买入[EOS],买入数量:56.0000000000,消耗:1.1932468800个ETH,申请买入失败,解冻用户资产.子委托uuid:72342ac6e07b49afbb6189fe4ef44f92', '2018-06-16 19:23:35', null, null, null, '0');

-- ----------------------------
-- Table structure for `tx_deal_order`
-- ----------------------------
DROP TABLE IF EXISTS `tx_deal_order`;
CREATE TABLE `tx_deal_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` varchar(32) NOT NULL DEFAULT '' COMMENT 'uuid',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `entrust_no` varchar(32) DEFAULT '' COMMENT '委托编号',
  `business_no` varchar(32) DEFAULT '' COMMENT '成交编号',
  `coin_currency` varchar(50) DEFAULT '' COMMENT '币种',
  `coin_code` varchar(50) DEFAULT '' COMMENT '资产代码',
  `amount` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '成交量',
  `price` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '成交价',
  `gmv` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '成交额',
  `exchange` varchar(50) DEFAULT '' COMMENT '交易所',
  `mother_account` varchar(50) DEFAULT '' COMMENT '交易母账号',
  `is_confirm` tinyint(4) DEFAULT '0' COMMENT '是否对账 1,是:yes;0,否:no',
  `state` tinyint(4) DEFAULT '1' COMMENT '状态 1,全部完成:finished;',
  `direction` tinyint(4) DEFAULT '1' COMMENT '委托方向 1,买入:buyIn;2,卖出:sellOut',
  `style` tinyint(4) DEFAULT '1' COMMENT '委托方式 1,市价委托:market;2,限价委托:limited',
  `service_fee` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '交易所收取的手续费',
  `plat_fee` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '平台收取的手续费',
  `deal_time` datetime DEFAULT NULL COMMENT '成交时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(50) DEFAULT '' COMMENT '创建人',
  `updator` varchar(50) DEFAULT '' COMMENT '更新人',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记 0,否:no;1,是:yes',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_no` (`business_no`),
  KEY `del_flag` (`del_flag`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='交易管理-成交订单';

-- ----------------------------
-- Records of tx_deal_order
-- ----------------------------
INSERT INTO `tx_deal_order` VALUES ('1', '703728f8ca7c45cda0f3e716ba3d3f5d', '2', '5923318517', '1767128116', 'ETH', 'HT', '0000000006.6000000000', '0000000000.0088848000', '0000000000.0586396800', 'huobi', '火币母账号', '0', '1', '1', '2', '0000000000.0132000000', '0000000000.0132000000', '2018-06-15 21:53:17', '2018-06-15 21:53:15', null, '', '', '0');
INSERT INTO `tx_deal_order` VALUES ('2', '0b77bc4f031b44ebb5e080c5d7f0dbea', '2', '5923318517', '1767128105', 'ETH', 'HT', '0000000005.4000000000', '0000000000.0088732300', '0000000000.0479154420', 'huobi', '火币母账号', '0', '1', '1', '2', '0000000000.0108000000', '0000000000.0108000000', '2018-06-15 21:53:17', '2018-06-15 21:53:15', null, '', '', '0');
INSERT INTO `tx_deal_order` VALUES ('3', 'ac1e5328a4a04f719d44ccc3b617c215', '4', '5978667222', '1774501634', 'ETH', 'HT', '0000000008.0000000000', '0000000000.0086900000', '0000000000.0695200000', 'huobi', '火币母账号', '0', '1', '1', '2', '0000000000.0160000000', '0000000000.0160000000', '2018-06-16 19:19:35', '2018-06-16 19:19:35', null, '', '', '0');
INSERT INTO `tx_deal_order` VALUES ('4', '14a89e2517d7412bb62b43140c85008c', '4', '5978746169', '1774514765', 'ETH', 'EOS', '0000000001.0000000000', '0000000000.0212882300', '0000000000.0212882300', 'huobi', '火币母账号', '0', '1', '1', '2', '0000000000.0020000000', '0000000000.0010000000', '2018-06-16 19:22:09', '2018-06-16 19:22:09', null, '', '', '0');

-- ----------------------------
-- Table structure for `tx_main_delegation`
-- ----------------------------
DROP TABLE IF EXISTS `tx_main_delegation`;
CREATE TABLE `tx_main_delegation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` varchar(32) NOT NULL DEFAULT '' COMMENT 'uuid',
  `state` tinyint(4) DEFAULT '1' COMMENT '状态 1,下单申请(已报):reported;2,委托中:commissionedIn;3,已完成:finished;4,失败:failed;5;',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `delegate_no` varchar(32) DEFAULT '' COMMENT '委托编号',
  `style` tinyint(4) DEFAULT '1' COMMENT '委托方式 1,市价委托:market;2,限价委托:limited',
  `origin` tinyint(4) DEFAULT '1' COMMENT '来源 1,PC:pc;2,IOS:ios;3,安卓:android',
  `direction` tinyint(4) DEFAULT '1' COMMENT '方向 1,买入:buyIn;2,卖出:sellOut',
  `coin_currency` varchar(10) NOT NULL DEFAULT '' COMMENT '委托币种',
  `remark` text COMMENT '备注',
  `coin_code` varchar(10) DEFAULT '' COMMENT '委托资产代码',
  `amount` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '委托量',
  `price` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '委托价',
  `gmv` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '交易额',
  `service_fee_scale` double unsigned DEFAULT '0' COMMENT '手续费率',
  `is_split` tinyint(4) DEFAULT '0' COMMENT '是否拆单了 0否 1是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(50) DEFAULT '' COMMENT '创建人',
  `updator` varchar(50) DEFAULT '' COMMENT '更新人',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记 0,否:no;1,是:yes',
  PRIMARY KEY (`id`),
  KEY `del_flag` (`del_flag`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `delegate_no` (`delegate_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='交易管理-主委托管理';

-- ----------------------------
-- Records of tx_main_delegation
-- ----------------------------
INSERT INTO `tx_main_delegation` VALUES ('1', '41439e749f5d4ca08ca6f43ef4ed84b5', '4', '2', 'M1806151815756841', '2', '1', '2', 'USDT', '子委托下单失败,失败原因:api-signature-not-valid:Signature not valid: 公钥错误 [2009]', 'BTC', '0000000000.1000000000', '0000006600.5525000000', '0000000660.0552500000', '0.001', '0', '2018-06-15 11:36:15', '2018-06-15 11:36:24', '用户2', 'system', '0');
INSERT INTO `tx_main_delegation` VALUES ('2', '477a300ddd2b4ce5a3097c94f0a58aad', '3', '2', 'M1806150394945064', '2', '1', '2', 'USDT', '子委托部分撤回-m', 'BTC', '0000000000.4000000000', '0000006600.0000000000', '0000002640.0000000000', '0.001', '0', '2018-06-15 11:37:40', '2018-06-15 11:37:50', '用户2', 'system', '0');
INSERT INTO `tx_main_delegation` VALUES ('3', '30a4eff562d1479d8e6d19a372286bba', '3', '2', 'M1806151138206952', '2', '1', '1', 'ETH', '已成交', 'HT', '0000000012.0000000000', '0000000000.0088848000', '0000000000.1066176000', '0.002', '1', '2018-06-15 21:53:02', '2018-06-15 21:53:15', '用户2', 'system', '0');
INSERT INTO `tx_main_delegation` VALUES ('4', 'f8eb5a81336f472bb46c070fe75ffcff', '3', '4', 'M1806160012530439', '2', '1', '1', 'ETH', '已成交', 'HT', '0000000008.0000000000', '0000000000.0086900000', '0000000000.0695200000', '0.002', '1', '2018-06-16 19:19:27', '2018-06-16 19:19:36', '用户4', 'system', '0');
INSERT INTO `tx_main_delegation` VALUES ('5', '3244db73f29e4c5199bcff38cf804e8a', '4', '4', 'M1806160566916710', '2', '1', '1', 'ETH', '未获取到满足条件的母账号:[okex]', 'EOS', '0000000001.0000000000', '0000000000.0212766600', '0000000000.0212766600', '0.001', '1', '2018-06-16 19:20:15', '2018-06-16 19:20:16', '用户4', '', '0');
INSERT INTO `tx_main_delegation` VALUES ('6', 'f56a8ea609be4e07be8d8bc2485e2bf8', '3', '4', 'M1806160724980369', '2', '1', '1', 'ETH', '已成交', 'EOS', '0000000001.0000000000', '0000000000.0212882300', '0000000000.0212882300', '0.001', '0', '2018-06-16 19:21:21', '2018-06-16 19:22:09', '用户4', 'system', '0');
INSERT INTO `tx_main_delegation` VALUES ('7', 'b67817df868e4944b85bb7e3e98c179f', '4', '4', 'M1806161656369495', '2', '1', '1', 'ETH', '子委托下单失败,失败原因:account-frozen-balance-insufficient-error:trade account balance is not enough, left: `0.5649` [2009]', 'EOS', '0000000056.0000000000', '0000000000.0213079800', '0000000001.1932468800', '0.001', '0', '2018-06-16 19:23:28', '2018-06-16 19:23:35', '用户4', 'system', '0');

-- ----------------------------
-- Table structure for `tx_sub_delegation`
-- ----------------------------
DROP TABLE IF EXISTS `tx_sub_delegation`;
CREATE TABLE `tx_sub_delegation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` varchar(32) NOT NULL DEFAULT '' COMMENT 'uuid',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `state` tinyint(4) DEFAULT '1' COMMENT '状态 1,下单申请(已报):reported;2,交易中:trading;3,撤单中:revoking;4,已完全撤单:revoked;5,部分撤单:partOfRevoke;6,部分成交:deal;7,已成交:deal;8,下单失败:failed',
  `coin_currency` varchar(50) DEFAULT 'BTC' COMMENT '币种',
  `coin_code` varchar(50) DEFAULT '' COMMENT '资产代码',
  `main_delegate_no` varchar(32) DEFAULT '' COMMENT '所属主委托编号',
  `entrust_no` varchar(32) DEFAULT '' COMMENT '委托编号',
  `bill_no` varchar(32) DEFAULT '' COMMENT '本系统中的订单号',
  `amount` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '委托量',
  `price` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '委托价',
  `gmv` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '交易额',
  `deal_price` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '成交价',
  `deal_amount` decimal(20,10) unsigned zerofill DEFAULT '0000000000.0000000000' COMMENT '成交量',
  `style` tinyint(4) DEFAULT '1' COMMENT '委托方式 1,市价委托:market;2,限价委托:limited',
  `direction` tinyint(4) DEFAULT '1' COMMENT '委托方向 1,买入:buyIn;2,卖出:sellOut',
  `service_fee` decimal(20,10) unsigned DEFAULT '0.0000000000' COMMENT '手续费',
  `service_fee_scale` double unsigned DEFAULT '0' COMMENT '手续费率',
  `has_brother` tinyint(4) DEFAULT '0' COMMENT '是否有兄弟订单 0否 1是',
  `exchange` varchar(50) DEFAULT '' COMMENT '交易所',
  `mother_account` varchar(50) DEFAULT '' COMMENT '交易母账号',
  `is_confirm` tinyint(4) DEFAULT '0' COMMENT '是否对账 1,是:yes;0,否:no',
  `info` varchar(500) DEFAULT '' COMMENT '订单描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(50) DEFAULT '' COMMENT '创建人',
  `updator` varchar(50) DEFAULT '' COMMENT '更新人',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记 0,否:no;1,是:yes',
  PRIMARY KEY (`id`),
  KEY `del_flag` (`del_flag`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `entrust_no` (`entrust_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='交易管理-子委托管理';

-- ----------------------------
-- Records of tx_sub_delegation
-- ----------------------------
INSERT INTO `tx_sub_delegation` VALUES ('1', '1e74ebbb9db94953a21fd3ddd0a99b28', '2', '8', 'USDT', 'BTC', 'M1806151815756841', '', 'S1806150635534188', '0000000000.1000000000', '0000006600.5525000000', '0000000660.0552500000', '0000000000.0000000000', '0000000000.0000000000', '2', '2', '0.0000000000', '0.001', '1', 'huobi', 'account_huobi', '0', 'api-signature-not-valid:Signature not valid: 公钥错误 [2009]', '2018-06-15 11:36:20', '2018-06-15 11:36:24', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('2', 'da54bd48f2fa46119fc6c1382a336150', '2', '8', 'USDT', 'BTC', 'M1806150394945064', '', 'S1806151866385514', '0000000000.3479690000', '0000006600.0000000000', '0000002296.5954000000', '0000000000.0000000000', '0000000000.0000000000', '2', '2', '0.0000000000', '0.001', '1', 'huobi', 'account_huobi', '0', 'api-signature-not-valid:Signature not valid: 公钥错误 [2009]', '2018-06-15 11:37:41', '2018-06-15 11:37:45', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('3', 'f9ca127de4df459caea93dc1aa9cb538', '2', '8', 'USDT', 'BTC', 'M1806150394945064', '', 'S1806151031046481', '0000000000.0520310000', '0000006600.0000000000', '0000000343.4046000000', '0000000000.0000000000', '0000000000.0000000000', '2', '2', '0.0000000000', '0.001', '1', 'bian', 'account_bian', '0', '交易所应答异常[-1100:Illegal characters found in parameter \'price\'; legal range is \'^([0-9]{1,20})(\\.[0-9]{1,20})?$\'.] [2009]', '2018-06-15 11:37:44', '2018-06-15 11:37:51', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('4', '0868c8e79d484ec4bf8a9f1e227a1dff', '2', '7', 'ETH', 'HT', 'M1806151138206952', '5923318517', 'S1806150156237009', '0000000012.0000000000', '0000000000.0088848000', '0000000000.1066176000', '0000000000.0088848000', '0000000012.0000000000', '2', '1', '0.0000000000', '0.002', '1', 'huobi', '火币母账号', '0', '生成子委托成功,已报', '2018-06-15 21:53:04', '2018-06-15 21:53:15', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('5', '733df00020b140a3b543169373a3b002', '4', '7', 'ETH', 'HT', 'M1806160012530439', '5978667222', 'S1806160181711455', '0000000008.0000000000', '0000000000.0086900000', '0000000000.0695200000', '0000000000.0086900000', '0000000008.0000000000', '2', '1', '0.0000000000', '0.002', '1', 'huobi', '火币母账号', '0', '生成子委托成功,已报', '2018-06-16 19:19:29', '2018-06-16 19:19:35', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('6', '83438a64ad39442b95410ea0de1d8821', '4', '7', 'ETH', 'EOS', 'M1806160724980369', '5978746169', 'S1806161731225630', '0000000001.0000000000', '0000000000.0212882300', '0000000000.0212882300', '0000000000.0212882300', '0000000001.0000000000', '2', '1', '0.0000000000', '0.001', '0', 'huobi', '火币母账号', '0', '生成子委托成功,已报', '2018-06-16 19:21:21', '2018-06-16 19:22:09', '', '', '0');
INSERT INTO `tx_sub_delegation` VALUES ('7', '72342ac6e07b49afbb6189fe4ef44f92', '4', '8', 'ETH', 'EOS', 'M1806161656369495', '', 'S1806160098677578', '0000000056.0000000000', '0000000000.0213079800', '0000000001.1932468800', '0000000000.0000000000', '0000000000.0000000000', '2', '1', '0.0000000000', '0.001', '0', 'huobi', '火币母账号', '0', 'account-frozen-balance-insufficient-error:trade account balance is not enough, left: `0.5649` [2009]', '2018-06-16 19:23:28', '2018-06-16 19:23:35', '', '', '0');
