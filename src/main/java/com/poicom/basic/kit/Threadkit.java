package com.poicom.basic.kit;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程工具
 * @author FireTercel 2015年6月27日 
 *
 */
public class Threadkit {
	
	private static Logger log = LoggerFactory.getLogger(Threadkit.class);
	
	private final static int POOL_SIZE=50;
	
	/**
	 * 1：缓存型线程池；
	 * 2：固定线程池；
	 * 3：调度型线程池；
	 * 4：单例线程；
	 * @param poolNum
	 * @return
	 */
	public static ExecutorService getExecutor(int poolNum){
		switch(poolNum){
		case 1:
			return Executors.newCachedThreadPool();
		case 2:
			return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		case 3:
			return Executors.newSingleThreadExecutor();//同参数为1的FixedThreadPool
		case 4:
			return Executors.newScheduledThreadPool(100);//线程池里面最多有paras个线程
		default:
			return Executors.newCachedThreadPool();
		}
	}
	
	/**
	 * 普通线程
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class LiftOff implements Runnable{
		protected int countDown = 10;
		private static int taskCount = 0;
		private final int id = taskCount++;
		public LiftOff(){}
		public LiftOff(int countDown){
			this.countDown= countDown;
		}
		public String status(){
			return "#" + id +"("+(countDown > 0 ? countDown : "Liftoff!")+").";
		}
		@Override
		public void run() {
			while(countDown-->0){
				log.info(status());
				Thread.yield();
			}
		}
	}
	public static void testLiftOff(){
		int cpuNum=Runtime.getRuntime().availableProcessors();
		log.info("可用CPU数量："+cpuNum);
		ExecutorService executor= getExecutor(4);
		for(int i=0;i<5;i++){
			executor.execute(new Threadkit.LiftOff());
		}
		executor.shutdown();
	}
	
	/**
	 * 带返回值线程
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class TaskWithResult implements Callable<String>{
		private int id;
		public TaskWithResult(int id){
			this.id=id;
		}
		@Override
		public String call() throws Exception {
			return "result of TaskWithResult " + id;
		}
	}
	
	public static void testTaskWithResult(){
		ExecutorService executor= getExecutor(1);
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		for(int i=0;i<10;i++){
			results.add(executor.submit(new TaskWithResult(i)));
		}
		for(Future<String> fs:results){
			try{
				//get()方法前先调用isDone()确定结果准备就绪。
				log.info(fs.get());//直接调用get()会阻塞待结果准备就绪。
			}catch(InterruptedException e){
				log.error(e.toString());
				return;
			}catch(ExecutionException e ){
				log.error(e.toString());
			}finally{
				executor.shutdown();
			}
		}
	}
	
	/**
	 * 休眠
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class SleepingTask extends LiftOff{
		public void run(){
			try{
				while(countDown-->0){
					log.info(status());
					TimeUnit.MILLISECONDS.sleep(100);
				}
			}catch(InterruptedException e){
				log.error("Interrupted");
			}
		}
	}
	
	public static void testSleepingTask(){
		ExecutorService executor= getExecutor(1);
		for(int i=0;i<5;i++){
			executor.execute(new SleepingTask());
		}
		executor.shutdown();
	}
	
	/**
	 * 优先级
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class SimplePriorities implements Runnable{
		private int countDown =5;
		private volatile double d;
		private int priority;
		public SimplePriorities(int priority){
			this.priority= priority;
		}
		public String toString(){
			return Thread.currentThread() + ":" +countDown;
		}
		@Override
		public void run() {
			Thread.currentThread().setPriority(priority);
			while(true){
				for(int i=1;i<100000;i++){
					d+=(Math.PI + Math.E) / (double)i;
					if(i%1000 ==0)
						Thread.yield();
				}
				log.info(this.toString());
				if(--countDown ==0) return;
			}
		}
	}
	
	public static void testSimplePriorities(){
		ExecutorService executor = getExecutor(1);
		for(int i=0;i<5;i++){
			executor.execute(new SimplePriorities(Thread.MIN_PRIORITY));
		}
		executor.execute(new SimplePriorities(Thread.MAX_PRIORITY));
		executor.shutdown();
	}
	
	/**
	 * 后台线程
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class SimpleDaemons implements Runnable{
		@Override
		public void run() {
			try{
				while(true){
					TimeUnit.MILLISECONDS.sleep(100);
					log.info(Thread.currentThread() + " " +this);
				}
			}catch(InterruptedException e){
				log.info(" sleep() interrupted"); 
			}
		}
	}
	
	public static void testSimpleDaemons(){
		for(int i=0;i<10;i++){
			Thread daemon = new Thread(new SimpleDaemons());
			daemon.setDaemon(true);//非后台线程结束时，后台线程会终止
			daemon.start();
		}
		log.info("All daemons started");
		try {
			TimeUnit.MILLISECONDS.sleep(175);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 后台线程工厂
	 * @author FireTercel 2015年6月27日 
	 *
	 */
	static class DaemonThreadFactory implements ThreadFactory{
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	}
	
	static class DaemonFromFactory implements Runnable{
		@Override
		public void run() {
			try{
				while(true){
					TimeUnit.MILLISECONDS.sleep(100);
					log.info(Thread.currentThread() + " " + this);
				}
			}catch(InterruptedException e ){
				log.error("Interrupted");
			}
		}
	}
	
	public static void testDaemonFromFactory(){
		ExecutorService executor = Executors.newCachedThreadPool(new DaemonThreadFactory());
		for(int i=0 ; i<10; i++){
			executor.execute(new DaemonFromFactory());
		}
		log.info(" All daemons started");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		testDaemonFromFactory();
	}

}
