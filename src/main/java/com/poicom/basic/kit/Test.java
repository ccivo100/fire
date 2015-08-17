package com.poicom.basic.kit;

public class Test {
	
	public void print(int n){
		if(n>1){
			int index =n-1;
			print(index);
		}
		for(int i=1;i<=n;i++){
			System.out.print(i+" * "+n+" = " + n * i + "   ");
		}
		System.out.println();
	}
	public static void main(String[] args){
		new Test().print(9);
	}
}
