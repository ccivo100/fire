package com.poicom.common.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
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
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;

public class AlertJob implements Job{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	@Before(Tx.class)
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		int time=PropKit.getInt("alert.time", 20);
		//获取status为1的工单，即未处理
		List<Order> orders =Order.dao.findOrderByStatus(1);
		Map<Long,Record> operatorMap=new HashMap<Long,Record>();
		DateTime now=DateTime.now();
		for(Order order:orders){
			int t=DateKit.dateBetween(order.get("offer_at").toString(),now);
			if(t>time){
				//设置Order的status为 2：已超时。
				order.set("status", 2).update();
				getAlertOperator(operatorMap,order);
			}
		}
		System.out.println(operatorMap.values());
		//doAlert(operatorMap);
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
			//String[] array =new String[phones.size()];
			//AlertKit.sendSms(userinfo,phones.toArray(array));
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
	
	
	/**
	 * @描述 判断申报时间和现在时间差。
	 * @param offer_at
	 * @param now
	 * @return
	 */
	@Deprecated
	public int dateBetween(DateTime offer_at,DateTime now){
		
		System.out.print("两个时间相差：");
		/*System.out.print(Days.daysBetween(offer_at, now).getDays() + " 天, ");
        System.out.print(Hours.hoursBetween(offer_at, now).getHours() % 24
                + " 小时, ");
        System.out.print(Minutes.minutesBetween(offer_at, now).getMinutes() % 60
                + " 分钟, ");
        System.out.println(Seconds.secondsBetween(offer_at, now).getSeconds() % 60
                + " 秒.");*/
        int time=Days.daysBetween(offer_at, now).getDays()*24+Hours.hoursBetween(offer_at, now).getHours() % 24;
        System.out.println("一共 "+time+" 小时 ");
        return time;
	}

}
