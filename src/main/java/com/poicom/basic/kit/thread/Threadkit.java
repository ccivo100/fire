package com.poicom.basic.kit.thread;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.lang.ThreadUtils;

/**
 * 线程工具
 * @author FireTercel 2015年6月27日 
 *
 */
public class Threadkit {
	
	private static Logger log = LoggerFactory.getLogger(Threadkit.class);
	
	private final static int POOL_SIZE=50;
	
	public static void print(String print){
		System.out.println(print);
	}
	
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
				print(status());
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
				print(this.toString());
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
	
	/**
	 * 后台线程
	 * @author FireTercel 2015年6月29日 
	 *
	 */
	static class DaemonFromFactory implements Runnable{
		@Override
		public void run() {
			try{
				while(true){
					TimeUnit.MILLISECONDS.sleep(100);
					print(Thread.currentThread() + " " + this);
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
		print(" All daemons started");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static class DaemonThreadPoolExecutor extends ThreadPoolExecutor{
		public DaemonThreadPoolExecutor(){
			super(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),new DaemonThreadFactory());
		}
	}
	
	/**
	 * 后台线程创建的子线程，同样是后台线程。
	 * @author FireTercel 2015年6月29日 
	 *
	 */
	static class Daemon implements Runnable{
		private Thread[] t = new Thread[10];
		public void run(){
			for(int i=0;i<t.length;i++){
				t[i] = new Thread(new DaemonSpawn());
				t[i].start();
				print("DaemonSpawn " +i + " started. ");
			}
			for(int i=0;i<t.length;i++){
				print("t["+ i + "].isDaemon() = " +t[i].isDaemon());
			}
			while(true){
				Thread.yield();
			}
		}
	}
	
	static class DaemonSpawn implements Runnable{
		public void run(){
			while(true)
				Thread.yield();
		}
	}
	
	public static void testDaemonThreadPoolExecutor(){
		try {
			Thread d = new Thread(new Daemon());
			d.setDaemon(true);
			d.start();
			log.info("d.isDaemon() = " +d.isDaemon() +" , ");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 抛出异常。
	 * @author FireTercel 2015年6月29日 
	 *
	 */
	static class ExceptionThread implements Runnable{
		public void run(){
			throw new RuntimeException();
		}
	}
	
	public static void testExceptionThread(){
		ExecutorService executor = getExecutor(1);
		executor.execute(new ExceptionThread());
	}
	
	/**
	 * 捕获异常
	 * @author FireTercel 2015年7月23日 
	 *
	 */
	static class ExceptionThread2 implements Runnable{
		public void run(){
			Thread t = Thread.currentThread();
			print(" run() by "+t);
			print("eh = "+t.getUncaughtExceptionHandler());
			throw new RuntimeException();
		}
	}
	
	static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
		public void uncaughtException(Thread t, Throwable e){
			print(" caught "+e);
		}
	}
	
	static class HandlerThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable r){
			print(this + " creating new Thread");
			Thread t = new Thread(r);
			print("created "+t);
			t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
			print("eh = "+t.getUncaughtExceptionHandler());
			return t;
		}
	}
	
	public static void testCaptureUncaughtException(){
		ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
		exec.execute(new ExceptionThread2());
	}
	
	/**
	 * 使用相同的异常处理器，可以设置处理器为Default。
	 */
	public static void testSettingDefaultHandler(){
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		ExecutorService exec = getExecutor(1);
		exec.execute(new ExceptionThread());
	}
	
	
	public static void main(String[] args){
		testSimplePriorities();
	}

}
