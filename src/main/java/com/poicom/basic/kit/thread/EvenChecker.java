package com.poicom.basic.kit.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable {
	private IntGenerator generator;
	private final int id;
	public EvenChecker(IntGenerator g,int ident){
		generator = g;
		id = ident;
	}
	@Override
	public void run() {
		while(!generator.isCanceled()){
			int val = generator.next();
			if(val %2!=0){
				print(val + " not even!");
				generator.cancel();
			}
			print("val = "+val);
		}
	}
	public static void test(IntGenerator gp){
		test(gp,10);
	}
	
	public static void test(IntGenerator gp,int count){
		print("Press Control-C to exit");
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0 ; i<count ; i++){
			exec.execute(new EvenChecker(gp,i));
		}
		exec.shutdown();
	}
		
	public static void print(String print){
		System.out.println(print);
	}
	
}
