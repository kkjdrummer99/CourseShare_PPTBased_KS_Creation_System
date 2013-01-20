package com.kse.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
  
	public String downloadHtmlWithAgentString(String address) throws Exception{ 

	
		String result = "";
        URLConnection conn = null; 
        InputStream  in = null; 

        try { 
            URL url = new URL(address); 
            conn = url.openConnection(); 

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            
            
            conn.setReadTimeout(5*60000);
            conn.setConnectTimeout(30000);
            in = conn.getInputStream(); 
           
            
            
            
            byte[] buffer = new byte[1024]; 
            int numRead; 
            long numWritten = 0; 
            while ((numRead = in.read(buffer)) != -1) {
                numWritten += numRead; 
                String line = new String(buffer, 0, numRead);
                result+=line;
            } 
            
            if(numWritten==0){
            	result = null;
            }
 
 
        } catch (Exception exception) { 
            exception.printStackTrace(); 
            result = null;
        } finally { 
            try { 
                if (in != null) { 
                    in.close(); 
                } 
            } catch (IOException ioe) { 
            	result = null;
            } 
        } 
        return result;
    } 
	
	
	
public int downloadPPT(String address, String localFileName) throws Exception{ 
		
		int result = 0;
        OutputStream out = null; 
        URLConnection conn = null; 
        InputStream  in = null; 
        
        try { 
            URL url = new URL(address); 
            out = new BufferedOutputStream(new FileOutputStream(localFileName)); 
            conn = url.openConnection(); 
            conn.setReadTimeout(5*60000);
            conn.setConnectTimeout(20000);
            in = conn.getInputStream(); 
            byte[] buffer = new byte[1024]; 
            int numRead; 
            long numWritten = 0; 
            while ((numRead = in.read(buffer)) != -1) { 
                out.write(buffer, 0, numRead); 
                numWritten += numRead; 
            } 
            result = (int)(numWritten/1024);
 
        } catch (Exception exception) { 
            exception.printStackTrace(); 
            System.out.println("Exception download file e = "+exception.toString());
            result = 0;
        } finally { 
            try { 
                if (in != null) { 
                    in.close(); 
                } 
                if (out != null) { 
                    out.close(); 
                } 
            } catch (IOException ioe) { 
            	result = 0;
            } 
        } 

        return result;
    } 
	
	
	public void deleteFile(String fileName) throws Exception{

	    File f = new File(fileName);
	    if (f.delete()) {

	    } else {
	      System.err.println("Cannot delete -" + fileName);
	    }
	}

	

}
