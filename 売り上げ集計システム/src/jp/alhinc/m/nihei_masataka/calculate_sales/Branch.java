package jp.alhinc.m.nihei_masataka.calculate_sales;

import java.util.regex.Pattern;

public class Branch implements Comparable<Branch> {
	String bCode; //支店コード
	String bName; //支店名
	long bSum; //支店合計金額
	
	static Pattern bp = Pattern.compile("([０-９]|\\d){3},([^,])+"); //支店定義ファイルの1行のフォーマットを正規表現にコンパイル
	
	public static boolean bCheck(String s){ //引数で渡された支店定義ファイルの1行がフォーマット通りかチェックする
		return bp.matcher(s).find();
		
	}
	
	Branch(String n, String m){
		this.bCode =n;
		this.bName =m;
		this.bSum =0;
		}
	
	long bAddition(String s){  //引数で渡された金額(String型)を自身の合計金額に加算し、合計金額を返す
		try{
			this.bSum +=Long.parseLong(s);
			
		}catch(NumberFormatException e){
			System.out.println("e");
		}
		return this.bSum;
	}
	
	public int compareTo(Branch bran){
		if(this.bSum > bran.bSum){
			return -1;
		}else{
			return 1;
		}
	}

}
