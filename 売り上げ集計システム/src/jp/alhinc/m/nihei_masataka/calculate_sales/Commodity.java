package jp.alhinc.m.nihei_masataka.calculate_sales;

public class Commodity implements Comparable<Commodity> {
	String cCode;
	String cName;
	long cSum;
	
	Commodity(String n, String m){
		this.cCode=n;
		this.cName=m;
		this.cSum=0;
		}
	
	long cAddition(String s){
		try{
			this.cSum += Long.parseLong(s);
			
		}catch(NumberFormatException e){
			System.out.println("e");
		}
		return this.cSum;
	}
	
	public int compareTo(Commodity com){
		if(this.cSum > com.cSum){
			return -1;
		}else{
			return 1;
		}
	
		
	}


}
