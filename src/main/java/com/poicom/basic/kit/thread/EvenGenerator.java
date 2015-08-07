package com.poicom.basic.kit.thread;

public class EvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	/**
	 * 方法加锁，防止共享资源同时被多个任务进行操作<br>
	 * 使用Thread.yield()方法，可以提高上下文切换次数。
	 */
	@Override
	public synchronized int next() {
		++currentEvenValue;
		Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
	
	public static void main(String[] args){
		EvenChecker.test(new EvenGenerator());
	}

}
