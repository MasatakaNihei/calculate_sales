package jp.alhinc.nihei_masataka.calculate_sales;

import java.io.BufferedWriter;
import java.io.IOException;

public class Output {
	public static void bOut(Branch b, BufferedWriter sbr) throws IOException , NullPointerException{
		sbr.write(b.bCode + "," + b.bName + "," + b.bSum + System.getProperty("line.separator"));
	}
	
	public static void cOut(Commodity c, BufferedWriter cbr) throws IOException , NullPointerException{
		cbr.write(c.cCode + "," + c.cName + "," + c.cSum + System.getProperty("line.separator"));
	}

}
