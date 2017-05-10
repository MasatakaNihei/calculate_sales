package jp.alhinc.m.nihei_masataka.calculate_sales;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;
public class CalculateSales {
	
	public static void main(String[] args){
		String drectory = args[0];  //d=コマンドライン引数で受け取ったディレクトリ

		//1.支店定義ファイル読み込み
		//HashMapへ、支店コードをキーとして、対応する支店コード、支店名、合計金額(初期値0)を持つBranchインスタンスを生成・格納する
		
		HashMap<String, Branch> bMap = new HashMap<String, Branch>();
		BufferedReader br = null;
		try{
			File file = new File(drectory + "\\branch.lst");
			if(file.exists() == false){
				System.out.println("支店定義ファイルが存在しません");
				return;
			}
		    br =new BufferedReader(new FileReader(file)); //店舗定義ファイル読み込みストリーム
		    String bs;
		    while((bs = br.readLine()) != null){  //店舗定義データを一行ずつ読み込み、bMapへ格納
		    	if(Branch.bCheck(bs) == false){
		    		System.out.println("支店定義ファイルのフォーマットが不正です");
		    		return;
		    	}
		    	String[] str = bs.split(",");         
		    	bMap.put(str[0], new Branch(str[0],str[1]));  
		    }
		    
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
		
		
		//2.商品定義ファイル読み込み(支店と同様
		HashMap<String, Commodity> cMap = new HashMap<String, Commodity>();
		try{
			  File file = new File(drectory+ "\\commodity.lst");
			  if(file.exists() == false){
				  System.out.println("商品定義ファイルが存在しません");
				  return;
			  }
			   	
			  br =new BufferedReader(new FileReader(file)); 
			  String bs;
			  
			  while((bs = br.readLine()) != null){ 
				  if(Commodity.cCheck(bs) == false){
					  System.out.println("商品定義ファイルのフォーマットが不正です");
					  return;
				  }
				  
				  String[] str = bs.split(",");         
				  cMap.put(str[0], new Commodity(str[0],str[1]));  
			  }
			  
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
		
		//3.集計
		
		//数字8桁.rcdのファイルをリストへ格納し、数字部分を別のリストへ格納
		File folder = new File(drectory);
		String[] fileList = folder.list();
		Pattern r = Pattern.compile("^([０-９]|\\d){8}.rcd$");
		ArrayList<Integer> rcdNo = new ArrayList<Integer>();
		String[] splitrcd;
		for(String fileName : fileList){
			if(r.matcher(fileName).find()){
				splitrcd = fileName.split("\\.");
				rcdNo.add(Integer.parseInt(splitrcd[0]));
			}
			
		}
		//連番のチェック
		Collections.sort(rcdNo);
		
		for(int i = 0; i< (rcdNo.size()-1); i++){
			if(rcdNo.get(i).intValue() != (rcdNo.get(i+1).intValue()-1)){
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}
		
		
		
		int i=1; //.rcdファイル名の初期値
		
		for(int name : rcdNo){
			try {
				br =new BufferedReader(new FileReader(drectory+ "\\"+ String.format("%08d",name) + ".rcd")); 
				String s;
				String[] rcd = new String[3]; 
				
				for(int n = 0; (s = br.readLine()) != null; n++){ //読み込んだ売上データを配列rcdへ格納
					try{                                           //rcd[0]=支店コード、同[1]=商品コード、同[2]=売上金額
						rcd[n] = s;
						
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println(i+".rcdのフォーマットが不正です");
						return;
					}	
				}
				
				if(bMap.get(rcd[0]) == null){
					System.out.println(i + ".rcdの支店コードが不正です");
					return;
				}
				
				if(cMap.get(rcd[1]) == null){
					System.out.println(i + ".rcdの商品コードが不正です");
					return;
				}
				
				long b =(bMap.get(rcd[0])).bAddition(rcd[2]); //支店コードに対応するBranchインスタンスのbSumへ金額を加算し,加算後の金額を戻り値で取得
				long c =(cMap.get(rcd[1])).cAddition(rcd[2]); //前行の商品版
				if(b >= 10000000000L || c >= 10000000000L){
					System.out.println("合計金額が10桁を超えました");
					System.out.print(b+","+c);
					return;
				}
				
				 
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
				return;
			}finally{
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
			}
			
		}
		
		
		
		
		//4.集計結果出力  
	    //金額降順にSortする為、各マップに格納されているインスタンスをArrayListへ格納後、sortし、出力
		
		ArrayList<Branch> bList = new ArrayList<Branch>();
		ArrayList<Commodity> cList = new ArrayList<Commodity>();
		
		for(Branch bra : bMap.values()){
			bList.add(bra);
		}
		
		for(Commodity co : cMap.values()){
			cList.add(co);
		}
		
		Collections.sort(bList);
		Collections.sort(cList);
		
		BufferedWriter bw = null;
		try{
		    bw = new BufferedWriter(new FileWriter(new File (drectory+ "\\branch.out")));	
		    
		    for(Branch bran : bList){
		    	bw.write(bran.bCode + "," + bran.bName + "," + bran.bSum + "\r\n");
		    }
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
		
		try{
			bw = new BufferedWriter(new FileWriter(new File (drectory+ "\\commodity.out")));
			
			for(Commodity comm : cList){
				bw.write(comm.cCode + "," + comm.cName + "," + comm.cSum + "\r\n");
			}
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
		
		
				
			
		
		
		
	}
}
