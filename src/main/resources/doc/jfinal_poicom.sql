# Host: localhost  (Version: 5.6.23-log)
# Date: 2015-04-11 16:49:21
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='工作单位';

#
# Data for table "com_branch"
#

INSERT INTO `com_branch` VALUES (1,'中山总公司','2015-03-11 10:42:55',NULL,NULL),(2,'湖南分公司','2015-03-11 10:43:09',NULL,NULL),(3,'重庆公司','2015-03-11 10:43:21',NULL,NULL),(4,'江西分公司','2015-03-11 10:43:32',NULL,NULL),(5,'安徽分公司','2015-03-11 10:43:42',NULL,NULL),(6,'南京分公司','2015-03-11 10:43:53',NULL,NULL),(7,'吉林分公司','2015-03-11 10:44:02',NULL,NULL),(8,'辽宁分公司','2015-03-11 10:44:12','2015-04-08 11:15:26',NULL),(9,'广西分公司','2015-03-11 10:44:22','2015-04-08 11:15:50',NULL),(10,'中山总公司 运维部','2015-03-20 12:22:03','2015-04-08 11:15:50',NULL);

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
  `status` int(11) DEFAULT NULL COMMENT '0：已处理 1：未处理 2：超时 已提醒',
  `deal_user` bigint(11) DEFAULT NULL COMMENT '受理人',
  `comment` text COMMENT '处理意见',
  `deal_at` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  `spend_time` bigint(20) DEFAULT NULL COMMENT '申报-处理时间差',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COMMENT='工单';

#
# Data for table "com_order"
#

INSERT INTO `com_order` VALUES (1,4,'电脑故障，电脑坏了，坏了，坏了，真的坏了，如何处理。电脑故障，电脑坏了，坏了，坏了，真的坏了，如何处理。',3,'2015-03-10 15:29:46',NULL,0,3,'马上处理好。','2015-03-16 17:30:23',NULL),(2,4,'打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，打印机故障，',1,'2015-03-12 09:27:52','2015-03-16 15:45:25',0,2,'马上处理好','2015-03-16 17:31:46',NULL),(3,4,'电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，电话故障，',2,'2015-03-12 09:28:39',NULL,0,3,'建议测试','2015-03-13 00:00:00',NULL),(4,4,'电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通电话打不通',2,'2015-03-12 09:29:58',NULL,0,3,'重新制作电话头接口。','2015-03-17 17:11:57',NULL),(5,1,'打印机故障很久了，需要过来修理。',1,'2015-03-12 10:47:06',NULL,0,3,'处理好了','2015-03-17 17:11:57',NULL),(6,4,'电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办电脑坏了怎么办',3,'2015-03-13 16:47:13',NULL,0,2,'处理电脑故障','2015-03-19 09:37:46',NULL),(7,4,'打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了打印机打印不了',1,'2015-03-13 16:52:37',NULL,0,2,'处理打印机故障','2015-03-19 09:38:16',NULL),(8,4,'打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了打印机又坏了',1,'2015-03-13 16:55:21',NULL,0,2,'继续处理打印机故障','2015-03-19 09:38:27',NULL),(9,4,'打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。打印机还是不可以使用。',1,'2015-03-16 09:03:34',NULL,0,3,'处理打印机功能。','2015-03-19 12:27:30',NULL),(10,4,'打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊打印机啊啊啊啊啊啊啊',1,'2015-03-16 15:03:54','2015-03-19 14:06:02',0,2,'策略','2015-03-20 14:49:43',169),(12,4,'快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟快点处理电话故障吧兄弟',2,'2015-03-16 15:10:05','2015-03-16 15:47:46',0,2,' ','2015-03-23 09:00:03',NULL),(13,4,'电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊电话打不通啊，怎么办才好啊',2,'2015-03-16 15:10:59','2015-03-16 15:47:28',0,2,'处理。','2015-03-23 09:07:24',NULL),(14,4,' 水电费水电费',1,'2015-03-16 15:25:21','2015-03-23 09:35:45',0,2,' ','2015-03-23 09:36:29',NULL),(15,4,'发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理发送故障了，赶紧处理',2,'2015-03-17 09:41:35',NULL,0,2,'的','2015-03-23 16:04:44',NULL),(16,4,'都坏了怎么办，电话电脑萌萌哒。',3,'2015-03-17 09:48:16',NULL,0,2,'的','2015-03-23 16:15:33',NULL),(17,4,'打印机萌萌哒',1,'2015-03-17 09:59:06',NULL,0,2,'处理','2015-03-20 14:50:04',NULL),(18,4,'打印机萌萌哒',1,'2015-03-17 10:07:20',NULL,0,2,'的份上','2015-03-23 16:18:01',NULL),(19,4,'电话你够了哦',3,'2015-03-17 10:15:08',NULL,0,2,'1','2015-03-23 16:21:12',NULL),(20,4,'搞错，我是电脑',2,'2015-03-17 11:17:27',NULL,0,2,'的','2015-03-23 16:37:35',NULL),(21,4,'在么？',1,'2015-03-17 11:27:43',NULL,0,2,' ','2015-03-23 16:45:50',NULL),(22,4,'不在么？',1,'2015-03-17 11:31:57',NULL,0,3,'处理','2015-03-20 14:54:15',NULL),(23,4,'上放水电费。',1,'2015-03-17 11:34:26','2015-03-21 10:36:37',0,2,'淡淡的','2015-03-23 16:46:33',NULL),(24,4,'动次打次',1,'2015-03-17 11:35:40','2015-03-24 10:49:18',0,2,'处理处理处理处理处理处理处理处理处理处理处理处理处理处理处理处理','2015-03-24 11:21:56',NULL),(25,4,'小木，我的打印机坏了，快过来帮我处理一下呗。',1,'2015-03-17 11:49:22',NULL,0,2,'快过来帮我处理一下呗。\r\n快过来帮我处理一下呗。\r\n快过来帮我处理一下呗。\r\n快过来帮我处理一下呗。','2015-03-24 11:31:19',NULL),(26,4,'完毕',1,'2015-03-18 17:54:14',NULL,0,2,' ','2015-03-23 09:07:38',NULL),(27,4,'打印机故障。',1,'2015-03-19 10:07:00','2015-03-24 10:49:37',0,2,'123456789123456789123456789','2015-03-24 12:21:33',NULL),(28,4,'测试时间-秒。',2,'2015-03-19 10:11:56',NULL,0,2,'胜多负少','2015-03-23 16:10:09',NULL),(29,4,'处理打印机',1,'2015-03-19 10:18:26',NULL,0,2,'1','2015-03-23 16:14:34',NULL),(30,4,'淡淡的点点滴滴',1,'2015-03-19 11:45:35','2015-03-24 11:54:29',0,2,'中山总公司 运维部中山总公司 运维部中山总公司 运维部','2015-03-24 12:23:13',NULL),(31,4,'上放水电费上放水电费，，，，，，，。。。。。，，，士大夫似的，',1,'2015-03-19 11:46:38','2015-03-24 16:44:43',0,2,'的放水电费速度发水电费水电费水电费水电费水电费是的。大富豆腐干豆腐','2015-03-24 16:46:58',NULL),(32,4,'淡淡的淡淡',1,'2015-03-19 11:46:50',NULL,0,2,'淡淡的淡淡','2015-04-01 10:45:51',NULL),(33,4,'开发商就到了分开就是打',2,'2015-03-19 12:05:41',NULL,0,3,'处理','2015-03-20 14:54:24',NULL),(34,4,'打印机故障了',1,'2015-03-19 14:09:16',NULL,0,2,'打印机故障了','2015-04-01 10:45:56',NULL),(35,4,'我的打印机坏了，麻烦帮忙处理一下。',1,'2015-03-19 16:18:46',NULL,0,3,'狐狸号了。','2015-03-20 14:55:14',NULL),(36,4,'电脑发生了故障，怎么办。',3,'2015-03-21 10:35:45',NULL,0,3,'处理处理','2015-04-01 10:43:20',NULL),(37,4,'楠哥，电脑坏了，帮忙处理下。',3,'2015-03-21 10:59:26',NULL,0,2,'很快收到付款时间的开放就是打了客服就睡了快递费。可就是地方了开始觉得快乐费就是了打开非金属','2015-03-25 14:32:13',NULL),(38,4,'手动阀啊啊啊啊啊啊啊',1,'2015-03-23 10:32:57','2015-03-23 10:33:19',0,2,'手动阀啊啊啊啊啊啊啊','2015-04-01 10:46:02',NULL),(39,4,'      淡淡的淡淡的     ',1,'2015-03-23 10:34:57',NULL,0,2,'处理故障','2015-04-01 15:31:26',220),(40,4,'132',2,'2015-03-23 14:41:20','2015-03-23 15:20:45',0,2,'123456789789456123123456789','2015-03-24 12:23:30',NULL),(41,4,'OMG',1,'2015-03-23 14:47:43',NULL,0,2,'123456789123456789789456123','2015-03-24 12:25:16',NULL),(42,4,'电话故障了，电话故障了。电话故障了，电话故障了。电话故障了，电话故障了。',1,'2015-03-23 15:52:01',NULL,0,2,'sdfsdfsdfsd','2015-04-07 17:58:21',362),(43,4,'方闪电大地快乐减肥了时间到了疯狂送酒店了开发建设。',1,'2015-03-23 16:01:35','2015-03-23 16:02:03',0,2,'士大夫似的','2015-03-23 16:07:54',NULL),(44,4,'水电费对方身份东方闪电',1,'2015-03-23 16:16:59','2015-04-01 17:59:39',0,2,'sdfsafsfwe ','2015-04-07 17:58:39',361),(45,4,'打印机故障了怎么办打印机故障了怎么办打印机故障了怎么办打印机故障了怎么办',1,'2015-03-24 11:23:25',NULL,0,2,'打印机故障了怎么办打印机故障了怎么办打印机故障了怎么办打印机故障了怎么办','2015-04-01 10:46:07',NULL),(46,4,'舞法舞天的',2,'2015-03-24 11:28:23','2015-04-01 11:54:32',0,2,'处理测试','2015-04-11 16:48:36',437),(47,4,'放下屠刀立地成佛',4,'2015-03-24 11:34:07',NULL,0,2,'的就老是觉得分开就睡了多久烦死了就废了时间到了费就是了打开减肥了深刻的家乐福卡仕达睡了快点就疯了快睡觉都浪费快睡觉多了开发。','2015-03-24 15:44:06',NULL),(48,4,'这是啥',1,'2015-03-24 11:43:49',NULL,0,2,'处理。','2015-04-01 15:34:11',195),(49,4,'放弃吧',1,'2015-03-24 11:57:37',NULL,2,NULL,NULL,NULL,NULL),(50,4,'数字游戏',1,'2015-03-24 12:05:26',NULL,2,NULL,NULL,NULL,NULL),(51,4,'淡淡的淡淡的订单上开了',4,'2015-03-24 15:30:46',NULL,2,NULL,NULL,NULL,NULL),(52,4,'速度快发来，看时间的浪费空间按手榴弹达到。',4,'2015-03-24 15:31:02',NULL,2,NULL,NULL,NULL,NULL),(53,4,'上放水电费上放水电费上',4,'2015-03-24 15:45:47',NULL,2,NULL,NULL,NULL,NULL),(54,4,'手动阀就是大家了分开就是打了客服加拉塞克水电费水电费水电费手动阀',1,'2015-03-24 16:29:03',NULL,2,NULL,NULL,NULL,NULL),(55,4,'收到了咖啡就是了。快递就分了',2,'2015-03-25 14:34:14',NULL,2,NULL,NULL,NULL,NULL),(56,4,'就是打开了房间睡多了可减肥了开始',2,'2015-03-25 14:36:06',NULL,0,3,'dsf','2015-04-01 10:42:02',NULL),(57,4,'够二',4,'2015-03-25 14:39:04',NULL,0,2,'阿瑟','2015-04-02 16:52:56',194),(58,4,'考勤、门禁故障需处理。',2,'2015-04-01 17:59:12','2015-04-10 11:29:18',1,NULL,NULL,NULL,NULL),(59,4,'0000000',2,'2015-04-01 18:00:15',NULL,1,NULL,NULL,NULL,NULL),(60,4,'线路故障了。',1,'2015-04-02 15:36:28',NULL,1,NULL,NULL,NULL,NULL),(61,4,'线路失败',1,'2015-04-10 09:35:45',NULL,1,NULL,NULL,NULL,NULL),(62,4,'oa失败',4,'2015-04-10 09:36:03',NULL,1,NULL,NULL,NULL,NULL),(63,4,'电脑故障',3,'2015-04-10 09:36:11',NULL,1,NULL,NULL,NULL,NULL),(64,4,'11111',3,'2015-04-10 09:41:21',NULL,1,NULL,NULL,NULL,NULL),(65,4,'1111111',4,'2015-04-10 09:41:27',NULL,1,NULL,NULL,NULL,NULL),(66,4,'222222',1,'2015-04-10 09:41:33',NULL,1,NULL,NULL,NULL,NULL),(67,4,'333333',3,'2015-04-10 09:41:41',NULL,1,NULL,NULL,NULL,NULL),(68,4,'444444',2,'2015-04-10 09:41:48',NULL,1,NULL,NULL,NULL,NULL),(69,4,'555555',2,'2015-04-10 09:41:56',NULL,1,NULL,NULL,NULL,NULL),(70,4,'555555',1,'2015-04-10 09:42:04',NULL,1,NULL,NULL,NULL,NULL),(71,4,'555555',2,'2015-04-10 09:42:13',NULL,1,NULL,NULL,NULL,NULL),(72,4,'555555',2,'2015-04-10 09:42:19',NULL,1,NULL,NULL,NULL,NULL),(73,4,'444444',3,'2015-04-10 09:42:26',NULL,1,NULL,NULL,NULL,NULL),(74,4,'1111',2,'2015-04-10 09:48:12',NULL,1,NULL,NULL,NULL,NULL),(75,4,'33333',3,'2015-04-10 09:48:17',NULL,1,NULL,NULL,NULL,NULL),(76,4,'33333',1,'2015-04-10 09:48:24',NULL,1,NULL,NULL,NULL,NULL),(77,4,'333333',4,'2015-04-10 09:48:30',NULL,1,NULL,NULL,NULL,NULL),(78,4,'333333',2,'2015-04-10 09:48:35',NULL,1,NULL,NULL,NULL,NULL),(79,4,'333333',3,'2015-04-10 09:48:42',NULL,1,NULL,NULL,NULL,NULL),(80,4,'33333',4,'2015-04-10 09:48:48',NULL,1,NULL,NULL,NULL,NULL),(81,4,'33333',3,'2015-04-10 09:48:53',NULL,1,NULL,NULL,NULL,NULL),(82,4,'33333',1,'2015-04-10 09:48:59',NULL,1,NULL,NULL,NULL,NULL),(83,4,'的东方闪电',2,'2015-04-10 09:49:05',NULL,1,NULL,NULL,NULL,NULL),(84,4,'测试对外线路故障，系统日志记录事件。',1,'2015-04-11 10:25:56',NULL,1,NULL,NULL,NULL,NULL),(85,4,'测试更改包名之后是否发生异常。',1,'2015-04-11 14:55:21',NULL,1,NULL,NULL,NULL,NULL);

#
# Structure for table "com_type"
#

DROP TABLE IF EXISTS `com_type`;
CREATE TABLE `com_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '故障名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新建日期',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='故障类型';

#
# Data for table "com_type"
#

INSERT INTO `com_type` VALUES (1,'对外线路故障','2015-03-31 09:16:22','2015-04-08 10:21:47',NULL),(2,'考勤、门禁故障','2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(3,'电脑故障','2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(4,'OA、ERP故障','2015-03-31 09:16:22','2015-04-02 14:04:20',NULL),(5,'内部网络故障','2015-03-31 09:51:45','2015-04-03 18:04:55',NULL),(6,'测试类型故障','2015-04-10 11:12:24','2015-04-10 11:13:08','2015-04-10 11:12:45');

#
# Structure for table "com_user_type"
#

DROP TABLE IF EXISTS `com_user_type`;
CREATE TABLE `com_user_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COMMENT='运维故障';

#
# Data for table "com_user_type"
#

INSERT INTO `com_user_type` VALUES (36,3,2),(43,3,3),(46,2,1),(47,2,2),(48,2,3),(49,2,4),(50,2,5);

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='权限';

#
# Data for table "sec_permission"
#

INSERT INTO `sec_permission` VALUES (1,'系统管理','P_ADMIN','/admin/**','',0,'2014-12-16 12:22:31','2015-04-08 09:50:09',NULL),(2,'角色管理','P_A_ROLE','/admin/role/**',NULL,1,'2015-03-26 15:13:34','2015-04-08 09:50:11',NULL),(3,'权限管理','P_A_PERMISSION','/admin/permission/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:12',NULL),(4,'用户管理','P_A_USER','/admin/user/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:14',NULL),(5,'工单管理','P_A_ORDER','/admin/order/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:15',NULL),(6,'异常管理','P_A_EXCEPTION','/admin/exception/**',NULL,1,'2015-03-10 12:27:55','2015-04-08 09:50:17',NULL),(7,'运维管理','P_OPERATE','/operate/**',NULL,0,'2015-03-11 10:07:27','2015-04-08 09:50:18',NULL),(8,'故障处理','P_O_DEAL','/operate/deal/**',NULL,7,'2015-03-11 10:12:23','2015-04-08 09:50:19',NULL),(9,'任务分配','P_O_TASK','/operate/task/**',NULL,7,'2015-03-11 10:18:15','2015-04-08 09:50:21',NULL),(10,'报障管理','P_REPORT','/report/**',NULL,0,'2015-03-11 10:13:18','2015-04-08 09:50:22',NULL),(11,'故障申报','P_R_OFFER','/report/offer/**',NULL,10,'2015-03-11 10:16:25','2015-04-08 09:50:23',NULL),(12,'个人管理','P_USER','/user/**',NULL,0,'2015-03-20 12:12:12','2015-04-08 09:50:25',NULL),(13,'个人资料','P_U_CENTER','/user/center/**',NULL,12,'2015-03-20 12:12:47','2015-04-08 09:50:26',NULL),(14,'修改密码','P_U_PWD','/user/pwdPage/**',NULL,12,'2015-03-27 17:52:59','2015-04-08 09:50:27',NULL),(15,'类型管理','P_A_TYPE','/admin/type/**',NULL,1,'2015-03-31 08:58:37','2015-04-08 09:50:28',NULL),(16,'主页','P_INDEX','/index/**',NULL,0,'2015-04-01 18:09:41','2015-04-10 11:05:58',NULL),(17,'主页','P_INDEX','/index/**',NULL,16,'2015-04-01 18:14:28','2015-04-08 09:50:31',NULL),(18,'查询工单','P_I_QUERY','/order/query/**',NULL,16,'2015-04-02 14:05:55','2015-04-08 09:50:33',NULL);

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

INSERT INTO `sec_role` VALUES (1,'超级管理员','R_ADMIN','',0,'2014-12-16 12:22:31','2015-04-02 17:33:36',NULL),(2,'系统管理员','R_MANAGER','',1,'2014-12-16 12:22:31','2015-03-30 15:20:55',NULL),(3,'运维主管','R_DEAL','',0,'2014-12-16 12:22:31','2015-03-30 15:20:54',NULL),(4,'运维员','R_DEALLER',NULL,3,'2015-03-11 09:57:54','2015-04-10 10:53:34',NULL),(5,'报障员','R_OFFER','',0,'2014-12-16 12:22:31','2015-04-10 11:17:27',NULL);

#
# Structure for table "sec_role_permission"
#

DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8 COMMENT='角色权限';

#
# Data for table "sec_role_permission"
#

INSERT INTO `sec_role_permission` VALUES (176,4,8),(177,4,13),(178,4,14),(179,4,17),(180,4,18),(181,4,16),(182,4,7),(183,4,12),(184,3,8),(185,3,9),(186,3,13),(187,3,14),(188,3,17),(189,3,18),(190,3,16),(191,3,7),(192,3,12),(193,1,2),(194,1,3),(195,1,4),(196,1,5),(197,1,6),(198,1,15),(199,1,13),(200,1,14),(201,1,17),(202,1,18),(203,1,16),(204,1,1),(205,1,12),(206,2,4),(207,2,5),(208,2,6),(209,2,15),(210,2,1),(211,5,11),(212,5,13),(213,5,14),(214,5,17),(215,5,18),(216,5,16),(217,5,10),(218,5,12);

#
# Structure for table "sec_syslog"
#

DROP TABLE IF EXISTS `sec_syslog`;
CREATE TABLE `sec_syslog` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL COMMENT '版本',
  `actionenddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'action结束日期',
  `actionendtime` bigint(20) DEFAULT NULL COMMENT 'action结束时间',
  `actionhaoshi` bigint(20) DEFAULT NULL COMMENT 'action耗时',
  `actionstartdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'action开始日期',
  `actionstarttime` bigint(20) DEFAULT NULL COMMENT 'action开始时间',
  `cause` char(1) DEFAULT NULL COMMENT '未知',
  `cookie` varchar(1024) DEFAULT NULL COMMENT 'cookie',
  `description` text COMMENT '描述',
  `enddate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `endtime` bigint(20) DEFAULT NULL,
  `haoshi` bigint(20) DEFAULT NULL COMMENT '耗时',
  `ips` varchar(128) DEFAULT NULL COMMENT 'IPS',
  `method` varchar(4) DEFAULT NULL COMMENT '方法',
  `referer` varchar(500) DEFAULT NULL COMMENT '未知',
  `requestpath` text COMMENT '请求路径',
  `startdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `starttime` bigint(20) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '状态',
  `useragent` varchar(200) DEFAULT NULL COMMENT '用户代理',
  `viewhaoshi` bigint(20) DEFAULT NULL COMMENT '视图耗时',
  `operatorids` bigint(20) DEFAULT NULL COMMENT '操作人员id',
  `accept` varchar(200) DEFAULT NULL,
  `acceptencoding` varchar(200) DEFAULT NULL,
  `acceptlanguage` varchar(200) DEFAULT NULL,
  `connection` varchar(200) DEFAULT NULL,
  `host` varchar(200) DEFAULT NULL COMMENT 'HOST',
  `xrequestedwith` varchar(200) DEFAULT NULL,
  `pvids` bigint(20) DEFAULT NULL COMMENT 'PVid',
  `userids` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';

#
# Data for table "sec_syslog"
#


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
  `remark` varchar(255) DEFAULT NULL COMMENT '保留属性',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户';

#
# Data for table "sec_user"
#

INSERT INTO `sec_user` VALUES (1,'admin','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher',' ','/res/img/头像.jpg','科技','点通','点通科技',NULL,'2015-03-11 10:46:12','2015-04-03 18:05:03',NULL),(2,'deal','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher','','/res/img/头像.jpg','科技','点通','运维主管',NULL,'2015-03-10 14:48:05','2015-04-01 16:57:37',NULL),(3,'dealler','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher',' ','/res/img/头像.jpg','张','三','运维员',NULL,'2015-03-10 15:35:38','2015-04-03 16:40:04',NULL),(4,'reporter','admin','283784513@qq.com','15900088260','$shiro1$SHA-256$500000$evG5MqwWor/BsYb+UagWbQ==$Zhznxwcpp+TRrSbTRp+YEyunOn5ceaa7/1hPA/accQU=','default_hasher','','/res/img/头像.jpg','李','四','小陈',NULL,'2015-03-11 10:40:18','2015-04-07 16:58:56',NULL);

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

INSERT INTO `sec_user_info` VALUES (1,1,1,0,1,'2015-03-11 10:42:24','2015-04-02 17:09:59',NULL),(2,4,2,0,2,'2015-03-13 15:35:03','2015-04-08 11:44:44',NULL),(3,3,3,0,10,'2015-03-16 12:18:29','2015-04-02 17:11:06',NULL),(4,2,4,0,10,'2015-03-16 17:18:17','2015-04-02 17:11:09',NULL);

#
# Structure for table "sec_user_role"
#

DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='用户角色';

#
# Data for table "sec_user_role"
#

INSERT INTO `sec_user_role` VALUES (1,1,1),(2,2,3),(3,3,4),(14,4,5);
