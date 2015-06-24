package com.poicom.basic.thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.kit.StringKit;
import com.poicom.function.model.Alertinfo;
/**
 * 
 * @author 唐东宇
 *
 */
public class ThreadAlert {
	
	protected static Logger logger=LoggerFactory.getLogger(ThreadAlert.class);
	
	//入库队列大小
	private static final int queueSize=5000;
	//线程是否运行
	private static boolean threadRun=true;
	//此队列按照FIFO（先进先出）原则对元素进行排序
	private static Queue<AlertKit> queue=new ConcurrentLinkedQueue<AlertKit>();
	
	public static void setThreadRun(boolean threadRun){
		ThreadAlert.threadRun=threadRun;
	}
	
	public static void add(AlertKit alertKit){
		if(alertKit!=null){
			synchronized(queue){
				if(queue.size()<queueSize){
					queue.offer(alertKit);
				}else{
					queue.poll();//获取并移除队列的头，队列为空返回null
					queue.offer(alertKit);//将指定元素插入此队列的尾部
					logger.error("日志队列：超过"+queueSize);
				}
			}
		}
	}
	
	/**
	 * 获取  对象，基于ConcurrentLinkedQueue
	 * @return
	 */
	public static AlertKit getAlertKit(){
		synchronized(queue){
			if(queue.isEmpty()){
				return null;
			}else{
				return	queue.poll();//获取并移除此队列的头，队列为空返回null
			}
		}
	}
	
	public static void startSendEmailAndSmsThread(){
		try{
			for(int i=0;i<10;i++){
				Thread sendEmailAndSmsThread=new Thread(new Runnable(){
					public void run(){
						while(threadRun){
							try{
								//
								AlertKit alertKit=getAlertKit();
								if(null==alertKit){
									//logger.debug("提示--暂无任务（10秒后重新启动）...");
									Thread.sleep(1000*10);
								}else{
									logger.debug("启动--发送邮件/短信提醒功能...");
									alertKit.sendEmail(alertKit.getEmailTitle(),alertKit.getEmailBody(),alertKit.getEmailAdd());
									int smsrecode=alertKit.sendSms(alertKit.getSmsContext(),alertKit.getSmsPhone());
									
									Alertinfo alertinfo= new Alertinfo();
									alertinfo.set("email", alertKit.getEmailAdd())
												.set("emailcontext", alertKit.getEmailBody())
												.set("phone", StringKit.arrayToString(alertKit.getSmsPhone()))
												.set("smscontext", alertKit.getSmsContext())
												.set("smsrecode", smsrecode);
									alertinfo.save();
									
									logger.debug("完毕--发送邮件/短信提醒功能...");
								}
							}catch(Exception e){
								logger.error("异常--发送邮件/短信提醒功能异常！");
								e.printStackTrace();
								throw new RuntimeException("ThreadAlert -> send Exception");
							}
						}
					}
				});
				
				sendEmailAndSmsThread.setName("com-poicom-Thread-Alert-SendEmailAndSms-"+(i+1));
				sendEmailAndSmsThread.start();
				
			}
		}catch(Exception e){
			throw new RuntimeException("ThreadAlert new Thread Exception");
		}
	}
	
	

}
