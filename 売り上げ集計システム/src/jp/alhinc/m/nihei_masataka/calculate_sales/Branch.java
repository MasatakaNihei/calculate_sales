package jp.alhinc.m.nihei_masataka.calculate_sales;


public class Branch implements Comparable<Branch> {
	String bCode; //支店コード
	String bName; //支店名
	long bSum; //支店合計金額
	
	Branch(String n, String m){
		this.bCode =n;
		this.bName =m;
		this.bSum =0;
		}
	
	
	public int compareTo(Branch bran){
		if(this.bSum > bran.bSum){
			return -1;
		}else{
			return 1;
		}
	}

}
