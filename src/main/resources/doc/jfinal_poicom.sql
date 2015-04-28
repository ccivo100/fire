# Host: localhost  (Version: 5.6.23-log)
# Date: 2015-04-28 18:18:38
# Generator: MySQL-Front 5.3  (Build 4.89)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "add_contact"
#

DROP TABLE IF EXISTS `add_contact`;
CREATE TABLE `add_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '提交用户id',
  `name` varchar(20) DEFAULT NULL COMMENT '提交人姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '提交人电话',
  `context` text COMMENT '提交意见',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='建议';

#
# Data for table "add_contact"
#

INSERT INTO `add_contact` VALUES (1,2,'唐东宇','15900088260','建议。','2015-04-23 14:49:55');

#
# Structure for table "add_retrieve"
#

DROP TABLE IF EXISTS `add_retrieve`;
CREATE TABLE `add_retrieve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱地址',
  `random` varchar(50) DEFAULT NULL COMMENT '随机值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='找回密码';

#
# Data for table "add_retrieve"
#

INSERT INTO `add_retrieve` VALUES (1,'2015-04-27 17:06:37','dantechan@poicom.net','D5A4682AD3558F5E9D35D0547E8FD953');

#
# Structure for table "com_apartment"
#

DROP TABLE IF EXISTS `com_apartment`;
CREATE TABLE `com_apartment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='部门';

#
# Data for table "com_apartment"
#

INSERT INTO `com_apartment` VALUES (1,'研发中心',0,'2015-04-18 11:06:39','2015-04-28 12:28:22',NULL),(2,'运维中心',0,'2015-04-18 11:06:47','2015-04-28 12:28:15',NULL),(3,'财务中心',0,'2015-04-20 12:01:44','2015-04-28 12:28:33',NULL),(4,'运维一组',2,'2015-04-28 12:28:01',NULL,NULL),(5,'运维二组',2,'2015-04-28 12:28:12',NULL,NULL),(6,'会计一组',3,'2015-04-28 17:55:55',NULL,NULL),(7,'会计二组',3,'2015-04-28 17:56:06',NULL,NULL);

#
# Structure for table "com_apartment_type"
#

DROP TABLE IF EXISTS `com_apartment_type`;
CREATE TABLE `com_apartment_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apartment_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `type_id` bigint(20) DEFAULT NULL COMMENT '故障类型id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门-故障';

#
# Data for table "com_apartment_type"
#


#
# Structure for table "com_branch"
#

DROP TABLE IF EXISTS `com_branch`;
CREATE TABLE `com_branch` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '单位名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='工作单位';

#
# Data for table "com_branch"
#

INSERT INTO `com_branch` VALUES (1,'中山总公司','2015-03-11 10:42:55',NULL,NULL),(2,'湖南分公司','2015-03-11 10:43:09',NULL,NULL),(3,'重庆公司','2015-03-11 10:43:21',NULL,NULL),(4,'江西分公司','2015-03-11 10:43:32',NULL,NULL),(5,'安徽分公司','2015-03-11 10:43:42',NULL,NULL),(6,'南京分公司','2015-03-11 10:43:53',NULL,NULL),(7,'吉林分公司','2015-03-11 10:44:02',NULL,NULL),(8,'辽宁分公司','2015-03-11 10:44:12','2015-04-08 11:15:26',NULL),(9,'广西分公司','2015-03-11 10:44:22','2015-04-08 11:15:50',NULL);

#
# Structure for table "com_comment"
#

DROP TABLE IF EXISTS `com_comment`;
CREATE TABLE `com_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '工单id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '处理人id',
  `context` text COMMENT '处理意见',
  `add_at` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

#
# Data for table "com_comment"
#


#
# Structure for table "com_level"
#

DROP TABLE IF EXISTS `com_level`;
CREATE TABLE `com_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(5) DEFAULT NULL COMMENT '等级',
  `deadline` int(5) DEFAULT NULL COMMENT '超时期限',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='故障等级';

#
# Data for table "com_level"
#

INSERT INTO `com_level` VALUES (1,'一般',6,'2015-04-16 15:41:23'),(2,'紧急',4,'2015-04-16 15:42:01'),(3,'非常紧急',2,'2015-04-16 15:42:11');

#
# Structure for table "com_order"
#

DROP TABLE IF EXISTS `com_order`;
CREATE TABLE `com_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `offer_user` bigint(11) DEFAULT NULL COMMENT '申报人id',
  `offer_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申报时间',
  `description` text COMMENT '故障描述',
  `type` bigint(11) DEFAULT NULL COMMENT '故障类型id',
  `level` bigint(20) DEFAULT NULL COMMENT '故障等级id',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '工单修改时间',
  `status` int(11) DEFAULT NULL COMMENT '0：已处理 1：未处理 2：超时未处理',
  `accept_user` bigint(20) DEFAULT NULL COMMENT '受理人',
  `accepted_at` timestamp NULL DEFAULT NULL COMMENT '受理人受理时间',
  `deal_user` bigint(11) DEFAULT NULL COMMENT '处理人id',
  `accept_at` timestamp NULL DEFAULT NULL COMMENT '处理人受理时间',
  `comment` text COMMENT '处理意见',
  `deal_at` timestamp NULL DEFAULT NULL COMMENT '处理完成时间',
  `addcomment` text COMMENT '附加意见',
  `add_at` timestamp NULL DEFAULT NULL COMMENT '附加意见时间',
  `spend_time` bigint(20) DEFAULT NULL COMMENT '申报-处理时间差',
  `flag` int(11) DEFAULT NULL COMMENT '催办标志 0 未；1 已',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='工单';

#
# Data for table "com_order"
#

INSERT INTO `com_order` VALUES (3,'测试，运维员处理无指派功能。',4,'2015-04-22 09:24:36','测试，运维员处理无指派功能。',2,2,NULL,0,3,'2015-04-22 09:24:52',3,NULL,'11111111111111111111','2015-04-30 09:10:03',NULL,NULL,50,0,'2015-04-22 09:24:36',NULL),(6,'22222',4,'2015-04-23 10:34:05','222222222',1,2,NULL,0,3,'2015-04-23 10:37:19',3,NULL,'123456 ','2015-04-23 10:55:39','111111111111111111','2015-04-25 01:45:51',NULL,0,'2015-04-23 10:34:05',NULL),(8,'晚饭去哪吃',4,'2015-04-24 15:59:30','今晚去哪吃饭比较好',1,1,NULL,0,3,'2015-04-24 15:59:54',3,NULL,'处理故障。处理故障。处理故障。','2015-04-27 14:05:06',NULL,NULL,70,0,'2015-04-24 15:59:30',NULL),(9,'测试！运维人员角色',4,'2015-04-27 09:18:51','运维人员角色测试',1,1,NULL,0,3,'2015-04-27 09:22:53',3,NULL,'int time=Level.dao.findById(order.get(\"level\")).get(\"deadline\");\n\t\t\tString offer_at=order.get(\"offer_at\").toString();\n\t\t\tDateTime now=DateTime.now();\n\t\t\tint t =DateKit.dateBetween(offer_at, now);\n\t\t\t\n\t\t\t//设置基本内容：处理人，建议，处理时间，修改状态等。\n\t\t\tif(ValidateKit.i','2015-04-27 14:45:25',NULL,NULL,NULL,0,'2015-04-27 09:18:51',NULL),(12,'测试指派功能',4,'2015-04-27 16:20:48','测试指派功能，提交指派申报。',2,3,'2015-04-27 16:20:59',0,2,'2015-04-27 16:57:35',3,'2015-04-28 09:59:09','解决指派功能问题，提交指派申报，解决完毕。','2015-04-28 10:05:09',NULL,NULL,17,0,'2015-04-27 16:20:48',NULL),(14,'测试故障申报内容。',4,'2015-04-28 09:46:21','我是故障申报的主题内容。',1,3,NULL,1,3,'2015-04-28 12:26:40',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-04-28 09:46:21',NULL),(15,'测试故障提交功能。',4,'2015-04-28 09:49:22','我是故障提交主题内容。',1,3,NULL,0,3,'2015-04-28 09:51:41',3,NULL,'提交处理意见，解答提交问题，回答完毕。','2015-04-28 09:55:52','提交处理意见，解答提交问题，我是补充建议，回答完毕。','2015-04-28 09:55:02',NULL,0,'2015-04-28 09:49:22',NULL),(16,'浏览器兼容性测试',4,'2015-04-28 10:26:39','不同版本浏览器兼容性测试',1,3,NULL,1,3,'2015-04-28 14:28:27',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-04-28 10:26:39',NULL),(17,'测试验证提示信息的内容',4,'2015-04-28 10:48:12','测试文本框验证时的提示内容',2,3,NULL,3,3,'2015-04-28 15:07:48',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-04-28 10:48:12',NULL),(18,'测试页码和翻页',4,'2015-04-28 10:49:10','检查是否能够正确选择显示第x 页',3,2,NULL,2,3,'2015-04-28 15:54:04',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-04-28 10:49:10',NULL),(19,'检查组合查询结果是否正确',4,'2015-04-28 10:50:28','检查组合查询时,查询结果是否正确',4,1,NULL,0,3,'2015-04-28 11:06:24',3,NULL,'vh f hfh gfh','2015-04-30 15:55:14',NULL,NULL,NULL,0,'2015-04-28 10:50:28',NULL),(20,'fdg dfg df',4,'2015-04-28 10:59:31','0000000g dfg fdg ',1,1,NULL,0,3,'2015-04-28 11:04:21',3,NULL,'cv xcv xcv xcv xcv ','2015-04-29 15:55:27',NULL,NULL,NULL,0,'2015-04-28 10:59:31',NULL);

#
# Structure for table "com_position"
#

DROP TABLE IF EXISTS `com_position`;
CREATE TABLE `com_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '职位名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='职位';

#
# Data for table "com_position"
#

INSERT INTO `com_position` VALUES (1,'研发总监','2015-04-18 11:08:15','2015-04-20 12:08:30',NULL),(2,'技术总监','2015-04-18 11:21:51',NULL,NULL),(3,'产品总监','2015-04-18 11:22:07',NULL,NULL),(4,'技术员','2015-04-18 11:08:29','2015-04-18 11:21:08',NULL),(5,'工程师','2015-04-18 11:08:45','2015-04-20 12:08:37',NULL),(6,'测试员','2015-04-20 12:19:51',NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='故障类型';

#
# Data for table "com_type"
#

INSERT INTO `com_type` VALUES (1,'对外线路故障','2015-03-31 09:16:22','2015-04-08 10:21:47',NULL),(2,'考勤、门禁故障','2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(3,'电脑故障','2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(4,'OA、ERP故障','2015-03-31 09:16:22','2015-04-02 14:04:20',NULL),(5,'内部网络故障','2015-03-31 09:51:45','2015-04-03 18:04:55',NULL);

#
# Structure for table "com_user_order"
#

DROP TABLE IF EXISTS `com_user_order`;
CREATE TABLE `com_user_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '工单id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='运维-工单';

#
# Data for table "com_user_order"
#

INSERT INTO `com_user_order` VALUES (3,3,3,'2015-04-22 09:24:36',NULL,NULL),(6,3,6,'2015-04-23 10:34:05',NULL,NULL),(8,3,8,'2015-04-24 15:59:30',NULL,NULL),(9,3,9,'2015-04-27 09:18:52',NULL,NULL),(12,3,12,'2015-04-27 16:20:48','2015-04-28 09:57:53',NULL),(14,3,14,'2015-04-28 09:46:21',NULL,NULL),(15,3,15,'2015-04-28 09:49:22',NULL,NULL),(16,3,16,'2015-04-28 10:26:39',NULL,NULL),(17,3,17,'2015-04-28 10:48:12',NULL,NULL),(18,3,18,'2015-04-28 10:49:10',NULL,NULL),(19,3,19,'2015-04-28 10:50:28',NULL,NULL),(20,3,20,'2015-04-28 10:59:32',NULL,NULL);

#
# Structure for table "com_user_type"
#

DROP TABLE IF EXISTS `com_user_type`;
CREATE TABLE `com_user_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='运维故障';

#
# Data for table "com_user_type"
#

INSERT INTO `com_user_type` VALUES (51,2,1),(52,2,2),(53,2,3),(54,2,4),(55,2,5),(56,3,2),(57,3,3),(58,9,4),(59,9,5),(60,3,1),(61,9,1),(62,9,2),(63,9,3),(64,3,4),(65,3,5),(66,11,4),(67,11,5);

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='权限';

#
# Data for table "sec_permission"
#

INSERT INTO `sec_permission` VALUES (1,'系统管理','P_ADMIN','/admin/**','',0,'2014-12-16 12:22:31','2015-04-08 09:50:09',NULL),(2,'角色管理','P_A_ROLE','/admin/role/**',NULL,1,'2015-03-26 15:13:34','2015-04-08 09:50:11',NULL),(3,'权限管理','P_A_PERMISSION','/admin/permission/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:12',NULL),(4,'用户管理','P_A_USER','/admin/user/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:14',NULL),(5,'工单管理','P_A_ORDER','/admin/order/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:15',NULL),(6,'异常管理','P_A_EXCEPTION','/admin/exception/**',NULL,1,'2015-03-10 12:27:55','2015-04-08 09:50:17',NULL),(7,'运维管理','P_OPERATE','/operate/**',NULL,0,'2015-03-11 10:07:27','2015-04-08 09:50:18',NULL),(8,'故障处理','P_O_DEAL','/operate/deal/**',NULL,7,'2015-03-11 10:12:23','2015-04-08 09:50:19',NULL),(9,'任务分配','P_O_TASK','/operate/task/**',NULL,7,'2015-03-11 10:18:15','2015-04-08 09:50:21',NULL),(10,'报障管理','P_REPORT','/report/**',NULL,0,'2015-03-11 10:13:18','2015-04-08 09:50:22',NULL),(11,'故障申报','P_R_OFFER','/report/offer/**',NULL,10,'2015-03-11 10:16:25','2015-04-08 09:50:23',NULL),(12,'个人管理','P_USER','/user/**',NULL,0,'2015-03-20 12:12:12','2015-04-08 09:50:25',NULL),(13,'个人资料','P_U_CENTER','/user/center/**',NULL,12,'2015-03-20 12:12:47','2015-04-08 09:50:26',NULL),(14,'修改密码','P_U_PWD','/user/pwdPage/**',NULL,12,'2015-03-27 17:52:59','2015-04-08 09:50:27',NULL),(15,'类型管理','P_A_TYPE','/admin/type/**',NULL,1,'2015-03-31 08:58:37','2015-04-08 09:50:28',NULL),(16,'主页','P_INDEX','/index/**',NULL,0,'2015-04-01 18:09:41','2015-04-10 11:05:58',NULL),(17,'主页','P_INDEX','/index/**',NULL,16,'2015-04-01 18:14:28','2015-04-08 09:50:31',NULL),(18,'查询工单','P_I_QUERY','/order/query/**',NULL,16,'2015-04-02 14:05:55','2015-04-08 09:50:33',NULL),(19,'提交建议','P_U_CONTACT','/user/contactMe/**',NULL,12,'2015-04-16 10:03:36',NULL,NULL),(20,'故障查询','P_R_REPORTS','/report/reports/**',NULL,10,'2015-04-16 15:47:56','2015-04-17 09:39:15',NULL),(21,'运维记录','P_O_OPERATES','/operate/operates/**',NULL,7,'2015-04-16 15:48:59','2015-04-28 14:50:47',NULL);

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

INSERT INTO `sec_role` VALUES (1,'超级管理员','R_ADMIN','',0,'2014-12-16 12:22:31','2015-04-17 10:33:01',NULL),(2,'系统管理员','R_MANAGER','',1,'2014-12-16 12:22:31','2015-03-30 15:20:55',NULL),(3,'运维主管','R_DEAL','',0,'2014-12-16 12:22:31','2015-03-30 15:20:54',NULL),(4,'运维员','R_DEALLER',NULL,3,'2015-03-11 09:57:54','2015-04-10 10:53:34',NULL),(5,'报障员','R_OFFER','',0,'2014-12-16 12:22:31','2015-04-10 11:17:27',NULL);

#
# Structure for table "sec_role_permission"
#

DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8 COMMENT='角色-权限';

#
# Data for table "sec_role_permission"
#

INSERT INTO `sec_role_permission` VALUES (206,2,4),(207,2,5),(208,2,6),(209,2,15),(210,2,1),(247,1,2),(248,1,3),(249,1,4),(250,1,5),(251,1,6),(252,1,15),(253,1,13),(254,1,14),(255,1,19),(256,1,17),(257,1,18),(258,1,16),(259,1,1),(260,1,12),(261,3,8),(262,3,9),(263,3,21),(264,3,13),(265,3,14),(266,3,19),(267,3,17),(268,3,18),(269,3,16),(270,3,7),(271,3,12),(272,4,8),(273,4,21),(274,4,13),(275,4,14),(276,4,19),(277,4,17),(278,4,18),(279,4,16),(280,4,7),(281,4,12),(282,5,11),(283,5,20),(284,5,13),(285,5,14),(286,5,19),(287,5,17),(288,5,18),(289,5,16),(290,5,10),(291,5,12);

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
  `avatar_url` varchar(255) DEFAULT '/res/img/头像.jpg' COMMENT '头像',
  `first_name` varchar(10) DEFAULT NULL COMMENT '名字',
  `last_name` varchar(10) DEFAULT NULL COMMENT '姓氏',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `remark` varchar(255) DEFAULT NULL COMMENT '保留属性',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户';

#
# Data for table "sec_user"
#

INSERT INTO `sec_user` VALUES (1,'admin','admin','fireterceltong@poicom.net','15900088260','$shiro1$SHA-256$500000$71Yo40JALP+LkOD+xlQHIg==$yzuPXuPtyRNHTgZeFcO1VlYfKvqdRY9zbVpA+2gl/XY=','default_hasher','','/res/img/touxiang.jpg','科技','点通','点通科技',NULL,'2015-03-11 10:46:12','2015-04-24 15:40:05',NULL),(2,'deal','admin','fireterceltong@poicom.net','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher','','/res/img/touxiang.jpg','科技','点通','运维主管',NULL,'2015-03-10 14:48:05','2015-04-24 15:37:28',NULL),(3,'dealler','admin','fireterceltong@poicom.net','15900088260','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher','','/res/img/touxiang.jpg','张','三','运维员',NULL,'2015-03-10 15:35:38','2015-04-28 10:07:06',NULL),(4,'reporter','admin','dantechan@poicom.net','18924518660','$shiro1$SHA-256$500000$evG5MqwWor/BsYb+UagWbQ==$Zhznxwcpp+TRrSbTRp+YEyunOn5ceaa7/1hPA/accQU=','default_hasher','','/res/img/touxiang.jpg','李','四','李四',NULL,'2015-03-11 10:40:18','2015-04-28 10:07:03',NULL),(9,'admin1','点通科技','13846556545@163.com','13846556545','$shiro1$SHA-256$500000$h/5xBbsbcPPU4iQdZnalwQ==$G3KGtt8pP8kPZtxDw19gUf3LoOvqPNzL9yQ9hF8/YE4=','default_hasher','','/res/img/touxiang.jpg','成','龙','成龙',NULL,'2015-04-20 16:55:56','2015-04-24 15:37:29',NULL),(10,'admin2','点通科技','13854545123@163.com','13854545123','$shiro1$SHA-256$500000$ufy/UpKqPPy8DVF++u8qRQ==$LTSuy5393ujxURedCOmMdVDxuqTVialTO8w0QkjKT3Q=','default_hasher','','/res/img/touxiang.jpg','李','小龙','李小龙',NULL,'2015-04-20 16:58:07','2015-04-24 15:40:06',NULL),(11,'黄二','点通科技','13800138111@139.com','13800138111','$shiro1$SHA-256$500000$g8Oe8ZtXxvUffUrZrOAUfA==$E3qAkEIdRQcGelJaTjV4p72wQ99mvDeK3nZPDH9ETb8=','default_hasher','','/res/img/touxiang.jpg','黄','二','黄二',NULL,'2015-04-23 09:23:30','2015-04-24 18:03:52',NULL),(12,'HardyLeung','点通科技','hardyleung@poicom.net','13715657082','$shiro1$SHA-256$500000$U2/Z7Fr6CTjWDsnlZ1tKSQ==$1GVgFr8ne6uuw6EbAWC8YlABq5iNXTrxqAUbOjTWyJ8=','default_hasher','','/res/img/头像.jpg','梁','润河','梁润河',NULL,'2015-04-28 10:48:06',NULL,NULL);

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
  `apartment_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `position_id` bigint(20) DEFAULT NULL COMMENT '职位id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户-信息';

#
# Data for table "sec_user_info"
#

INSERT INTO `sec_user_info` VALUES (1,1,1,0,1,1,1,'2015-03-11 10:42:24','2015-04-18 11:22:18',NULL),(2,4,1,0,2,1,5,'2015-03-13 15:35:03','2015-04-20 14:25:45',NULL),(3,3,1,0,1,2,4,'2015-03-16 12:18:29','2015-04-20 14:25:45',NULL),(4,2,1,0,1,2,2,'2015-03-16 17:18:17','2015-04-20 14:25:47',NULL),(7,9,1,0,2,2,5,'2015-04-20 16:55:56','2015-04-28 10:49:06',NULL),(8,10,1,0,1,1,5,'2015-04-20 16:58:07','2015-04-28 10:49:14',NULL),(9,11,1,0,1,2,4,'2015-04-23 09:23:30','2015-04-24 14:03:59',NULL),(10,12,1,0,1,1,1,'2015-04-28 10:48:06',NULL,NULL);

#
# Structure for table "sec_user_role"
#

DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='用户-角色';

#
# Data for table "sec_user_role"
#

INSERT INTO `sec_user_role` VALUES (1,1,1),(2,2,3),(3,3,4),(14,4,5),(15,7,5),(16,8,5),(18,10,4),(19,9,4),(20,11,4),(21,12,5);
