package com.kse.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.kse.model.KnowledgeProjectModel;

public class FileUtil {
  
	// get root folder
	public static String getRootPath() {
		File file = new File(".");
		String result = "";

		try {

			result = file.getCanonicalPath().toString();
		} catch (Exception e) {
			System.out.println("exception getRootPath = " + e.toString());
		}
		return result;
	}

	// save model
	public static boolean saveModel(String filePath,KnowledgeProjectModel source) {
		boolean result = false;

		try {
			Gson gson = new Gson();
			FileUtil.writeStringToFile(gson.toJson(source), filePath);
			result = true;
			gson = null;
			
		} catch (Exception e) {
			System.out.println("ex saveModel = " + e.toString());
		}
		return result;
	}

	// load saved model
	public static KnowledgeProjectModel loadModel(String filePath) {
		KnowledgeProjectModel result = null;

		try {
			String source = FileUtil.readStringFromFile(filePath);
			Gson gson = new Gson();
			result = gson.fromJson(source, KnowledgeProjectModel.class);

		} catch (Exception e) {
			result = null;
			System.out.println("ex loadModel = " + e.toString());
		}
		return result;
	}

	// write string to file
	public static void writeStringToFile(String text, String path) {
		
		try {
			FileUtil.deleteFile(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			bw.write(text);
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// read string from file
	public static String readStringFromFile(String path) {
		String text = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				text += (line+"\n");
			}

			br.close();
			br = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	// read string from file
	public static String readStringFromFileWithLineSapartor(String path) {
		String text = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				text += (line+"\r\n");
			}

			br.close();
			br = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	// delete file
	public static boolean deleteFile(String filePath) {

		boolean result = false;

		try {
			File target = new File(filePath);

			if (target.exists()) {
				target.delete();
				result = true;
			}
		} catch (Exception e) {
			System.out.println("ex delete file =" + e.toString());
			result = false;
		}
		return result;
	}

	// move file
	public static boolean moveFile(String from, String to) {

		boolean result = false;

		try {

			File fromFile = new File(from);
			File toFile = new File(to);
			fromFile.renameTo(toFile);
			result = true;
		} catch (Exception e) {
			System.out.println("ex moveFile file =" + e.toString());
			result = false;
		}
		return result;
	}

	// copy file
	public static boolean copyFile(String from, String to) {

		boolean result = false;

		try {
			FileInputStream fis = new FileInputStream(from);
			FileOutputStream fos = new FileOutputStream(to);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

			fis = null;
			fos = null;

			result = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ex copyFile file =" + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	   
	   
	//get file header name 
	public static String getFileHeaderName(String filePath) {
		String result = null;
		try {
			File file = new File(filePath);
			String fileName = file.getName();
			int pos = fileName.lastIndexOf(".");
			result = fileName.substring(0, pos);
			file = null;

		} catch (Exception e) {
			System.out.println("getFileHeaderName exception = " + e.toString());
		}
		return result;
	}
	
	//read string from file in low case
	public static String extractStringInLowcaseFromTextFile(String filePath){
		String result = null;
		try{		
			String s = FileUtil.readStringFromFile(filePath);
			
			if(s.trim().length()>0){
				result = s.toLowerCase();	
			}
			s = null;
		}catch(Exception e){
			System.out.println("Exception = "+e.toString());
			result = null;
		}
		return result;
	}
	
	//read string with line from file
	public static ArrayList<String> readLineStringFromFile(String filePath){
		
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if(!result.contains(line)){
					result.add(line);
				}
			}

			br.close();
			br = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	   

}
