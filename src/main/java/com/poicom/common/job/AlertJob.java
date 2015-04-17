package com.poicom.common.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.common.kit.AlertKit;
import com.poicom.common.kit.DateKit;
import com.poicom.function.app.model.Level;
import com.poicom.function.app.model.Order;
import com.poicom.function.system.model.User;

/**
 * 定时器，定时扫面com_order表中
 * 对超时未处理的数据进行设置并触发短信提示功能
 * @author poicom7
 *
 */
public class AlertJob implements Job{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	@Before(Tx.class)
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		int time=PropKit.getInt("alert.time", 20);
		//故障等级
		List<Level>levelList=Level.dao.findAll();
		
		//获取status为1的工单，即未处理
		List<Order> orders =Order.dao.findOrderByStatus(1);
		Map<Long,Record> operatorMap=new HashMap<Long,Record>();
		DateTime now=DateTime.now();
		for(Order order:orders){
			int t=DateKit.dateBetween(order.get("offer_at").toString(),now);
			Level level=getLevel(levelList,order);
			time=level.getInt("deadline");
			if(t>time){
				//设置Order的status为 2：已超时。
				order.set("status", 2).update();
				getAlertOperator(operatorMap,order);
			}
		}
		logger.info(operatorMap.values().toString());
		System.out.println("执行");
		//doAlert(operatorMap);
	}
	
	public static Level getLevel(List<Level> levelList,Order order){
		Level level=null;
		for(Level l:levelList){
			if(l.get("id")==order.get("level")){
				level=l;
				break;
			}
		}		
		return level;
	}
	
	/**
	 * @描述 执行提醒操作
	 */
	public void doAlert(Map<Long,Record> operatorMap){
		//用户不为空
		if(!ValidateKit.isNullOrEmpty(operatorMap.values())){
			Set<Long> keys=operatorMap.keySet();
			List<String> phones=new ArrayList<String>();
			for(Long key:keys){
				Record operator=operatorMap.get(key);
				String body="您好，您所属故障类型工单已超时未处理，请及时处理。";
				if(ValidateKit.isEmail(operator.getStr("useremail"))){
					AlertKit.sendEmail("点通故障系统提醒您！",body,operator.getStr("useremail"));
				}
				//电话&&非空 then 保存电话列表
				if(!ValidateKit.isNullOrEmpty(operator.getStr("userphone"))){
					if(ValidateKit.isPhone(operator.getStr("userphone"))){
						phones.add(operator.getStr("userphone"));
					}
				}
			}
			//发送短信
			String[] array =new String[phones.size()];
			AlertKit.sendSms(new Object(),phones.toArray(array));
		}
	}
	
	/**
	 * @描述 获取需要通知的用户集
	 */
	public void getAlertOperator(Map<Long,Record> operatorMap,Order order){
		//获取该Order的类型type
		Long typeid=order.get("type");
		//根据type，查询相应User的List  Record类型保存userid,useremail,userphone,firstname,lastname,fullname。
		List<Record> operators=User.dao.getOperatorList(typeid);
		for(Record operator:operators){
			if(operatorMap.get(operator.getLong("userid"))==null){
				operatorMap.put(operator.getLong("userid"), operator);
			}
		}
	}
	

}
