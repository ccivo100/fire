package com.poicom.basic.thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.function.model.Syslog;

/**
 * 操作日志入库处理
 * @author 陈宇佳
 *
 */
public class ThreadSysLog {
	
	protected static Logger logger=LoggerFactory.getLogger(ThreadSysLog.class);
	
	//入库队列大小
	private static final int queueSize=5000;
	//线程是否运行
	private static boolean threadRun=true;
	//此队列按照FIFO（先进先出）原则对元素进行排序
	private static Queue<Syslog> queue=new ConcurrentLinkedQueue<Syslog>();
	
	public static void setThreadRun(boolean threadRun){
		ThreadSysLog.threadRun=threadRun;
	}
	
	/**
	 * 向队列中增加Syslog对象，基于ConcurrentLinkedQueue
	 * @param syslog
	 */
	public static void add(Syslog syslog){
		if(syslog!=null){
			synchronized(queue){
				if(queue.size()<=queueSize){
					queue.offer(syslog);
				}else{
					queue.poll();//获取并移除队列的头，队列为空返回null
					queue.offer(syslog);//将指定元素插入此队列的尾部
					logger.error("日志队列：超过"+queueSize);
				}
			}
		}
	}
	
	/**
	 * 获取Syslog对象，基于ConcurrentLinkedQueue
	 * @return
	 */
	public static Syslog getSyslog(){
		synchronized(queue){
			if(queue.isEmpty()){
				return null;
			}else{
				return	queue.poll();//获取并移除此队列的头，队列为空返回null
			}
		}
	}
	
	public static void startSaveDBThread(){
		try{
			for(int i=0;i<10;i++){
				Thread insertDbThread =new Thread(new Runnable(){
					public void run(){
						while(threadRun){
							try{
								//取队列数据
								Syslog sysLog=getSyslog();
								if(null==sysLog){
									Thread.sleep(200);
								}else{
									logger.info("启动--保存操作日志到数据库...");
									sysLog.save();
									logger.info("完毕--保存操作日志到数据库...");
								}
							}catch(Exception e){
								logger.error("异常--保存操作日志到数据库异常！");
								e.printStackTrace();
								throw new RuntimeException("ThreadSysLog -> save Exception");
							}
						}
					}
				});
				
				insertDbThread.setName("com-poicom-Thread-SysLog-insertDB-"+(i+1));
				insertDbThread.start();
			}
		}catch(Exception e){
			throw new RuntimeException("ThreadSysLog new Thread Exception");
		}
	}

}
