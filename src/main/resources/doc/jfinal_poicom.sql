# Host: localhost  (Version: 5.6.23-log)
# Date: 2015-03-19 17:02:03
# Generator: MySQL-Front 5.3  (Build 4.89)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "com_branch"
#

DROP TABLE IF EXISTS `com_branch`;
CREATE TABLE `com_branch` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '分公司名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='工作单位';

#
# Data for table "com_branch"
#

INSERT INTO `com_branch` VALUES (1,'中山总公司','2015-03-11 10:42:55',NULL,NULL),(2,'湖南分公司','2015-03-11 10:43:09',NULL,NULL),(3,'重庆公司','2015-03-11 10:43:21',NULL,NULL),(4,'江西分公司','2015-03-11 10:43:32',NULL,NULL),(5,'安徽分公司','2015-03-11 10:43:42',NULL,NULL),(6,'南京分公司','2015-03-11 10:43:53',NULL,NULL),(7,'吉林分公司','2015-03-11 10:44:02',NULL,NULL),(8,'辽宁分公司','2015-03-11 10:44:12',NULL,NULL),(9,'广西分公司','2015-03-11 10:44:22',NULL,NULL);

#
# Structure for table "com_order"
#

DROP TABLE IF EXISTS `com_order`;
CREATE TABLE `com_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `offer_user` bigint(11) DEFAULT NULL COMMENT '申报人',
  `description` text COMMENT '故障描述',
  `type` bigint(11) DEFAULT NULL COMMENT '故障类型id',
  `offer_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申报时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '工单修改时间',
  `status` int(11) DEFAULT NULL COMMENT '0：未处理 1：已处理 2：已提醒',
  `deal_user` bigint(11) DEFAULT NULL COMMENT '受理人',
  `comment` text COMMENT '处理意见',
  `deal_at` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='工单';

#
# Data for table "com_order"
#

INSERT INTO `com_order` VALUES (1,4,'电脑故障，电脑坏了，坏了，坏了，真的坏了，如何处理。电脑故障，电脑坏了，坏了，坏了，真的坏了，如何处理。',3,'2015-03-10 15:29:46',NULL,1,3,'马上处理好。','2015-03-16 17:30:23'),(2,4,'打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，',1,'2015-03-12 09:27:52','2015-03-16 15:45:25',1,2,'马上处理好','2015-03-16 17:31:46'),(3,4,'电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，',2,'2015-03-12 09:28:39',NULL,1,3,'建议测试','2015-03-13 00:00:00'),(4,4,'电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通',2,'2015-03-12 09:29:58',NULL,1,3,'重新制作电话头接口。','2015-03-17 17:11:57'),(5,1,'管理员测试管理员测试管理员测试管理员测试管理员测试管理员测试管理员测试管理员测试管理员测试',1,'2015-03-12 10:47:06',NULL,1,3,NULL,NULL),(6,4,'电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办',3,'2015-03-13 16:47:13',NULL,1,2,'处理电脑故障','2015-03-19 09:37:46'),(7,4,'打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了',1,'2015-03-13 16:52:37',NULL,1,2,'处理打印机故障','2015-03-19 09:38:16'),(8,4,'打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了',1,'2015-03-13 16:55:21',NULL,1,2,'继续处理打印机故障','2015-03-19 09:38:27'),(9,4,'打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。',1,'2015-03-16 09:03:34',NULL,1,3,'处理打印机功能。','2015-03-19 12:27:30'),(10,4,'打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊',1,'2015-03-16 15:03:54','2015-03-19 14:06:02',0,NULL,NULL,NULL),(12,4,'快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟',2,'2015-03-16 15:10:05','2015-03-16 15:47:46',0,NULL,NULL,NULL),(13,4,'电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊',2,'2015-03-16 15:10:59','2015-03-16 15:47:28',0,NULL,NULL,NULL),(14,4,'打印机故障了好吗打印机故障了好吗打印机故障了好吗打印机故障了好吗打印机故障了好吗打印机故障了好吗打印机故障了好吗',1,'2015-03-16 15:25:21',NULL,0,NULL,NULL,NULL),(15,4,'发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理',2,'2015-03-17 09:41:35',NULL,0,NULL,NULL,NULL),(16,4,'测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件',3,'2015-03-17 09:48:16',NULL,0,NULL,NULL,NULL),(17,4,'邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试',1,'2015-03-17 09:59:06',NULL,0,NULL,NULL,NULL),(18,4,'继续邮件测试继续邮件测试继续邮件测试继续邮件测试继续邮件测试继续邮件测试继续邮件测试',1,'2015-03-17 10:07:20',NULL,0,NULL,NULL,NULL),(19,4,'邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试',3,'2015-03-17 10:15:08',NULL,0,NULL,NULL,NULL),(20,4,'邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试邮件测试',2,'2015-03-17 11:17:27',NULL,0,NULL,NULL,NULL),(21,4,'故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试故障邮件测试',1,'2015-03-17 11:27:43',NULL,0,NULL,NULL,NULL),(22,4,'邮件测试继续。邮件测试继续。邮件测试继续。邮件测试继续。邮件测试继续。邮件测试继续。邮件测试继续。邮件测试继续。',1,'2015-03-17 11:31:57',NULL,0,NULL,NULL,NULL),(23,4,'上放水电费上放水电费上放水电费上放水电费上放水电费上放水电费上放水电费上放水电费',1,'2015-03-17 11:34:26',NULL,0,NULL,NULL,NULL),(24,4,'sfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfssfsdfsdfs',1,'2015-03-17 11:35:40',NULL,0,NULL,NULL,NULL),(25,4,'小林，我的打印机坏了，快过来帮我处理一下呗。小林，我的打印机坏了，快过来帮我处理一下呗。小林，我的打印机坏了，快过来帮我处理一下呗。小林，我的打印机坏了，快过来帮我处理一下呗。',1,'2015-03-17 11:49:22',NULL,0,NULL,NULL,NULL),(26,4,'收费宋丹丹收费宋丹丹收费宋丹丹收费宋丹丹收费宋丹丹收费宋丹丹收费宋丹丹收费宋丹丹',1,'2015-03-18 17:54:14',NULL,0,NULL,NULL,NULL),(27,4,'打印机故障。',1,'2015-03-19 10:07:00',NULL,0,NULL,NULL,NULL),(28,4,'测试时间-秒。',2,'2015-03-19 10:11:56',NULL,0,NULL,NULL,NULL),(29,4,'处理打印机',1,'2015-03-19 10:18:26',NULL,0,NULL,NULL,NULL),(30,4,'淡淡的',1,'2015-03-19 11:45:35',NULL,0,NULL,NULL,NULL),(31,4,'上放水电费',1,'2015-03-19 11:46:38',NULL,0,NULL,NULL,NULL),(32,4,'淡淡的淡淡',1,'2015-03-19 11:46:50',NULL,0,NULL,NULL,NULL),(33,4,'开发商就到了分开就是打了客服就是了快点交房了时间到了分开就是打了客服就流口水的减肥了开始觉得浪费快睡觉多了分开就是打了客服吉林省但就是分了快睡觉多了分开就是打了客服就落实经费了深刻觉得浪费框架上打了客服就就分了就是打了飞机了深刻的肌肤。',2,'2015-03-19 12:05:41',NULL,0,NULL,NULL,NULL),(34,4,'打印机故障了',1,'2015-03-19 14:09:16',NULL,0,NULL,NULL,NULL),(35,4,'我的打印机坏了，麻烦帮忙处理一下。',1,'2015-03-19 16:18:46',NULL,0,NULL,NULL,NULL);

#
# Structure for table "com_type"
#

DROP TABLE IF EXISTS `com_type`;
CREATE TABLE `com_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '故障名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='故障类型';

#
# Data for table "com_type"
#

INSERT INTO `com_type` VALUES (1,'打印机故障'),(2,'电话故障'),(3,'电脑故障');

#
# Structure for table "com_user_type"
#

DROP TABLE IF EXISTS `com_user_type`;
CREATE TABLE `com_user_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='运维故障';

#
# Data for table "com_user_type"
#

INSERT INTO `com_user_type` VALUES (1,2,1),(2,2,2),(3,2,3),(4,3,1),(5,3,2);

#
# Structure for table "sec_permission"
#

DROP TABLE IF EXISTS `sec_permission`;
CREATE TABLE `sec_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL COMMENT '值',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `intro` varchar(255) DEFAULT NULL COMMENT '简介',
  `pid` bigint(20) DEFAULT '0' COMMENT '父级id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='权限';

#
# Data for table "sec_permission"
#

INSERT INTO `sec_permission` VALUES (1,'管理员目录','P_ADMIN','/admin/**','',0,'2014-12-16 12:22:31','2015-03-11 10:20:23',NULL),(2,'权限管理','P_A_ROLE','/admin/role/**','',1,'2014-12-16 12:22:31','2015-03-11 10:20:29',NULL),(3,'用户管理','P_A_USER','/admin/user/**','',1,'2014-12-16 12:22:31','2015-03-11 10:20:33',NULL),(4,'工单管理','P_A_ORDER','/admin/order/**','',1,'2014-12-16 12:22:31','2015-03-11 10:20:37',NULL),(5,'异常管理','P_A_EXCEPTION','/admin/exception/**',NULL,1,'2015-03-10 12:27:55','2015-03-11 10:20:41',NULL),(6,'运维管理','P_OPERATE','/operate/**',NULL,0,'2015-03-11 10:07:27','2015-03-16 10:12:04',NULL),(7,'故障处理','P_O_DEAL','/operate/deal/**',NULL,6,'2015-03-11 10:12:23','2015-03-16 10:12:07',NULL),(8,'任务分配','P_O_TASK','/operate/task/**',NULL,6,'2015-03-11 10:18:15','2015-03-16 10:12:14',NULL),(9,'报障管理','P_REPORT','/report/**',NULL,0,'2015-03-11 10:13:18','2015-03-11 10:17:34',NULL),(10,'故障申报','P_R_OFFER','/report/offer/**',NULL,9,'2015-03-11 10:16:25','2015-03-11 10:20:02',NULL);

#
# Structure for table "sec_role"
#

DROP TABLE IF EXISTS `sec_role`;
CREATE TABLE `sec_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL COMMENT '值',
  `intro` varchar(255) DEFAULT NULL COMMENT '简介',
  `pid` bigint(20) DEFAULT '0' COMMENT '父级id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='角色';

#
# Data for table "sec_role"
#

INSERT INTO `sec_role` VALUES (1,'超级管理员','R_ADMIN','',0,'2014-12-16 12:22:31',NULL,NULL),(2,'系统管理员','R_MANAGER','',1,'2014-12-16 12:22:31',NULL,NULL),(3,'运维主管','R_DEAL','',0,'2014-12-16 12:22:31','2015-03-11 10:21:25',NULL),(4,'运维员','R_DEALLER',NULL,3,'2015-03-11 09:57:54','2015-03-11 09:58:49',NULL),(5,'报障员','R_OFFER','',0,'2014-12-16 12:22:31','2015-03-11 10:21:30',NULL);

#
# Structure for table "sec_role_permission"
#

DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='角色权限';

#
# Data for table "sec_role_permission"
#

INSERT INTO `sec_role_permission` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,1,7),(8,1,8),(9,1,9),(10,1,10),(11,3,6),(12,3,7),(13,3,8),(14,4,6),(15,4,7),(16,5,9),(17,5,10);

#
# Structure for table "sec_user"
#

DROP TABLE IF EXISTS `sec_user`;
CREATE TABLE `sec_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '登录名',
  `providername` varchar(50) NOT NULL COMMENT '提供者',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `hasher` varchar(200) NOT NULL COMMENT '加密类型',
  `salt` varchar(200) NOT NULL COMMENT '加密盐',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `first_name` varchar(10) DEFAULT NULL COMMENT '名字',
  `last_name` varchar(10) DEFAULT NULL COMMENT '姓氏',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户';

#
# Data for table "sec_user"
#

INSERT INTO `sec_user` VALUES (1,'admin','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher',' ',NULL,'科技','点通','点通科技','2015-03-11 10:46:12','2015-03-19 14:34:05',NULL),(2,'deal','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher','','','科技','点通','运维主管','2015-03-10 14:48:05','2015-03-19 17:01:35',NULL),(3,'dealler','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher',' ',NULL,'张','三','运维员','2015-03-10 15:35:38','2015-03-19 14:34:09',NULL),(4,'reporter','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$vv1ASoRellUpqolJG+CJMw==$pvuQLRS0agh1ScGnwJpBDuAEUTnVDgJNcTu2MjQn8vc=','default_hasher','',NULL,'李','四','周星星','2015-03-11 10:40:18','2015-03-19 16:03:34',NULL);

#
# Structure for table "sec_user_info"
#

DROP TABLE IF EXISTS `sec_user_info`;
CREATE TABLE `sec_user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `gender` int(11) DEFAULT '0' COMMENT '性别0男，1女',
  `branch_id` bigint(20) DEFAULT NULL COMMENT '分公司id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户信息';

#
# Data for table "sec_user_info"
#

INSERT INTO `sec_user_info` VALUES (1,1,1,0,1,'2015-03-11 10:42:24',NULL,NULL),(2,4,1,0,3,'2015-03-13 15:35:03','2015-03-16 17:18:24',NULL),(3,3,1,0,1,'2015-03-16 12:18:29','2015-03-16 12:19:19',NULL),(4,2,1,0,1,'2015-03-16 17:18:17',NULL,NULL);

#
# Structure for table "sec_user_role"
#

DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户角色';

#
# Data for table "sec_user_role"
#

INSERT INTO `sec_user_role` VALUES (1,1,1),(2,2,3),(3,3,4),(4,4,5);
