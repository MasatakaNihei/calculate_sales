package jp.alhinc.nihei_masataka.calculate_sales;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;


public class RcdInput {
	
	public static final String[] rcdstr =new String[3];
	static String s;
	
	//連番チェック後の.rcdファイルを読み込み、売上金額を支店・商品にそれぞれ加算する。例外を戻り値0～4で返す。
	//戻り値0=例外なし 1=.rcdのフォーマットが不正 2=支店コードが不正 3=商品コードが不正　4=金額が10桁超え
	public static int rIn (BufferedReader rbr, HashMap<String, Branch> bm, HashMap<String, Commodity> cm)throws IOException{
		for(int n = 0; (s = rbr.readLine()) != null; n++){ //読み込んだ売上データを配列rcdへ格納
			try{                                           //rcdstr[0]=支店コード、同[1]=商品コード、同[2]=売上金額
				rcdstr[n] = s;
				
			}catch(ArrayIndexOutOfBoundsException e){ //4行以上ある場合
				return 1;
			}
			if((rcdstr[n].equals("")) | (Pattern.compile("^\\s+$")).matcher(rcdstr[n]).find()){  //読み込んだ一行が改行または空白のみの場合
				return 1;
			}
			
		}
		
		if(rcdstr[2]== null){  //売上ファイルが2行しかなかった場合
			return 1;
		}
		
		
		if(bm.get(rcdstr[0]) == null){
			return 2;
		}
		
		if(cm.get(rcdstr[1]) == null){
			return 3;
		}
		
		long b = (((Branch)bm.get(rcdstr[0])).bSum += Long.parseLong(rcdstr[2]));
		long c = (((Commodity)cm.get(rcdstr[1])).cSum +=  Long.parseLong(rcdstr[2]));
		if(String.valueOf(b).length() >10 || String.valueOf(c).length() > 10){
			
			return 4;
		}
		
		return 0;
	}

}
