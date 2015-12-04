package com.poicom.function.notice.template;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.kit.DateKit;
import com.poicom.function.model.Order;

/**
 * 短信模板
 * @author FireTercel 2015年7月24日 
 *
 */
public class SmsTemplate {
	
	/**
	 * 新建故障单发送短信
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String newOrderSmsMsg(Record offer,Order order){
		StringBuffer msg = new StringBuffer();
		msg.append("您好，")
				.append(offer.getStr("bname")+"的 ")
				.append(offer.getStr("ufullname"))
				.append("（"+offer.getStr("uphone")+"） ")
				//.append("提交了关于”"+StringUtils.abbreviate(order.getStr("title"),10)+"“的故障工单，请尽快处理。");
				.append("提交了来自”"+StringUtils.abbreviate(order.getStr("title"),10)+"“的故障申诉，请尽快处理。");
		return msg.toString();
	}
	
	/**
	 * 新建故障单发送短信-TO-同部门人员
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String newOrderToOwnSmsMsg(Record offer,Order order){
		StringBuffer msg=new StringBuffer();
		msg.append("您好，贵部门的")
				.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"） ")
				//.append("提交了关于”"+StringUtils.abbreviate(order.getStr("title"),10)+"“")
				.append("提交了来自”"+StringUtils.abbreviate(order.getStr("title"),10)+"“")
				.append("的故障申诉，详情请登陆系统查看。");
		return msg.toString();
	}
	
	/**
	 * 撤回故障单发送短信
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String recallOrderSmsMsg(Record offer,Order order){
		StringBuffer msg=new StringBuffer();
		msg.append("您好，")
				.append(offer.getStr("bname")+" 的 ")
				.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
				.append("撤回于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
				.append("发起的故障申告！");
		return msg.toString();
	}
	
	/**
	 * 每个处理环节，发送短信
	 * @param offer
	 * @param deal
	 * @param order
	 * @param selectProgress
	 * @return
	 */
	public static String dealOrderSmsMsg(Record offer,Record deal,Order order,int selectProgress){
		StringBuffer msg=new StringBuffer();
		msg.append("尊敬的用户，")
				.append(offer.getStr("aname")+"的"+offer.getStr("ufullname"))
				.append("于"+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+"，")
				//.append("提交关于“"+StringUtils.abbreviate(order.getStr("title"),15)+"”"+"的故障单。")
				.append("提交来自“"+StringUtils.abbreviate(order.getStr("title"),15)+"”"+"故障申诉。")
				.append("现由"+deal.getStr("aname")+"的"+deal.getStr("ufullname"));
		if(selectProgress == 0){
			msg.append("开始处理");
		}else if(selectProgress == 1){
			msg.append("继续处理");
		}else if(selectProgress == 2){
			msg.append("处理完毕");
		}else if(selectProgress == 3){
			msg.append("转派处理");
		}else if(selectProgress == 4){
			msg.append("提出反馈");
		}
		else if(selectProgress == 5){
			msg.append("反馈结果");
		}
		else if(selectProgress == 6){
			msg.append("回访完毕");
		}
		
		msg.append("，详情请登陆系统查看。");
		return msg.toString();
	}
	

}
