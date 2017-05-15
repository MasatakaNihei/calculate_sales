package jp.alhinc.nihei_masataka.calculate_sales;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
public class CalculateSales {
	String code;
	String name;
	long sum;
	
	public CalculateSales(String n, String m){
		this.code = n;
		this.name = m;
		this.sum = 0;
	}
	
	public long getSum(){
		return this.sum;
	}

	//定義ファイル読み込みメソッド。例外の文を戻り値で返す。
	public static String fileInput(String filePass , String fileName, String name, String pattern,  HashMap<String,CalculateSales> map){
		BufferedReader br = null;

		try{
			File file = new File(filePass + File.separator + fileName);
			
			if(!file.exists()){
				return name + "定義ファイルが存在しません";
			}
			
			if(!file.isFile()){
				return "予期せぬエラーが発生しました";
			}
			
		    br =new BufferedReader(new FileReader(file));
		    String s;
		    while((s = br.readLine()) != null){  //定義データを一行ずつ読み込み、引き値で渡されたHashMapへ格納
		    	if(!s.matches(pattern)){
		    		return name + "定義ファイルのフォーマットが不正です";
		    	}
		    	String[] str = s.split(",");
		    	map.put(str[0], new CalculateSales(str[0],str[1]));  
		    }
		    
		}catch(IOException e){
			return "予期せぬエラーが発生しました";
		}finally{
			try {
				if(br != null)
				br.close();
			}catch (IOException e) {
				return "予期せぬエラーが発生しました";
			}
		}
		
		return "";
	}
	
		
	//集計ファイル出力メソッド。例外の文を戻り値で返す。
	public static String fileOutput(String filePass , String fileName, HashMap<String, CalculateSales> map){
		ArrayList<CalculateSales> sortArray = new ArrayList<CalculateSales>();
		
		sortArray.addAll(map.values());
		Collections.sort(sortArray, Comparator.comparing(CalculateSales::getSum).reversed());
		
		BufferedWriter bw =null ;
		try{
			bw = new BufferedWriter(new FileWriter(new File (filePass + File.separator + fileName))); 
			
			for(CalculateSales cal : sortArray ){
				bw.write(cal.code + "," + cal.name + "," + cal.sum + System.lineSeparator());
			}
		}catch(IOException e){
			return "予期せぬエラーが発生しました";
		}catch(NullPointerException e){
			return "予期せぬエラーが発生しました";
		}finally{
			try {
				if(bw != null)
				bw.close();
			} catch (IOException e) {
				return "予期せぬエラーが発生しました";
			}catch(NullPointerException e){
				return "予期せぬエラーが発生しました";
			}
		}
		
		return "";
	}
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;	
		}
		
		String derectory;
		//コマンドライン引数の末尾がファイルセパレーターの場合、末尾を一文字削って変数derectoryへ受け取る
		if(args[0].matches(File.separator+"$")){ 
			derectory = args[0].substring(0, (args[0].length()-1));
		}else{
			derectory = args[0];  //drectory=コマンドライン引数で受け取ったディレクトリ
		}
		
		String errorCode = ""; //各メソッドからエラー文を受け取る変数
		
		//1.支店定義ファイル読み込み
		//HashMapへ、支店コードをキーとして、対応する支店コード、支店名、合計金額(初期値0)を持つBranchインスタンスを生成・格納する
		
		HashMap<String, CalculateSales> bMap = new HashMap<String, CalculateSales>();
	
		errorCode = CalculateSales.fileInput(derectory, "branch.lst","支店" , "^([０-９]|\\d){3},([^,^\\s])+$", bMap);
		
		if(!errorCode.isEmpty()){
			System.out.println(errorCode);
			return;
		}
		
		//2.商品定義ファイル読み込み(支店と同様
		HashMap<String, CalculateSales> cMap = new HashMap<String, CalculateSales>();
		
		errorCode = CalculateSales.fileInput(derectory, "commodity.lst","商品" , "^(\\d|[A-Za-zＡ-Ｚａ-ｚ０-１]){8},([^,^\\s])+$", cMap);
		
		if(!errorCode.isEmpty()){
			System.out.println(errorCode);
			return;
		}
		
		//3.集計
		
		//数字8桁.rcdのファイルを抽出し、数字部分をリストへ格納
		File[] fileList = new File(derectory).listFiles();
		ArrayList<File> rcdFiles = new ArrayList<File>();
		for(File file : fileList){
			if(file.getName().matches("^([０-９]|\\d){8}\\.rcd$") && file.isFile()){
				rcdFiles.add(file);
			}
			
		}
		//連番のチェック
		Collections.sort(rcdFiles);
		int now =0;
		if(rcdFiles.size() != 0){
			now = Integer.parseInt(rcdFiles.get(0).getName().split("\\.")[0]);
		}
		
		for(File f : rcdFiles){
			if(Integer.parseInt(f.getName().substring(0 , 8)) != now){
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
			now ++ ;
		}
		
		//売上ファイルを読み込み、各マップへ集計
		BufferedReader rcdbr = null;
		for(File file : rcdFiles){
			try {
				rcdbr =new BufferedReader(new FileReader(file)); 
				String s;
				String[] rcdstr = new String[3];
				for(int n = 0; (s = rcdbr.readLine()) != null; n++){ //読み込んだ売上データを配列rcdstrへ格納
					try{                                           //rcdstr[0]=支店コード、同[1]=商品コード、同[2]=売上金額
						rcdstr[n] = s;
						
					}catch(ArrayIndexOutOfBoundsException e){ //売上ファイルが4行以上ある場合
						System.out.println(file.getName() +"のフォーマットが不正です");
						return;
					}
					if(rcdstr[n].isEmpty() || rcdstr[n].matches("^\\s+$")){  //読み込んだ一行が改行または空白のみの場合
						System.out.println(file.getName() +"のフォーマットが不正です");
						return;
					}
				}
				
				if(rcdstr[2] == null | rcdstr[1]== null | rcdstr[0]== null){  //売上ファイルが3行未満の場合
					System.out.println(file.getName() +"のフォーマットが不正です");
					return;
				}
				
				if(bMap.get(rcdstr[0]) == null){
					System.out.println(file.getName() + "の支店コードが不正です");
					return;
				}
				
				if(cMap.get(rcdstr[1]) == null){
					System.out.println(file.getName() + "の商品コードが不正です");
					return;
				}
				
				long b = (bMap.get(rcdstr[0]).sum += Long.parseLong(rcdstr[2]));
				long c = (cMap.get(rcdstr[1]).sum += Long.parseLong(rcdstr[2]));
				if(String.valueOf(b).length() >10 || String.valueOf(c).length() > 10){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				rcdstr[0] = null;  //エラーを正常にキャッチするためrcdstrの各要素をnullへリセット
				rcdstr[1] = null;
				rcdstr[2] = null;
				
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
				return;
			
			}catch(NumberFormatException e){
				System.out.println("予期せぬエラーが発生しました");
				return;
			
			}finally{
				try {
					if(rcdbr != null)
					rcdbr.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
			}
			
		}
		
		//4.集計結果出力  
	    
		errorCode = CalculateSales.fileOutput(derectory , "branch.out" , bMap); //支店別集計ファイルの出力
		
		if(!errorCode.isEmpty()){
			System.out.println(errorCode);
			return;
		}
		
		errorCode =  CalculateSales.fileOutput(derectory , "commodity.out" , cMap); //商品別集計ファイルの出力
		
		if(!errorCode.isEmpty()){
			System.out.println(errorCode);
			return;
		}
	}
}
