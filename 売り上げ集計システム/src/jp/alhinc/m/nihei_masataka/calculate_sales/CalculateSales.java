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
public class CalculateSales {
	
	static String d ; //引数で渡されたディレクトリをクラス変数dで保持する
	
	
	public static void main(String[] args){
		//0.ディレクトリの受け取り（未定
		
		
		
		//1.支店定義ファイル読み込み
		//HashMapへ、支店コードをキーとして、対応する支店コード、支店名、合計金額(初期値0)を持つBranchインスタンスを生成・格納する
		
		HashMap<String, Branch> bMap = new HashMap<String, Branch>();
		
		try{
		    BufferedReader br =new BufferedReader(new FileReader(new File(d+ "\\branch.list"))); //店舗定義ファイル読み込みストリーム
		    String bs;
		    while((bs = br.readLine()) != null){  //店舗定義データを一行ずつ読み込み、bMapへ格納
			String[] str = bs.split(",");         
			bMap.put(str[0], new Branch(str[0],str[1]));  
			
		    }
		    br.close();
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}
		
		
		//2.商品定義ファイル読み込み(支店と同様
		HashMap<String, Commodity> cMap = new HashMap<String, Commodity>();
		try{
			  BufferedReader br =new BufferedReader(new FileReader(new File(d+ "\\commodity.list"))); 
			    String bs;
			    while((bs = br.readLine()) != null){  
				String[] str = bs.split(",");         
				cMap.put(str[0], new Commodity(str[0],str[1]));  
				
			    }
			    br.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
		
		//3.集計
		int i=1; //.rcdファイル名の初期値
		
		for(File file= new File(d+ "\\"+String.format("%08d", i)+ ".rcd"); file.exists() ; i++){
			try {
				BufferedReader br =new BufferedReader(new FileReader(file)); 
				String s;
				String[] rcd = new String[3]; 
				
				for(int n = 0; (s = br.readLine()) != null; n++){ //読み込んだ売上データを配列rcdへ格納
					try{                                           //rcd[0]=支店コード、同[1]=商品コード、同[2]=売上金額
						rcd[n] = s;
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println(i+".rcdのフォーマットが不正です");
					}	
				}
				
				long b =(bMap.get(rcd[0])).bAddition(rcd[2]); //支店コードに対応するBranchインスタンスのbSumへ金額を加算し,加算後の金額を戻り値で取得
				long c =(cMap.get(rcd[1])).cAddition(rcd[2]); //前行の商品版
				if(b >= 10000000000L || c >= 10000000000L){
					System.out.println("合計金額が10桁を超えました");
				}
			    br.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
			
		}
		
		//4.集計結果出力  
	    //金額降順にSortする為、各マップに格納されているインスタンスをArrayListへ格納後、sortし、出力
		
		ArrayList<Branch> bList = new ArrayList<Branch>();
		ArrayList<Commodity> cList = new ArrayList<Commodity>();
		
		for(Branch br : bMap.values()){
			bList.add(br);
		}
		
		for(Commodity co : cMap.values()){
			cList.add(co);
		}
		
		Collections.sort(bList);
		Collections.sort(cList);
		
		try{
		    BufferedWriter bbw = new BufferedWriter(new FileWriter(new File (d+ "\\branch.out")));	
		    
		    for(Branch bran : bList){
		    	bbw.write(bran.bCode + "," + bran.bName + "," + bran.bSum + "\r\n");
		    }
		    bbw.close();
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}
		
		try{
			BufferedWriter cbw = new BufferedWriter(new FileWriter(new File (d+ "\\commodity.out")));
			
			for(Commodity comm : cList){
				cbw.write(comm.cCode + "," + comm.cName + "," + comm.cSum + "\r\n");
			}
			cbw.close();
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}
		
		
				
			
		
		
		
	}
}
