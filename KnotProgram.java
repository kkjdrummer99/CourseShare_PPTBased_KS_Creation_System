package com.kse.knot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.kse.model.EdgeModel;
import com.kse.model.KnotFileResultModel;
import com.kse.model.KnotInputModel;
import com.kse.model.KnotResultModel;
import com.kse.model.LinkModel;
import com.kse.util.FileUtil;
import com.kse.util.RestClient;
import com.kse.util.RestClient.RequestMethod;

public class KnotProgram {
  
	//write prx file
	public static boolean writePrxFile(String filePath,int rowCount,double[][] coTable){
		
		boolean result = false;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write("data"); out.newLine();
			out.write("distances"); out.newLine();
			out.write(rowCount +" nodes"); out.newLine();
			out.write("0 decimal places"); out.newLine();
			out.write("1 minimum"); out.newLine();
			out.write("7 maximum"); out.newLine();
			out.write("upper triangle:"); out.newLine();
			
			for(int i=0; i<rowCount; i++) {
				for(int j=i; j<rowCount; j++) {
					out.write(coTable[i][j] + "\t");
				}
				out.newLine();
			}
			out.close();
			
			result = true;
		}catch(Exception e){
			System.out.println("ex = "+e.toString());
		}
		return result;
	}
	
	
	// cmd knot program
	public static boolean cmdKnotMake(String input) {
		boolean result = false;

		String prefix = FileUtil.getRootPath() + "\\knot";

		String dataPrx = prefix + "\\data.prx";
		String dataPf = prefix + "\\data.pf";
		String dataOut = prefix + "\\data.out";
		
		String originPrefix = FileUtil.getRootPath() + "\\knot_data";


		String originPrx = originPrefix + "\\" + input + ".prx";
		String originPf = originPrefix + "\\" + input + ".pf";
		String originOut = originPrefix + "\\" + input + ".out";

		// delete before file
		try {

			File pfFile = new File(dataPf);
			if (pfFile.exists()) {
				pfFile.delete();
			}

			File prxFile = new File(dataPrx);
			if (prxFile.exists()) {
				prxFile.delete();
			}

			File outFile = new File(dataOut);
			if (outFile.exists()) {
				outFile.delete();
			}

			File originPrxFile = new File(originPrx);
			File tempPrx = new File(dataPrx);

			originPrxFile.renameTo(tempPrx);

		} catch (Exception e) {
			System.out.println("ex = " + e.toString());
		}

		
		//do knot
		try {
			String s = "";
			String filePath = FileUtil.getRootPath();
			ProcessBuilder pb = new ProcessBuilder("cmd", "/C", "knot -p -d "
					+ "data.prx" + " -c -o " + "data.out");

			File dir = new File(filePath + "\\knot");
			pb.directory(dir);
			Process p = pb.start();

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((s = stdOut.readLine()) != null)
				System.out.println(s);
			while ((s = stdError.readLine()) != null)
				System.out.println(s);

			
			System.out.println("finish");
		} catch (Exception e) {
			result = false;
			System.out.println("ex = " + e.toString());
		}

		
		//move knot file 
		try {

			File originPrxFile = new File(dataPrx);
			File tempPrx = new File(originPrx);
			originPrxFile.renameTo(tempPrx);

			
			File originPfFile = new File(dataPf);
			File tempPf = new File(originPf);
			originPfFile.renameTo(tempPf);

			
			File originOutFile = new File(dataOut);
			File tempOut = new File(originOut);
			originOutFile.renameTo(tempOut);

			result = true;
			
		} catch (Exception e) {
			result = false;
			System.out.println("ex = " + e.toString());
		}
		return result;
	}
	
	
	//get coherence
	public static double getCoherence(String filePath){
		double result = 0;
		
		try{
			
			File target = new File(filePath);
			
			BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(target)));
			
			fis.readLine();
			fis.readLine();
			fis.readLine();
			fis.readLine();
			fis.readLine();
			fis.readLine();
			fis.readLine();
			
			String resultLine = fis.readLine().trim();
			String[] resultTemp = resultLine.split(" ");
			result = Double.parseDouble(resultTemp[0].trim());
			
			fis.close();
			fis = null;
			
			
		}catch(Exception e){
			System.out.println("ex getCoherence ="+e.toString());
		}
		return result;
	}
	
	
	//get edge array from file
	 public static ArrayList<EdgeModel> getEdgeArrayFromFile(String fileName){
	    	ArrayList<EdgeModel> result = new ArrayList<EdgeModel>();
	    	
	    	File file = new File(fileName);
	    	String s;
	    	int id = 1;
	    	try{
	    		BufferedReader fis = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
	    		for(int i = 0; i < 10; i++) {
	    			s = fis.readLine();
	    		}
	    		
	    		while((s = fis.readLine()) != null){
	    			String[] array;
	    			array = s.split("\\s+");
	    			
	    			EdgeModel aModel = new EdgeModel();
	    			LinkModel aLink = new LinkModel(id++, Integer.parseInt(array[3]), Integer.parseInt(array[3])*20);
	    			
	    			aModel.setLinkModel(aLink);
	    			aModel.setFrom(Integer.parseInt(array[1])-1);
	    			aModel.setTo(Integer.parseInt(array[2])-1);
	    			result.add(aModel);
	    		}
	    		
	    		fis.close();
	    		fis = null;
	    		
	    	} catch (IOException e){
				System.out.println(e.getMessage());
				
			}
	    	return result;
	    }
	 
	 
	 //get knot data from server
	 public static KnotResultModel getKnotDataFromRemoteServer(String serverAddress,int rowCount, double[][] coTable){
		 KnotResultModel result = null;
		try {

			KnotInputModel input = new KnotInputModel();
			input.setRowCount(rowCount);
			input.setCoTable(coTable);

			Gson gson = new Gson();
			RestClient client = new RestClient(serverAddress);
			client.AddParam("input_model", gson.toJson(input));
			client.Execute(RequestMethod.POST);
			String resultString = client.getResponse();
			result = gson.fromJson(resultString, KnotResultModel.class);
	
			gson = null;
			client = null;
			input = null;

		} catch (Exception e) {
			System.out.println("ex = " + e.toString());
		}
		return result;
	}
	 
	 
	 public static KnotFileResultModel getKnotFileDataFromRemoteServer(String serverAddress, String prx){
		 KnotFileResultModel result = null;
		 
			try {



				Gson gson = new Gson();
				RestClient client = new RestClient(serverAddress);
				client.AddParam("prx", prx);
				client.Execute(RequestMethod.POST);
				String resultString = client.getResponse();
				result = gson.fromJson(resultString, KnotFileResultModel.class);
		
				gson = null;
				client = null;
	

			} catch (Exception e) {
				System.out.println("ex = " + e.toString());
			}
		 
		 
		 return result;
	 }

}
