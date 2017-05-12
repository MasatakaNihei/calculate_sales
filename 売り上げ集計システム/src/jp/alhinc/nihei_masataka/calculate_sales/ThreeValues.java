package jp.alhinc.nihei_masataka.calculate_sales;

//コード、名前、金額の三つを同時に扱うためのクラス
public class ThreeValues  implements Comparable<ThreeValues>{
	String code;
	String name;
	long sum;
	
	public ThreeValues(String n, String m){
		this.code = n;
		this.name = m;
		this.sum = 0;
	}
	
	public int compareTo(ThreeValues s){
		if(this.sum > s.sum){
			return -1;
		}else{
			return 1;
		}	
	}
}
