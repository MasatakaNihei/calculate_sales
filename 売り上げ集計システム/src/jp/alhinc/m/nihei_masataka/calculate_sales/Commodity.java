package jp.alhinc.m.nihei_masataka.calculate_sales;

import java.util.regex.Pattern;

public class Commodity implements Comparable<Commodity> {  //Branchクラスの商品版
	String cCode;
	String cName;
	long cSum;
	
    static Pattern  cp = Pattern.compile("(\\d|[A-Za-zＡ-Ｚａ-ｚ０-１]){8},[^,]+");
	
    public static boolean cCheck(String s){
    	return cp.matcher(s).find();
    }
    
    
	Commodity(String n, String m){
		this.cCode=n;
		this.cName=m;
		this.cSum=0;
		}
	
	
	public int compareTo(Commodity com){
		if(this.cSum > com.cSum){
			return -1;
		}else{
			return 1;
		}
	
		
	}


}
