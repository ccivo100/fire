# Host: localhost  (Version: 5.6.23-log)
# Date: 2015-07-01 18:02:43
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建议';

#
# Data for table "add_contact"
#


#
# Structure for table "add_retrieve"
#

DROP TABLE IF EXISTS `add_retrieve`;
CREATE TABLE `add_retrieve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `username` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱地址',
  `random` varchar(50) DEFAULT NULL COMMENT '随机值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='找回密码';

#
# Data for table "add_retrieve"
#

INSERT INTO `add_retrieve` VALUES (1,'2015-05-05 16:13:59',NULL,'dantechan@poicom.net','403A699F1A0BF357D99E21AFBA4C476B'),(2,'2015-06-19 15:46:48','firetercel','fireterceltong@poicom.net','ED047D5CCE8858860217B18624338AA8'),(3,'2015-06-19 15:48:14','admin','fireterceltong@poicom.net','98D9612C32068ACC3FE54A89ECA3EAE6');

#
# Structure for table "com_alertinfo"
#

DROP TABLE IF EXISTS `com_alertinfo`;
CREATE TABLE `com_alertinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL COMMENT '接收邮箱',
  `emailcontext` text COMMENT '邮件内容',
  `emailrecode` int(11) DEFAULT NULL COMMENT '邮件返回码',
  `phone` varchar(255) DEFAULT NULL COMMENT '接收手机号码',
  `smscontext` text COMMENT '短信内容',
  `smsrecode` int(11) DEFAULT NULL COMMENT '短信返回码',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='短信邮件提醒内容。';

#
# Data for table "com_alertinfo"
#

INSERT INTO `com_alertinfo` VALUES (1,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O开始处理，详情请登陆系统查看。',200,'2015-06-25 15:33:15'),(2,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O开始处理，详情请登陆系统查看。',200,'2015-06-25 15:33:15'),(3,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O转派处理，详情请登陆系统查看。',200,'2015-06-25 15:34:15'),(4,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由运维二组的测试账号O转派处理，详情请登陆系统查看。',200,'2015-06-25 15:34:15'),(5,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：截止位置，工单完成。。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-25 15:36:35'),(6,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：截止位置，工单完成。。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-25 15:36:35'),(7,'fireterceltong@poicom.net','尊敬的唐东宇，你好。<br/>客服部的测试账号R（15900088260）于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：截止位置，工单完成。。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的唐东宇，客服部的测试账号R于2015-06-25 15:31:46，提交关于“故障测试工单”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-25 15:36:35'),(8,'dantechan@poicom.net','尊敬的 陈宇佳3，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：故障测试工单1.0<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”故障测试工单1.0“的故障工单，请尽快处理。',200,'2015-06-29 09:56:19'),(9,'dantechan@poicom.net','尊敬的 陈宇佳，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单1.0<br/>故障内容为：故障测试工单1.0<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单1.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:56:19'),(10,'fireterceltong@poicom.net','尊敬的 测试账号R，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单1.0<br/>故障内容为：故障测试工单1.0<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单1.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:56:19'),(11,'dantechan@poicom.net','尊敬的 测试账号O，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：故障测试工单1.0<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”故障测试工单1.0“的故障工单，请尽快处理。',200,'2015-06-29 09:56:19'),(12,'dantechan@poicom.net','尊敬的 陈宇佳1，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单1.0<br/>故障内容为：故障测试工单1.0<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单1.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:56:22'),(13,'dantechan@poicom.net','【撤回通知】尊敬的 测试账号O 您好，中山总公司 的 陈宇佳（18924518660）撤回于 2015-06-29 09:55:51 发起的故障申告！',NULL,'(\'18924518660\')','您好，中山总公司 的 陈宇佳（18924518660）撤回于 2015-06-29 09:55:51 发起的故障申告！',200,'2015-06-29 09:56:44'),(14,'dantechan@poicom.net','【撤回通知】尊敬的 陈宇佳3 您好，中山总公司 的 陈宇佳（18924518660）撤回于 2015-06-29 09:55:51 发起的故障申告！',NULL,'(\'18924518660\')','您好，中山总公司 的 陈宇佳（18924518660）撤回于 2015-06-29 09:55:51 发起的故障申告！',200,'2015-06-29 09:56:45'),(15,'fireterceltong@poicom.net','尊敬的 测试账号R，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单2.0<br/>故障内容为：故障测试工单2.0<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单2.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:58:00'),(16,'dantechan@poicom.net','尊敬的 测试账号O，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：故障测试工单2.0<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”故障测试工单2.0“的故障工单，请尽快处理。',200,'2015-06-29 09:58:00'),(17,'dantechan@poicom.net','尊敬的 陈宇佳1，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单2.0<br/>故障内容为：故障测试工单2.0<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单2.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:58:01'),(18,'dantechan@poicom.net','尊敬的 陈宇佳，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：故障测试工单2.0<br/>故障内容为：故障测试工单2.0<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”故障测试工单2.0“的故障工单，详情请登陆系统查看。',200,'2015-06-29 09:58:01'),(19,'dantechan@poicom.net','尊敬的 陈宇佳3，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：故障测试工单2.0<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”故障测试工单2.0“的故障工单，请尽快处理。',200,'2015-06-29 09:58:03'),(20,'dantechan@poicom.net','尊敬的陈宇佳，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理，详情请登陆系统查看。',200,'2015-06-29 10:02:35'),(21,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理，详情请登陆系统查看。',200,'2015-06-29 10:02:37'),(22,'dantechan@poicom.net','尊敬的陈宇佳3，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳3，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理，详情请登陆系统查看。',200,'2015-06-29 10:02:37'),(23,'dantechan@poicom.net','尊敬的陈宇佳1，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳1，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理，详情请登陆系统查看。',200,'2015-06-29 10:02:37'),(24,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理。<br/>处理意见为：开始处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3开始处理，详情请登陆系统查看。',200,'2015-06-29 10:02:38'),(25,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理。<br/>处理意见为：继续处理。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理，详情请登陆系统查看。',200,'2015-06-29 10:03:56'),(26,'dantechan@poicom.net','尊敬的陈宇佳，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理。<br/>处理意见为：继续处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理，详情请登陆系统查看。',200,'2015-06-29 10:03:56'),(27,'dantechan@poicom.net','尊敬的陈宇佳1，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理。<br/>处理意见为：继续处理。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳1，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3继续处理，详情请登陆系统查看。',200,'2015-06-29 10:03:56'),(28,'dantechan@poicom.net','尊敬的陈宇佳，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理，详情请登陆系统查看。',200,'2015-06-29 10:07:37'),(29,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理，详情请登陆系统查看。',200,'2015-06-29 10:07:37'),(30,'fireterceltong@poicom.net','尊敬的唐东宇，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的唐东宇，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理，详情请登陆系统查看。',200,'2015-06-29 10:07:38'),(31,'dantechan@poicom.net','尊敬的陈宇佳3，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理。<br/>处理意见为：工单转派。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳3，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由运维二组的陈宇佳3转派处理，详情请登陆系统查看。',200,'2015-06-29 10:07:38'),(32,'dantechan@poicom.net','尊敬的陈宇佳，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:53'),(33,'fireterceltong@poicom.net','尊敬的测试账号R，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的测试账号R，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:53'),(34,'dantechan@poicom.net','尊敬的陈宇佳1，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳1，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:53'),(35,'dantechan@poicom.net','尊敬的测试账号O，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的测试账号O，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:53'),(36,'dantechan@poicom.net','尊敬的陈宇佳3，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','尊敬的陈宇佳3，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:54'),(37,'fireterceltong@poicom.net','尊敬的唐东宇，你好。<br/>客服部的陈宇佳（18924518660）于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕。<br/>处理意见为：finally.。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','尊敬的唐东宇，客服部的陈宇佳于2015-06-29 09:57:57，提交关于“故障测试工单2.0”的故障单。现由培训部的唐东宇处理完毕，详情请登陆系统查看。',200,'2015-06-29 10:10:54'),(38,'fireterceltong@poicom.net','尊敬的 唐东宇，您好：<br/>贵部门的唐东宇 （15900088260）提交了故障工单。<br/>标题为：测试运维部门选择多个<br/>故障内容为：测试运维部门选择多个。<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','您好，贵部门的唐东宇（15900088260） 提交了关于”测试运维部门选择多个“的故障工单，详情请登陆系统查看。',200,'2015-07-01 11:38:52'),(39,'dantechan@poicom.net','尊敬的 陈宇佳2，您好：<br/>中山总公司 的 唐东宇 （15900088260）发来故障工单。<br/>故障内容为：测试运维部门选择多个。<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 唐东宇（15900088260） 提交了关于”测试运维部门选择多个“的故障工单，请尽快处理。',200,'2015-07-01 11:38:54'),(40,'fireterceltong@poicom.net','尊敬的 唐东宇，您好：<br/>中山总公司 的 唐东宇 （15900088260）发来故障工单。<br/>故障内容为：测试运维部门选择多个。<br/>请及时处理。',NULL,'(\'15900088260\')','您好，中山总公司的 唐东宇（15900088260） 提交了关于”测试运维部门选择多个“的故障工单，请尽快处理。',200,'2015-07-01 11:38:55'),(41,'dantechan@poicom.net','尊敬的 测试账号O，您好：<br/>中山总公司 的 唐东宇 （15900088260）发来故障工单。<br/>故障内容为：测试运维部门选择多个。<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 唐东宇（15900088260） 提交了关于”测试运维部门选择多个“的故障工单，请尽快处理。',200,'2015-07-01 11:38:56'),(42,'dantechan@poicom.net','尊敬的 陈宇佳3，您好：<br/>中山总公司 的 唐东宇 （15900088260）发来故障工单。<br/>故障内容为：测试运维部门选择多个。<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 唐东宇（15900088260） 提交了关于”测试运维部门选择多个“的故障工单，请尽快处理。',200,'2015-07-01 11:38:57'),(43,'dantechan@poicom.net','尊敬的 陈宇佳2，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：测试-部门多选<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，请尽快处理。',200,'2015-07-01 12:14:03'),(44,'dantechan@poicom.net','尊敬的 测试账号O，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：测试-部门多选<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，请尽快处理。',200,'2015-07-01 12:14:03'),(45,'fireterceltong@poicom.net','尊敬的 管理员，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：测试-部门多选<br/>请及时处理。',NULL,'(\'15900088260\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，请尽快处理。',200,'2015-07-01 12:14:03'),(46,'fireterceltong@poicom.net','尊敬的 测试账号R，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：测试-部门多选<br/>故障内容为：测试-部门多选<br/>详情请登陆系统查看。',NULL,'(\'15900088260\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，详情请登陆系统查看。',200,'2015-07-01 12:14:03'),(47,'dantechan@poicom.net','尊敬的 陈宇佳3，您好：<br/>中山总公司 的 陈宇佳 （18924518660）发来故障工单。<br/>故障内容为：测试-部门多选<br/>请及时处理。',NULL,'(\'18924518660\')','您好，中山总公司的 陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，请尽快处理。',200,'2015-07-01 12:14:03'),(48,'dantechan@poicom.net','尊敬的 陈宇佳1，您好：<br/>贵部门的陈宇佳 （18924518660）提交了故障工单。<br/>标题为：测试-部门多选<br/>故障内容为：测试-部门多选<br/>详情请登陆系统查看。',NULL,'(\'18924518660\')','您好，贵部门的陈宇佳（18924518660） 提交了关于”测试-部门多选“的故障工单，详情请登陆系统查看。',200,'2015-07-01 12:14:03');

#
# Structure for table "com_apartment"
#

DROP TABLE IF EXISTS `com_apartment`;
CREATE TABLE `com_apartment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点',
  `level` bigint(20) DEFAULT NULL COMMENT '层级',
  `image` varchar(50) DEFAULT NULL COMMENT '图标url',
  `orderid` int(11) DEFAULT NULL COMMENT '排序',
  `flag` char(1) DEFAULT NULL COMMENT '处理标示',
  `remark` varchar(255) DEFAULT NULL COMMENT '保留字段',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='部门';

#
# Data for table "com_apartment"
#

INSERT INTO `com_apartment` VALUES (1,'研发中心',0,NULL,NULL,NULL,'1',NULL,'2015-04-18 11:06:39','2015-04-30 12:30:54',NULL),(2,'运维中心',0,NULL,NULL,NULL,'1',NULL,'2015-04-18 11:06:47','2015-04-30 12:30:53',NULL),(3,'财务中心',0,NULL,NULL,NULL,'1',NULL,'2015-04-20 12:01:44','2015-04-30 12:30:49',NULL),(4,'运维一组',2,NULL,NULL,NULL,'0',NULL,'2015-04-28 12:28:01','2015-04-30 12:30:52',NULL),(5,'运维二组',2,NULL,NULL,NULL,'0',NULL,'2015-04-28 12:28:12','2015-04-30 12:30:51',NULL),(6,'会计一组',3,NULL,NULL,NULL,'0',NULL,'2015-04-28 17:55:55','2015-04-30 12:38:18',NULL),(7,'会计二组',3,NULL,NULL,NULL,'0',NULL,'2015-04-28 17:56:06',NULL,NULL),(8,'人资行政中心',0,NULL,NULL,NULL,'1',NULL,'2015-04-30 12:39:13',NULL,NULL),(9,'研发部',1,NULL,NULL,NULL,'0',NULL,'2015-04-30 14:47:24',NULL,NULL),(10,'培训部',8,NULL,NULL,NULL,'0',NULL,'2015-05-04 11:13:49','2015-05-04 11:23:02',NULL),(11,'客服中心',0,NULL,NULL,NULL,'1',NULL,'2015-05-18 14:10:51',NULL,NULL),(12,'客服部',11,NULL,NULL,NULL,'0',NULL,'2015-05-18 14:11:25',NULL,NULL),(13,'客服部2',11,NULL,NULL,NULL,'0',NULL,'2015-06-08 11:48:02',NULL,NULL),(14,'产品中心',0,NULL,NULL,NULL,'0',NULL,'2015-06-10 10:33:13',NULL,NULL),(15,'培训部1',8,NULL,NULL,NULL,'0',NULL,'2015-06-19 17:44:18',NULL,NULL);

#
# Structure for table "com_apartment_type"
#

DROP TABLE IF EXISTS `com_apartment_type`;
CREATE TABLE `com_apartment_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apartment_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `type_id` bigint(20) DEFAULT NULL COMMENT '故障类型id',
  `level` char(1) DEFAULT NULL COMMENT '关联级别',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8 COMMENT='部门-故障';

#
# Data for table "com_apartment_type"
#

INSERT INTO `com_apartment_type` VALUES (31,2,2,'1','2015-06-10 16:38:58',NULL,NULL),(32,4,6,'2','2015-06-10 16:39:04',NULL,NULL),(33,4,16,'2','2015-06-10 16:39:04',NULL,NULL),(34,5,16,'2','2015-06-10 16:39:09',NULL,NULL),(37,2,11,'1','2015-06-11 11:44:50',NULL,NULL),(43,8,1,'1','2015-06-12 12:26:17',NULL,NULL),(44,8,2,'1','2015-06-12 12:26:17',NULL,NULL),(45,8,3,'1','2015-06-12 12:26:17',NULL,NULL),(46,8,4,'1','2015-06-12 12:26:17',NULL,NULL),(47,8,5,'1','2015-06-12 12:26:17',NULL,NULL),(48,8,10,'1','2015-06-12 12:26:17',NULL,NULL),(49,8,11,'1','2015-06-12 12:26:17',NULL,NULL),(50,8,12,'1','2015-06-12 12:26:17',NULL,NULL),(51,8,13,'1','2015-06-12 12:26:17',NULL,NULL),(52,10,8,'2','2015-06-12 12:26:23',NULL,NULL),(53,10,9,'2','2015-06-12 12:26:23',NULL,NULL),(54,10,7,'2','2015-06-12 12:26:23',NULL,NULL),(55,10,14,'2','2015-06-12 12:26:29',NULL,NULL),(56,10,15,'2','2015-06-12 12:26:29',NULL,NULL),(57,10,6,'2','2015-06-12 12:26:29',NULL,NULL),(58,10,16,'2','2015-06-12 12:26:29',NULL,NULL),(59,2,1,'1','2015-06-12 12:29:00',NULL,NULL),(60,2,3,'1','2015-06-12 12:29:00',NULL,NULL),(61,2,4,'1','2015-06-12 12:29:00',NULL,NULL),(62,2,5,'1','2015-06-12 12:29:00',NULL,NULL),(63,2,10,'1','2015-06-12 12:29:00',NULL,NULL),(64,2,12,'1','2015-06-12 12:29:00',NULL,NULL),(65,2,13,'1','2015-06-12 12:29:00',NULL,NULL),(66,5,6,'2','2015-06-12 12:29:11',NULL,NULL),(67,5,8,'2','2015-06-12 12:29:11',NULL,NULL),(68,5,9,'2','2015-06-12 12:29:11',NULL,NULL),(69,5,14,'2','2015-06-12 12:29:11',NULL,NULL),(70,5,7,'2','2015-06-12 12:29:11',NULL,NULL),(71,5,15,'2','2015-06-12 12:29:15',NULL,NULL),(72,4,8,'2','2015-06-12 12:29:23',NULL,NULL),(73,4,9,'2','2015-06-12 12:29:23',NULL,NULL),(74,4,14,'2','2015-06-12 12:29:23',NULL,NULL),(75,4,15,'2','2015-06-12 12:29:23',NULL,NULL),(76,4,7,'2','2015-06-12 12:29:23',NULL,NULL),(100,1,1,'1','2015-06-12 14:13:56',NULL,NULL),(101,1,2,'1','2015-06-12 14:13:56',NULL,NULL),(102,9,8,'2','2015-06-12 14:14:09',NULL,NULL),(103,9,9,'2','2015-06-12 14:14:09',NULL,NULL),(104,9,14,'2','2015-06-12 14:14:09',NULL,NULL),(105,9,15,'2','2015-06-12 14:14:09',NULL,NULL),(106,9,6,'2','2015-06-12 14:14:09',NULL,NULL),(107,9,16,'2','2015-06-12 14:14:09',NULL,NULL);

#
# Structure for table "com_branch"
#

DROP TABLE IF EXISTS `com_branch`;
CREATE TABLE `com_branch` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '单位名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `apartment_ids` text COMMENT '分配的部门ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='工作单位';

#
# Data for table "com_branch"
#

INSERT INTO `com_branch` VALUES (1,'中山总公司','2015-03-11 10:42:55','2015-05-04 11:12:44',NULL,NULL),(2,'湖南分公司','2015-03-11 10:43:09','2015-05-04 11:15:00',NULL,NULL),(3,'重庆公司','2015-03-11 10:43:21',NULL,NULL,NULL),(4,'江西分公司','2015-03-11 10:43:32',NULL,NULL,NULL),(5,'安徽分公司','2015-03-11 10:43:42',NULL,NULL,NULL),(6,'南京分公司','2015-03-11 10:43:53',NULL,NULL,NULL),(7,'吉林分公司','2015-03-11 10:44:02',NULL,NULL,NULL),(8,'辽宁分公司','2015-03-11 10:44:12','2015-04-08 11:15:26',NULL,NULL),(9,'广西分公司','2015-03-11 10:44:22','2015-05-04 11:22:55',NULL,NULL);

#
# Structure for table "com_branch_apartment"
#

DROP TABLE IF EXISTS `com_branch_apartment`;
CREATE TABLE `com_branch_apartment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) DEFAULT NULL COMMENT '单位id',
  `apartment_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单位-部门关联表';

#
# Data for table "com_branch_apartment"
#


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
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='评论';

#
# Data for table "com_comment"
#

INSERT INTO `com_comment` VALUES (14,56,4,'转派运维工单','2015-06-12 16:09:16','2015-06-12 16:10:10',NULL,NULL),(15,56,7,'转派给运维中心处理。','2015-06-12 16:44:49','2015-06-12 16:45:01',NULL,NULL),(16,60,2,'测试结点','2015-06-19 17:15:16','2015-06-19 17:15:50',NULL,NULL),(17,61,2,'故障测试工单，结点','2015-06-19 17:44:58','2015-06-19 17:45:37',NULL,NULL),(18,62,2,'测试回复时通报申报人所在部门的所有人','2015-06-23 11:10:39','2015-06-23 11:11:18',NULL,NULL),(19,63,7,'处理完毕。','2015-06-23 11:32:41','2015-06-23 11:32:50',NULL,NULL),(21,64,2,'开始处理工单。','2015-06-24 16:26:34','2015-06-24 16:26:42',NULL,NULL),(22,64,2,'继续处理工单。','2015-06-24 16:34:12','2015-06-24 16:34:22',NULL,NULL),(23,64,7,'测试数据获取。','2015-06-24 16:40:58','2015-06-24 16:41:20',NULL,NULL),(24,64,2,'提交处理意见。','2015-06-24 16:50:46','2015-06-24 16:51:44',NULL,NULL),(25,64,2,'测试测试测试。','2015-06-24 16:56:02','2015-06-24 16:56:11',NULL,NULL),(26,64,2,'处理结束。','2015-06-24 17:01:29','2015-06-24 17:01:33',NULL,NULL),(27,65,2,'测试123456','2015-06-24 17:52:56','2015-06-24 17:53:49',NULL,NULL),(28,65,2,'测试123456','2015-06-24 17:56:02','2015-06-24 17:58:50',NULL,NULL),(29,58,7,'验证一下。','2015-06-25 11:15:29','2015-06-25 11:15:46',NULL,NULL),(30,68,2,'开始处理','2015-06-25 15:32:24','2015-06-25 15:33:09',NULL,NULL),(31,68,2,'工单转派','2015-06-25 15:33:06','2015-06-25 15:34:08',NULL,NULL),(32,68,7,'截止位置，工单完成。','2015-06-25 15:36:17','2015-06-25 15:36:31',NULL,NULL),(33,70,67,'开始处理','2015-06-29 10:01:25','2015-06-29 10:02:32',NULL,NULL),(34,70,67,'继续处理','2015-06-29 10:02:50','2015-06-29 10:03:53',NULL,NULL),(35,70,67,'工单转派','2015-06-29 10:06:21','2015-06-29 10:07:33',NULL,NULL),(36,70,7,'finally.','2015-06-29 10:10:35','2015-06-29 10:10:48',NULL,NULL);

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
  `offer_at` timestamp NULL DEFAULT NULL COMMENT '申报时间',
  `description` text COMMENT '故障描述',
  `type` bigint(11) DEFAULT NULL COMMENT '故障类型id',
  `level` bigint(20) DEFAULT NULL COMMENT '故障等级id',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '工单修改时间',
  `status` int(11) DEFAULT NULL COMMENT '0：已处理 1：处理中 2：未处理',
  `spend_time` bigint(20) DEFAULT NULL COMMENT '申报-处理时间差',
  `flag` int(11) DEFAULT NULL COMMENT '催办标志 0 未；1 已',
  `created_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 COMMENT='工单';

#
# Data for table "com_order"
#

INSERT INTO `com_order` VALUES (56,'测试，发送给人资工单',7,'2015-06-12 16:08:25','测试，发送给人资工单。',16,2,NULL,1,NULL,0,NULL,NULL),(57,'故障工单测试-运维二组',4,'2015-06-12 16:16:16','故障工单测试-运维二组',8,1,NULL,2,NULL,0,NULL,NULL),(58,'dantechan',4,'2015-06-12 16:18:08','dantechan',8,1,NULL,1,NULL,0,NULL,NULL),(59,'故障工单运维测试123',4,'2015-06-16 17:47:42','故障工单运维测试123',6,1,NULL,2,NULL,0,NULL,NULL),(60,'故障测试工单',4,'2015-06-19 17:15:27','测试结点',8,1,NULL,0,NULL,0,NULL,NULL),(61,'故障测试工单，结点',4,'2015-06-19 17:45:11','故障测试工单，结点',8,1,NULL,0,NULL,0,NULL,NULL),(62,'测试回复时通报申报人所在部门的所有人',4,'2015-06-23 11:10:29','测试回复时通报申报人所在部门的所有人',14,2,NULL,0,NULL,0,NULL,NULL),(63,'测试回复时通报申报人所在部门的所有人1',4,'2015-06-23 11:29:31','测试回复时通报申报人所在部门的所有人1',9,2,NULL,0,NULL,0,NULL,NULL),(64,'发送工单给运维2组',7,'2015-06-24 16:21:01','发送工单给运维2组',8,1,NULL,0,NULL,0,NULL,NULL),(65,'测试123456',3,'2015-06-24 17:51:05','测试123456',8,2,NULL,1,NULL,0,NULL,NULL),(66,'测试短信内容',7,'2015-06-25 12:00:54','测试短信内容',8,1,NULL,2,NULL,0,NULL,NULL),(67,'发送工单给运维二组，培训部1也收到通知。',7,'2015-06-25 14:32:27','发送工单给运维二组，培训部1也收到通知。',8,1,NULL,2,NULL,0,NULL,'2015-06-25 15:09:16'),(68,'故障测试工单',3,'2015-06-25 15:31:46','测试内容',8,3,NULL,0,NULL,0,NULL,NULL),(69,'故障测试工单1.0',4,'2015-06-29 09:55:51','故障测试工单1.0',9,1,NULL,2,NULL,0,NULL,'2015-06-29 09:56:41'),(70,'故障测试工单2.0',4,'2015-06-29 09:57:57','故障测试工单2.0',8,3,NULL,0,NULL,0,NULL,NULL),(72,'测试运维部门选择多个',7,'2015-07-01 11:38:01','测试运维部门选择多个。',8,1,NULL,2,NULL,0,NULL,NULL),(73,'测试-部门多选',4,'2015-07-01 12:13:53','测试-部门多选',8,1,NULL,2,NULL,0,NULL,NULL);

#
# Structure for table "com_position"
#

DROP TABLE IF EXISTS `com_position`;
CREATE TABLE `com_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '职位名称',
  `apartment_id` bigint(20) DEFAULT NULL COMMENT '所属部门id',
  `apartment_name` varchar(50) DEFAULT NULL COMMENT '所属部门名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='职位';

#
# Data for table "com_position"
#

INSERT INTO `com_position` VALUES (1,'研发总监',1,'研发中心',NULL,'2015-04-18 11:08:15','2015-04-20 12:08:30',NULL),(2,'技术总监',14,'产品中心',NULL,'2015-04-18 11:21:51',NULL,NULL),(3,'产品总监',14,'产品中心',NULL,'2015-04-18 11:22:07',NULL,NULL),(4,'技术员',2,'运维中心',NULL,'2015-04-18 11:08:29','2015-04-18 11:21:08',NULL),(5,'工程师',1,'研发中心',NULL,'2015-04-18 11:08:45','2015-04-20 12:08:37',NULL),(6,'测试员',14,'产品中心',NULL,'2015-04-20 12:19:51',NULL,NULL),(7,'研发总监',1,'研发中心',NULL,'2015-05-22 12:40:26',NULL,NULL),(8,'研发总监',1,'研发中心',NULL,'2015-05-22 12:41:08',NULL,'2015-06-10 11:53:16'),(9,'AAA',1,'研发中心',NULL,'2015-06-08 12:17:34',NULL,'2015-06-10 11:07:48'),(10,'程序猿',1,'研发中心',NULL,'2015-06-10 11:11:41',NULL,NULL),(11,'人力资源专员',8,'人资行政中心',NULL,'2015-06-10 11:17:39',NULL,NULL),(12,'码农',1,'研发中心',NULL,'2015-06-10 11:37:52',NULL,NULL),(13,'客服',11,'客服中心',NULL,'2015-06-24 17:48:41',NULL,NULL);

#
# Structure for table "com_type"
#

DROP TABLE IF EXISTS `com_type`;
CREATE TABLE `com_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '故障名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父类id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新建日期',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '更新日期',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='故障类型';

#
# Data for table "com_type"
#

INSERT INTO `com_type` VALUES (1,'一点通平台故障',0,'2015-03-31 09:16:22','2015-04-08 10:21:47',NULL),(2,'短信平台故障',0,'2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(3,'豪讯手机问题',0,'2015-03-31 09:16:22','2015-04-02 14:04:19',NULL),(4,'易百年手机问题',0,'2015-03-31 09:16:22','2015-04-02 14:04:20',NULL),(5,'通话问题',0,'2015-03-31 09:51:45','2015-04-03 18:04:55',NULL),(6,'短信平台无法登陆',2,'2015-05-05 11:06:37',NULL,NULL),(7,'通话质量问题',5,'2015-06-05 15:40:02',NULL,NULL),(8,'平台无法登陆',1,'2015-06-09 16:13:11',NULL,NULL),(9,'平台退出异常',1,'2015-06-09 16:13:28',NULL,NULL),(10,'老人桌面、子女端问题',0,'2015-06-09 16:14:57',NULL,NULL),(11,'激活问题',0,'2015-06-09 16:15:27',NULL,NULL),(12,'电脑硬件问题',0,'2015-06-09 16:15:35',NULL,NULL),(13,'手机卡欠费问题',0,'2015-06-09 16:15:42',NULL,NULL),(14,'平台电话不能正常呼出',1,'2015-06-09 16:16:06',NULL,NULL),(15,'平台听录音异常',1,'2015-06-09 16:16:20',NULL,NULL),(16,'短信平台导入短信名单失败',2,'2015-06-09 16:16:33',NULL,NULL);

#
# Structure for table "com_user_order"
#

DROP TABLE IF EXISTS `com_user_order`;
CREATE TABLE `com_user_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '工单id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8 COMMENT='运维-工单';

#
# Data for table "com_user_order"
#

INSERT INTO `com_user_order` VALUES (106,4,56,'2015-06-12 16:08:25',NULL,NULL),(109,2,57,'2015-06-12 16:16:16',NULL,NULL),(110,7,57,'2015-06-12 16:16:16',NULL,NULL),(111,2,58,'2015-06-12 16:18:08',NULL,NULL),(112,7,58,'2015-06-12 16:18:08',NULL,NULL),(113,2,56,'2015-06-12 16:45:02',NULL,NULL),(114,7,56,'2015-06-12 16:45:02',NULL,NULL),(115,2,59,'2015-06-16 17:47:42',NULL,NULL),(116,7,59,'2015-06-16 17:47:42',NULL,NULL),(117,2,60,'2015-06-19 17:15:27',NULL,NULL),(118,2,61,'2015-06-19 17:45:11',NULL,NULL),(119,2,62,'2015-06-23 11:10:29',NULL,NULL),(120,7,63,'2015-06-23 11:29:31',NULL,NULL),(121,62,63,'2015-06-23 11:29:31',NULL,NULL),(122,2,64,'2015-06-24 16:21:01',NULL,NULL),(123,2,65,'2015-06-24 17:51:05',NULL,NULL),(124,2,66,'2015-06-25 12:00:54',NULL,NULL),(125,2,67,'2015-06-25 14:32:27',NULL,NULL),(126,2,68,'2015-06-25 15:31:46',NULL,NULL),(127,7,68,'2015-06-25 15:34:08',NULL,NULL),(128,2,69,'2015-06-29 09:55:51',NULL,NULL),(129,67,69,'2015-06-29 09:55:51',NULL,NULL),(130,2,70,'2015-06-29 09:57:57',NULL,NULL),(131,67,70,'2015-06-29 09:57:57',NULL,NULL),(132,7,70,'2015-06-29 10:07:33',NULL,NULL),(133,66,72,'2015-07-01 11:38:50',NULL,NULL),(134,2,72,'2015-07-01 11:38:52',NULL,NULL),(135,67,72,'2015-07-01 11:38:52',NULL,NULL),(136,7,72,'2015-07-01 11:38:53',NULL,NULL),(137,1,73,'2015-07-01 12:13:53',NULL,NULL),(138,66,73,'2015-07-01 12:13:53',NULL,NULL),(139,2,73,'2015-07-01 12:13:53',NULL,NULL),(140,67,73,'2015-07-01 12:13:53',NULL,NULL),(141,7,73,'2015-07-01 12:13:53',NULL,NULL);

#
# Structure for table "com_user_type"
#

DROP TABLE IF EXISTS `com_user_type`;
CREATE TABLE `com_user_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运维故障';

#
# Data for table "com_user_type"
#


#
# Structure for table "sec_operator"
#

DROP TABLE IF EXISTS `sec_operator`;
CREATE TABLE `sec_operator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `description` varchar(20) DEFAULT NULL COMMENT '功能描述',
  `name` varchar(25) DEFAULT NULL COMMENT '功能名称',
  `onemany` char(1) DEFAULT NULL COMMENT '一对多？',
  `returnparamkeys` varchar(100) DEFAULT NULL COMMENT '返回参数key',
  `returnurl` varchar(200) DEFAULT NULL COMMENT '返回参数url',
  `rowfilter` char(1) DEFAULT NULL COMMENT '行级过滤',
  `url` varchar(200) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  `permissionname` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `splitpage` char(1) DEFAULT NULL COMMENT '分页？',
  `formtoken` char(1) DEFAULT NULL COMMENT '重复提交？',
  `ipblack` char(1) DEFAULT NULL,
  `privilegess` char(1) DEFAULT NULL COMMENT '授权',
  `ispv` char(1) DEFAULT NULL,
  `pvtype` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能列表';

#
# Data for table "sec_operator"
#


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
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='权限';

#
# Data for table "sec_permission"
#

INSERT INTO `sec_permission` VALUES (1,'系统管理','P_ADMIN','/admin/**','',0,'2014-12-16 12:22:31','2015-04-08 09:50:09',NULL),(2,'角色管理','P_A_ROLE','/admin/role/**',NULL,1,'2015-03-26 15:13:34','2015-04-08 09:50:11',NULL),(3,'权限管理','P_A_PERMISSION','/admin/permission/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:12',NULL),(4,'用户管理','P_A_USER','/admin/user/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:14',NULL),(5,'单位管理','P_A_BRANCH','/admin/branch/**','',1,'2014-12-16 12:22:31','2015-04-08 09:50:15',NULL),(6,'部门管理','P_A_APARTMENT','/admin/apartment/**',NULL,1,'2015-03-10 12:27:55','2015-04-08 09:50:17',NULL),(7,'运维管理','P_OPERATE','/operate/**',NULL,0,'2015-03-11 10:07:27','2015-04-08 09:50:18',NULL),(8,'故障处理','P_O_DEAL','/operate/deal/**',NULL,7,'2015-03-11 10:12:23','2015-04-08 09:50:19',NULL),(9,'运维记录','P_O_OPERATES','/operate/operates/**',NULL,7,'2015-04-16 15:48:59','2015-04-29 16:58:55',NULL),(10,'报障管理','P_REPORT','/report/**',NULL,0,'2015-03-11 10:13:18','2015-04-08 09:50:22',NULL),(11,'故障申报','P_R_OFFER','/report/offer/**',NULL,10,'2015-03-11 10:16:25','2015-04-08 09:50:23',NULL),(12,'个人管理','P_USER','/user/**',NULL,0,'2015-03-20 12:12:12','2015-04-08 09:50:25',NULL),(13,'个人资料','P_U_CENTER','/user/center/**',NULL,12,'2015-03-20 12:12:47','2015-04-08 09:50:26',NULL),(14,'修改密码','P_U_PWD','/user/pwdPage/**',NULL,12,'2015-03-27 17:52:59','2015-04-08 09:50:27',NULL),(15,'类型管理','P_A_TYPE','/admin/type/**',NULL,1,'2015-03-31 08:58:37','2015-04-08 09:50:28',NULL),(16,'主页','P_INDEX','/index/**',NULL,0,'2015-04-01 18:09:41','2015-04-10 11:05:58',NULL),(17,'主页','P_INDEX','/index/**',NULL,16,'2015-04-01 18:14:28','2015-04-08 09:50:31',NULL),(18,'提交建议','P_U_CONTACT','/user/contactMe/**',NULL,12,'2015-04-16 10:03:36','2015-04-29 23:20:04','2015-05-15 11:33:06'),(19,'申报记录','P_R_REPORTS','/report/reports/**',NULL,10,'2015-04-16 15:47:56','2015-04-29 23:20:06',NULL),(20,'职位管理','P_A_POSITION','/admin/position/**',NULL,1,'2015-05-14 14:06:58',NULL,NULL),(22,'个人资料','P_A_CENTER','/admin/center/**',NULL,1,'2015-05-14 17:23:39',NULL,NULL),(23,'修改密码','P_A_PWD','/admin/pwdPage/**',NULL,1,'2015-05-14 17:25:16',NULL,NULL),(24,'工单管理','P_A_ORDER','/admin/order/**',NULL,1,'2015-05-15 19:30:39',NULL,NULL),(25,'通知管理','P_A_ALERTINFO','/admin/alertinfo/**',NULL,1,'2015-06-05 15:41:39',NULL,NULL);

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
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色';

#
# Data for table "sec_role"
#

INSERT INTO `sec_role` VALUES (1,'超级管理员','R_ADMIN','',0,'2014-12-16 12:22:31','2015-04-17 10:33:01',NULL),(2,'系统管理员','R_MANAGER','',1,'2014-12-16 12:22:31','2015-03-30 15:20:55',NULL),(3,'运维部','R_DEAL','',0,'2014-12-16 12:22:31','2015-04-29 16:30:06',NULL),(4,'报障部','R_OFFER','',0,'2014-12-16 12:22:31','2015-05-04 11:04:48',NULL);

#
# Structure for table "sec_role_permission"
#

DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=utf8 COMMENT='角色-权限';

#
# Data for table "sec_role_permission"
#

INSERT INTO `sec_role_permission` VALUES (379,3,8),(380,3,9),(381,3,13),(382,3,14),(383,3,17),(384,3,16),(385,3,7),(386,3,12),(511,4,11),(512,4,19),(513,4,13),(514,4,14),(515,4,17),(516,4,16),(517,4,10),(518,4,12),(519,2,4),(520,2,22),(521,2,23),(522,2,13),(523,2,14),(524,2,1),(525,2,12),(526,1,2),(527,1,3),(528,1,4),(529,1,5),(530,1,6),(531,1,15),(532,1,20),(533,1,22),(534,1,23),(535,1,24),(536,1,25),(537,1,1);

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
  `operatorids` bigint(20) DEFAULT NULL COMMENT '功能id',
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
  `version` bigint(20) DEFAULT NULL,
  `errorcount` bigint(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL COMMENT '登录名',
  `providername` varchar(50) NOT NULL COMMENT '提供者',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(20) DEFAULT NULL COMMENT '固话',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `hasher` varchar(200) NOT NULL COMMENT '加密类型',
  `salt` varchar(200) NOT NULL COMMENT '加密盐',
  `avatar_url` varchar(255) DEFAULT '/res/img/touxiang.jpg' COMMENT '头像',
  `first_name` varchar(10) DEFAULT NULL COMMENT '名字',
  `last_name` varchar(10) DEFAULT NULL COMMENT '姓氏',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `remark` varchar(255) DEFAULT NULL COMMENT '保留属性',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户';

#
# Data for table "sec_user"
#

INSERT INTO `sec_user` VALUES (1,NULL,NULL,'admin','admin','fireterceltong@poicom.net',NULL,'15900088260','$shiro1$SHA-256$500000$70RYUWmzs8zMbAXc6j6zNg==$1e5TpkQCoBUIWw7TU+WTGEKSebwgYxUUV01Vm+CtWX4=','default_hasher','','/admin/assets/avatars/avatar4.png','科技','点通','管理员',NULL,'2015-03-11 10:46:12','2015-05-04 12:22:57',NULL),(2,NULL,NULL,'dealler','admin','dantechan@poicom.net',NULL,'18924518660','$shiro1$SHA-256$500000$o/aFEzJOSlJxqFL+a04+Tg==$iJfedksd5GXgvtuz50A4XL2p0X9YyPFe58Lzgb26NsQ=','default_hasher','','/admin/assets/avatars/avatar4.png','科技','点通','测试账号O',NULL,'2015-03-10 14:48:05','2015-04-24 15:37:28',NULL),(3,NULL,NULL,'reporter','admin','fireterceltong@poicom.net',NULL,'15900088260','$shiro1$SHA-256$500000$isKccerRU240Azacspdskg==$Ule6DgNHCziF5XQtj74ZGrXdBYfn38bdDwzZdNvVsXg=','default_hasher','','/admin/assets/avatars/avatar4.png','张','三','测试账号R',NULL,'2015-03-10 15:35:38','2015-04-28 10:07:06',NULL),(4,NULL,NULL,'dantechan','admin','dantechan@poicom.net',NULL,'18924518660','$shiro1$SHA-256$500000$hcbPhv80vNh2AWZvcOm4Sw==$4jTnh1YCDa4SZd/UINpu9fAdpvtMDZXiM5IamoJfr7A=','default_hasher','','/admin/assets/avatars/avatar4.png','陈','宇佳','陈宇佳',NULL,'2015-03-11 10:40:18','2015-04-28 10:07:03',NULL),(7,NULL,NULL,'firetercel','运维员','fireterceltong@poicom.net',NULL,'15900088260','$shiro1$SHA-256$500000$/TJpfQ6DV8CORGtJkDTVQw==$7qtBem3RH2cxnTQTIxHV4CLVS+r62j+t61f/doEhHXg=','default_hasher','','/admin/assets/avatars/avatar4.png','唐','东宇','唐东宇',NULL,'2015-05-07 17:32:02',NULL,NULL),(9,NULL,NULL,'hardyleung','管理员','18118118118@181.com',NULL,'18118118118','$shiro1$SHA-256$500000$d6idmCJqKa2J5svJXXaHfA==$4XFsiUXiiWECe4XOaKLU9P/Pr8CY/SCehXROArUvu0o=','default_hasher','','/admin/assets/avatars/avatar4.png','梁','润河','梁润河',NULL,'2015-05-18 16:04:11',NULL,'2015-07-01 17:36:56'),(10,NULL,NULL,'douglaswong','管理员','18118118118@181.com',NULL,'18118118118','$shiro1$SHA-256$500000$yUmNMQFUnbLdwOMBtvNoDQ==$GCcjbVtm51O9JacQbH5lp/ebu0iJ4Vte1W2cdESSFPo=','default_hasher','','/admin/assets/avatars/avatar4.png','黄','德赏','黄德赏',NULL,'2015-05-18 16:11:25',NULL,'2015-06-29 09:29:48'),(11,NULL,NULL,'mansonng','管理员','18118118118@181.com',NULL,'18118118118','$shiro1$SHA-256$500000$YCwZ+bqcBVXIATr5iu3xCg==$RH2RR4ZNfrInxwzHdoSZftNvVfaUvZ5P9+hZNWyiKUo=','default_hasher','','/admin/assets/avatars/avatar4.png','吴','明桂','吴明桂',NULL,'2015-05-18 16:14:06',NULL,'2015-06-29 09:29:50'),(12,NULL,NULL,'joewong','管理员','joewong@poicom.net',NULL,'13813138138','$shiro1$SHA-256$500000$EOdqU8VVihKMbaJ8JID8QA==$M/+4etYhvvnBSOKRkDOUsZ+EICEuCdiLuBXa/TU3/uw=','default_hasher','','/admin/assets/avatars/avatar4.png','黄','卓辉','黄卓辉',NULL,'2015-05-18 16:14:47',NULL,'2015-06-25 15:30:00'),(13,NULL,NULL,'stevenshen','管理员','18118118118@181.com',NULL,'18118118118','$shiro1$SHA-256$500000$fAeMe/KMPpYV5h4qQanlDQ==$4jH2eyYbBRhqAa2RRE1n9x3i3NFtXHXgif/bnkVX6QU=','default_hasher','','/admin/assets/avatars/avatar4.png','沈','金龙','沈金龙',NULL,'2015-05-18 16:15:30',NULL,'2015-06-25 15:30:01'),(62,NULL,NULL,'tangguoguo','管理员','15900088260@163.com',NULL,'15900088260','$shiro1$SHA-256$500000$WhKNBWIpqUYSyu1YkQVJlg==$aZ7dWEP0Z6VFYAXf6JClM2mWl6HRLLn/27Z9ROEmdkU=','default_hasher','','/admin/assets/avatars/avatar3.png','唐','果果','唐果果',NULL,'2015-05-22 20:00:31',NULL,'2015-06-25 14:31:02'),(63,NULL,NULL,'administrator','管理员','18118118118@181.com',NULL,'18118118118','$shiro1$SHA-256$500000$rT6NB1U0WgMaT67xmMH0LA==$8wGuG0gmSddzTtQmuNXpjj6bfd6FOX+3JZe1LBjkEqM=','default_hasher','','/admin/assets/avatars/avatar4.png','系统','管理员','系统管理员',NULL,'2015-06-02 18:58:41',NULL,'2015-06-29 09:29:57'),(64,NULL,NULL,'admin1','管理员','15900088260@163.com',NULL,'15900088260','$shiro1$SHA-256$500000$joXFVfRxWVuS5rIIspWlFQ==$QbvxI6YN642zCh6qoQQiNWM8LN7yFSEXQOkn70DwIKI=','default_hasher','','/admin/assets/avatars/avatar4.png','aa','aa','aaaa',NULL,'2015-06-04 14:22:22',NULL,'2015-06-25 14:31:08'),(65,NULL,NULL,'dantechan1','管理员','dantechan@poicom.net',NULL,'18924518660','$shiro1$SHA-256$500000$nadpi3whfPVxJ6+H6siTNQ==$/CUrZC85d3+0Sfot8EKYwIKxMnCWN0UdXsJHZwhFglw=','default_hasher','','/admin/assets/avatars/avatar4.png','陈','宇佳1','陈宇佳1',NULL,'2015-06-29 09:40:13',NULL,NULL),(66,NULL,NULL,'dantechan2','管理员','dantechan@poicom.net',NULL,'18924518660','$shiro1$SHA-256$500000$+4piXYp+aK94gEg8kW6Wgg==$ALuQC/EeJv6k+rkL8IX8CGqpol4ph4GfbfJQ3X5EOJk=','default_hasher','','/admin/assets/avatars/avatar4.png','陈','宇佳2','陈宇佳2',NULL,'2015-06-29 09:42:31',NULL,NULL),(67,NULL,NULL,'dantechan3','管理员','dantechan@poicom.net',NULL,'18924518660','$shiro1$SHA-256$500000$zTK0ar9ZvMYUGumxNQn/sg==$XEH06EKHCGcExuyEJyo2EyXc2gwCJe+GnmJeCEXPA48=','default_hasher','','/admin/assets/avatars/avatar4.png','陈','宇佳3','陈宇佳3',NULL,'2015-06-29 09:49:56',NULL,NULL);

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
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户-信息';

#
# Data for table "sec_user_info"
#

INSERT INTO `sec_user_info` VALUES (1,1,0,0,1,9,1,'2015-03-11 10:42:24','2015-04-30 14:47:39',NULL),(2,4,1,0,1,12,13,'2015-03-13 15:35:03','2015-04-30 14:47:50',NULL),(3,3,1,0,1,12,13,'2015-03-16 12:18:29','2015-04-29 11:06:31',NULL),(4,2,1,0,1,5,4,'2015-03-16 17:18:17','2015-04-29 11:06:31',NULL),(7,7,3,0,1,10,11,'2015-05-07 17:32:02',NULL,NULL),(9,9,1,0,1,9,1,'2015-05-18 16:04:11',NULL,NULL),(10,10,1,0,4,9,12,'2015-05-18 16:11:25',NULL,NULL),(11,11,1,0,1,9,5,'2015-05-18 16:14:06',NULL,NULL),(12,12,1,0,1,12,13,'2015-05-18 16:14:47',NULL,NULL),(13,13,1,0,1,12,13,'2015-05-18 16:15:30',NULL,NULL),(62,62,1,1,1,10,11,'2015-05-22 20:00:31',NULL,NULL),(63,63,1,0,1,9,5,'2015-06-02 18:58:41',NULL,NULL),(64,64,1,0,2,15,11,'2015-06-04 14:22:22',NULL,NULL),(65,65,1,0,1,12,13,'2015-06-29 09:40:13',NULL,NULL),(66,66,1,0,1,4,4,'2015-06-29 09:42:31',NULL,NULL),(67,67,1,0,1,5,4,'2015-06-29 09:49:56',NULL,NULL);

#
# Structure for table "sec_user_role"
#

DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='用户-角色';

#
# Data for table "sec_user_role"
#

INSERT INTO `sec_user_role` VALUES (1,1,1),(34,8,4),(35,9,2),(37,10,3),(39,12,4),(41,13,4),(43,2,3),(44,17,4),(48,18,4),(54,20,2),(56,21,4),(57,22,3),(59,4,3),(60,4,4),(61,7,4),(63,3,4),(64,63,2),(72,64,4),(73,11,4),(77,7,3),(78,62,4),(79,62,3),(80,65,4),(81,66,3),(82,65,3),(83,67,3);
