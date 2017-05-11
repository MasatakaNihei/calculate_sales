package jp.alhinc.nihei_masataka.calculate_sales;

public class Commodity implements Comparable<Commodity> {  //Branchクラスの商品版
	String cCode;
	String cName;
	long cSum;
    
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
