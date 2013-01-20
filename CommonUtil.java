import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CommonUtil {
  /**
	 * read files from data folder
	 * return text of array
	 * @return
	 */
	public ArrayList<String> ReadFilesTemp() {
		ArrayList<String> textLst = new ArrayList<String>();
		String fileAddr="data/"; // File address
		   
	    try {
	    	//Read txt     
	        File rootDir = new File(fileAddr);
	        String[] rootFile = rootDir.list();
	        	
	        //explore root directory
	        for (int i = 0; i < rootFile.length; i++) {
				String childPath = fileAddr + "/" + rootFile[i];
				File childDir = new File(childPath); 
				String[] childFile = childDir.list();
					
				if(childFile==null)
					continue;

				// explore child directories
				for (int j = 0; j < childFile.length; j++) {
					String path = childPath +"/"+ childFile[j];
					BufferedReader br = new BufferedReader(new FileReader(path));
						
					String line="";
					while ((line += br.readLine()) != null) {     
					}
			        	
					textLst.add(line);
				}
			}        	        	
	    } catch(IOException e) {
	    	e.printStackTrace();   
	    }   
	    return textLst;	   
	}
	
	public static void writeToFile(String text, String path) {
		try{
			BufferedWriter bw = new	BufferedWriter(new FileWriter(path));			
			bw.write(text+"\r\n");			
			bw.close();
		} catch(Exception e) {
			System.out.println(e);
		}
    }
	   
	public static void writeToFile(TreeMap<String,Integer> keyList, String path) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			Set set = keyList.entrySet();
			Iterator keys = set.iterator();
			while (keys.hasNext()) {
				bw.write(keys.next().toString()+"\r\n");
			}		
			bw.close();
	    } catch(Exception e) {
	    	System.out.println(e);
		}
    }
	   
	public static void writeToFile(ArrayList<String> stxt, String path) {
		try{
			BufferedWriter bw = new	BufferedWriter(new FileWriter(path));			
			bw.write(stxt.size()+"\r\n");
			for(int i=0; i<stxt.size(); i++) {
				bw.write(stxt.get(i)+"\r\n");
			}
			bw.close();
	    } catch(Exception e) {
	    	System.out.println(e);
		}
	}
}
