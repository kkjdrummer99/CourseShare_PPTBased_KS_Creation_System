import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import com.kse.knot.KnotProgram;
import com.kse.model.KnotFileResultModel;
import com.kse.model.KnowledgeStructureModel;
import com.kse.util.FileUtil;

public class PPTBased_KS_CreationProcess {
  static String[] NP_regexs = {
		 "( ?(VBG)? ?(NNP?S?)+ ?(NNP?S?)+)"	//명사 2개 이상
		,"( ?(JJ)+ ?(NNP?S?)+)"	//형용사 + 명사1개 이상
		,"( ?(NNP?S?)+ ?(NNP?S?)+)"	//명사2개 이상
	};
	
	static String[] NN_regexs = {    			
		" ?(NNP?S?)+"	//명사만 1개 이상
		," ?(NNP?S?)"	//명사만 1개
	};
	
	static String fulltext = "";
	static List<String> top_list = new ArrayList<String>();
	static double[][] relationcnt;
	static double maxco;
	static int slide_len;
	static String[] slide;
	static int total_word_cnt = 0;
	static boolean initiallyPerformed = false;
	static int auto_extracted_terms_cnt = 0;
	private static ArrayList<String> words = new ArrayList<String>();
	//public static final String serverAddress="http://143.248.90.232:8080/knowledge_service/knot";
	int INDEX;
	final int TERMCNT = 50; // n(Entity) <= 50
	
	// Default Constructor
	public PPTBased_KS_CreationProcess() {
		//System.out.println("* START *");
	}
	
	public PPTBased_KS_CreationProcess(int index) {
		INDEX = index;		
		relationcnt = new double[INDEX][INDEX];
	}
		
	public void calculateKSM1() throws Exception {
		for(int i=0; i<slide_len; i++) {
			String str = slide[i];
			for(int j=0; j<INDEX-1; j++) {
				String word1 = top_list.get(j);
				for(int k=j+1; k<INDEX; k++) {
					String word2 = top_list.get(k);
					if(str != null) {
						if(str.contains(word1) && str.contains(word2))
							relationcnt[j][k] += 1.0;
					}
				}
			}
		}
				
		// 계산한 값을 7-scale로 출력
		for(int i=0; i<INDEX; i++) {
			for(int j=0; j<INDEX; j++) {
				double temp = relationcnt[i][j];
				System.out.print(relationcnt[i][j] + " ");
				if(temp > maxco)
					maxco = temp;
			}
			System.out.println();
		}
		
		System.out.println("MAX "+maxco);
		if(maxco==0.0) {
			maxco = 1.0;
		}
		
		for(int i=0; i<INDEX; i++) {
			for(int j=0; j<INDEX; j++) {
				double temp = 7.0-(6.0*(relationcnt[i][j]/maxco));
				//double temp = 7.0*(relationcnt[i][j]/maxco);
				if(temp == 0.0) {
					temp = 1.0;
				}
				relationcnt[i][j] = temp;
				System.out.print(relationcnt[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	
	public void calculateKSM2() throws Exception {
		// 같은 슬라이드 내에 있는 단어쌍에 +1
		for(int i=0; i<slide_len; i++) {
			String str = slide[i];
			for(int j=0; j<INDEX-1; j++) {
				String word1 = top_list.get(j);
				for(int k=j+1; k<INDEX; k++) {
					String word2 = top_list.get(k);
					if(str != null && str.contains(word1) && str.contains(word2)) 
						relationcnt[j][k] += 1.0;
				}
			}
		}
		
		// 앞, 뒤에 위치한 슬라이드끼리 함께 나타나는 단어쌍에 +1
		for(int i=0; i<slide_len; i++) {
			String str = slide[i];
			int currenti, lasti=slide_len-1;
			
			if(i==0) {
				currenti = 1;
				String after1 = slide[currenti];
				for(int j=0; j<INDEX-1; j++) {
					String word1 = top_list.get(j);
					for(int k=j+1; k<INDEX; k++) {
						String word2 = top_list.get(k);
						if(str != null && after1 != null && str.contains(word1) && after1.contains(word2)) 
							relationcnt[j][k] += 1.0;
					}
				}
			}
			else if(i==lasti) {
				currenti = lasti-1;
				String before1= slide[currenti];
				for(int j=0; j<INDEX-1; j++) {
					String word1 = top_list.get(j);
					for(int k=j+1; k<INDEX; k++) {
						String word2 = top_list.get(k);
						if(str != null && before1 != null && str.contains(word1) && before1.contains(word2))
							relationcnt[j][k] += 1.0;						
					}
				}
			}
			else {
				currenti = i;
				String before1 = slide[currenti-1], after1 = slide[currenti+1];
				for(int j=0; j<INDEX-1; j++) {
					String word1 = top_list.get(j);
					for(int k=j+1; k<INDEX; k++) {
						String word2 = top_list.get(k);
						if(str != null && after1 !=null && str.contains(word1) && after1.contains(word2)) 
							relationcnt[j][k] += 1.0;
						else if(str != null && before1 != null && str.contains(word1) && before1.contains(word2)) 
							relationcnt[j][k] += 1.0;						
					}
				}
			}
		}
		
		// 계산한 값을 7-scale로 출력
		for(int i=0; i<INDEX; i++) {
			for(int j=0; j<INDEX; j++) {
				double temp = relationcnt[i][j];
				System.out.print(relationcnt[i][j] + " ");
				if(temp > maxco)
					maxco = temp;
			}
			System.out.println();
		}
		
		System.out.println("MAX "+maxco);
		if(maxco==0.0) {
			maxco = 1.0;
		}
		
		for(int i=0; i<INDEX; i++) {
			for(int j=0; j<INDEX; j++) {
				double temp = 7.0-(6.0*(relationcnt[i][j]/maxco));
				//double temp = 7.0*(relationcnt[i][j]/maxco);
				if(temp == 0.0) {
					temp = 1.0;
				}
				relationcnt[i][j] = temp;
				System.out.print(relationcnt[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	public void calculateKSM3() throws Exception {
		// 같은 슬라이드 내에 있는 단어쌍에 +1
				for(int i=0; i<slide_len; i++) {
					String str = slide[i];
					for(int j=0; j<INDEX-1; j++) {
						String word1 = top_list.get(j);
						for(int k=j+1; k<INDEX; k++) {
							String word2 = top_list.get(k);
							if(str != null && str.contains(word1) && str.contains(word2)) 
								relationcnt[j][k] += 1.0;
						}
					}
				}
				
				// 현 슬라이드를 기준으로 앞 2개, 뒤 2개에 위치한 슬라이드끼리 함께 나타나는 단어쌍에 +1
				for(int i=0; i<slide_len; i++) {
					String str = slide[i];
					int currenti, lasti=slide_len-1;
					
					if(i==0) {
						currenti = 1;
						String after1 = slide[currenti], after2 = slide[currenti+1];
						for(int j=0; j<INDEX-1; j++) {
							String word1 = top_list.get(j);
							for(int k=j+1; k<INDEX; k++) {
								String word2 = top_list.get(k);
								if(str != null && after1 != null && str.contains(word1) && after1.contains(word2)) 
									relationcnt[j][k] += 1.0;
								if(str != null && after2 != null && str.contains(word1) && after2.contains(word2)) 
									relationcnt[j][k] += 1.0;
							}
						}
					}
					else if(i==1) {
						currenti = i;
						String before1 = slide[currenti-1], after1 = slide[currenti+1], after2 = slide[currenti+2];
						for(int j=0; j<INDEX-1; j++) {
							String word1 = top_list.get(j);
							for(int k=j+1; k<INDEX; k++) {
								String word2 = top_list.get(k);
								if(str != null && before1 != null && str.contains(word1) && before1.contains(word2))
									relationcnt[j][k] += 1.0;
								if(str != null && after1 != null && str.contains(word1) && after1.contains(word2))
									relationcnt[j][k] += 1.0;
								if(str != null && after2 != null && str.contains(word1) && after2.contains(word2))
									relationcnt[j][k] += 1.0;
							}
						}
					}
					else if(i==lasti-1) {
						currenti = i;
						String before1 = slide[currenti-1], before2 = slide[currenti-2], after1 = slide[currenti+1];
						for(int j=0; j<INDEX-1; j++) {
							String word1 = top_list.get(j);
							for(int k=j+1; k<INDEX; k++) {
								String word2 = top_list.get(k);
								if(str != null && before1 != null && str.contains(word1) && before1.contains(word2))
									relationcnt[j][k] += 1.0;
								if(str != null && before2 != null && str.contains(word1) && before2.contains(word2))
									relationcnt[j][k] += 1.0;
								if(str != null && after1 != null && str.contains(word1) && after1.contains(word2))
									relationcnt[j][k] += 1.0;
							}
						}
					}
					else if(i==lasti) {
						currenti = lasti-1;
						String before1= slide[currenti], before2 = slide[currenti-1];
						for(int j=0; j<INDEX-1; j++) {
							String word1 = top_list.get(j);
							for(int k=j+1; k<INDEX; k++) {
								String word2 = top_list.get(k);
								if(str != null && before1 != null && str.contains(word1) && before1.contains(word2))
									relationcnt[j][k] += 1.0;
								if(str != null && before2 != null && str.contains(word1) && before2.contains(word2))
									relationcnt[j][k] += 1.0;
							}
						}
					}
					else {
						currenti = i;
						String before1 = slide[currenti-1], before2 = slide[currenti-2], after1 = slide[currenti+1], after2 = slide[currenti+2];
						for(int j=0; j<INDEX-1; j++) {
							String word1 = top_list.get(j);
							for(int k=j+1; k<INDEX; k++) {
								String word2 = top_list.get(k);
								if(str != null && after1 !=null && str.contains(word1) && after1.contains(word2)) 
									relationcnt[j][k] += 1.0;
								if(str != null && before1 != null && str.contains(word1) && before1.contains(word2)) 
									relationcnt[j][k] += 1.0;				
								if(str != null && after2 !=null && str.contains(word1) && after2.contains(word2)) 
									relationcnt[j][k] += 1.0;
								if(str != null && before2 != null && str.contains(word1) && before2.contains(word2)) 
									relationcnt[j][k] += 1.0;						
							}
						}
					}
				}
				
				// 계산한 값을 7-scale로 출력
				for(int i=0; i<INDEX; i++) {
					for(int j=0; j<INDEX; j++) {
						double temp = relationcnt[i][j];
						System.out.print(relationcnt[i][j] + " ");
						if(temp > maxco)
							maxco = temp;
					}
					System.out.println();
				}
				
				System.out.println("MAX "+maxco);
				if(maxco==0.0) {
					maxco = 1.0;
				}
				
				for(int i=0; i<INDEX; i++) {
					for(int j=0; j<INDEX; j++) {
						double temp = 7.0-(6.0*(relationcnt[i][j]/maxco));
						//double temp = 7.0*(relationcnt[i][j]/maxco);
						if(temp == 0.0) {
							temp = 1.0;
						}
						relationcnt[i][j] = temp;
						System.out.print(relationcnt[i][j] + "\t");
					}
					System.out.println();
				}
	}
	
	/********************************************************************************
	 * Method Name : extractAllterm 
	 * Purpose     : Extract all terms at all slides in the given presentation file 
	 * Parameters  : File name, its path
	 * Return      : 1-Dimensional array of String type
	 *******************************************************************************/
		
	public String[] extractAllterm(String fname, String path) throws Exception {			
		// ppt에서 전체 텍스트를 가지고 옴
		String input = "";
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
		fis.close();		
		POIOLE2TextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
		PowerPointExtractor powerPointExtractor = (PowerPointExtractor) oleTextExtractor;

		input = powerPointExtractor.getText(); // 모든 텍스트를 추출
		input = input.toLowerCase(); // 모든 문자를 소문자로 변환
		fulltext = input;
		//CommonUtil.writeToFile(fulltext, "./output/FULLTEXT.txt");
		
		// 각 슬라이드별 텍스트를 slidetext[]에 넣기
		FileInputStream fis_t = new FileInputStream(file);
		SlideShow showppt = new SlideShow(fis_t);
		org.apache.poi.hslf.model.Slide[] slideset = showppt.getSlides();
		int len = slideset.length;
		slide_len = len;
		System.out.println("Total Slide : "+len);
		String[] strset = new String[len];
		String[] slideText = new String[len];
				
		for(int i=0; i<len; i++) {
			TextRun[] str11 = slideset[i].getTextRuns(); // 제목 + 내용
			String eachtest = "";
			for(int j=0; j<str11.length; j++) {				
				eachtest += str11[j].getText().toLowerCase().replaceAll("\n\n\n", "") + " ";
			}
			slideText[i] = eachtest;
		}
		slide = slideText;
		//System.out.println("slide text: "+slideText);
		
		// Title과 Content의 분리
		String[] titleset = new String[len];
		String titletext = "";
		String contenttext = input;
		for(int i=0; i<len; i++) {
			if(slideset[i].getTitle() != null) {
				String str = slideset[i].getTitle().toLowerCase();
				titleset[i] = str;
				titletext += str+"\n";
			}			
		}
		
		// 전체 Full Text에서 Title을 제거하여 Content 추출
		for(int i=0; i<len; i++) {
			String str = titleset[i];
			if(str != null) {
				contenttext = contenttext.replace(str, "");
			}
		}
		
		// 전체 Full Text를 대상으로 TF를 구하기 위한 Tokenizing / Stemming
		Hashtable<String, Integer> NP_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> NN_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> ALL_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> temp = new Hashtable<String, Integer>();
		Hashtable<String, Integer> mergedTemp = new Hashtable<String, Integer>();
		
		String tagString = "";
		int MAXSIZE = 20000, tokenArr_len = 0, numi = 0;
		String[][] tokenArr = new String[MAXSIZE][3]; // (Original Word)|(POS Tag)|(Lemmatization)
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		Annotation document = new Annotation(input);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
		   	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		   		// this is the text of the token
		   		tokenArr[numi][0] = (token.get(TextAnnotation.class)).toString();
		   		System.out.println("original: "+tokenArr[numi][0]);
		        
		   		// this is the POS tag of the token
		   		tokenArr[numi][1] = token.get(PartOfSpeechAnnotation.class);
		   		System.out.println("pos: "+tokenArr[numi][1]);
		   		tagString += tokenArr[numi][1]+" ";

		   		// this is the lemmatization of the token
		   		tokenArr[numi][2] = token.get(LemmaAnnotation.class);
		   		//System.out.println("lem: "+tokenArr[numi][2]);
		   		numi++;	    		
		   	}
		}
		tokenArr_len = numi;
			
		if(initiallyPerformed) {
		   	Pattern p = Pattern.compile(".*[a-z|A-Z|0-9]+.*");
		    Matcher m;
		    for(int i=0; i<tokenArr_len; i++) {
		    	m = p.matcher(tokenArr[i][0]);
		    	while(m.find()) {
		    		total_word_cnt++;
		    	}
		    }
		    initiallyPerformed = false;
		}		    
		   
		// Find words' count and put the NP/NN table
		int tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NP_regexs) {
		 	Pattern pattern = Pattern.compile(r);
		  	Matcher matcher = pattern.matcher(tagString);
		  	//NP_fq = saveWord(matcher, tokenArr, tagString);
		  	temp = saveWord(matcher, tokenArr, tagString);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, NP_fq);
			NP_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
		   	//System.out.println((++tmp)+"-Th NP_fq: "+NP_fq);
		}
		    
		temp = deleteduplicated(NP_fq);
		NP_fq.clear();
		NP_fq = (Hashtable<String, Integer>) temp.clone();
		temp.clear();
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_NP_fq.txt";
		writeToFileSorted(NP_fq, path); 
		    
		tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NN_regexs) {
		  	Pattern pattern = Pattern.compile(r);
		   	Matcher matcher = pattern.matcher(tagString);
		   	//NN_fq = saveWord(matcher, tokenArr, tagString);
		   	temp = saveWord(matcher, tokenArr, tagString);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, NN_fq);
			NN_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
		   	//System.out.println((++tmp)+"-Th NN_fq: "+NN_fq);
		}
		 
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_NN_fq.txt";
		writeToFileSorted(NN_fq, path);   
	    
		// 중복되는 단어 제거
		Set setNP = NP_fq.keySet();
		Iterator itrNP = setNP.iterator();
		while(itrNP.hasNext()) {
		  	String key_NP = (String)itrNP.next();
		   	String strNP[] = key_NP.split(" ");
		   	Set setNN = NN_fq.keySet();
		   	Iterator itrNN = setNN.iterator();
		  	//System.out.println("key_NP : "+key_NP);
		   	while(itrNN.hasNext()) {
		   		String key_NN = (String)itrNN.next();
		   		//System.out.println("key_NN : "+key_NN);
		   		if((strNP[0].trim()).equals(key_NN) || (strNP[1].trim()).equals(key_NN)) {
		   			int nvalue = NN_fq.get(key_NN) - NP_fq.get(key_NP);
		   			NN_fq.put(key_NN, nvalue);
		   		}
		   	}
		}
		    
		ALL_fq = mergeWord(NN_fq, NP_fq);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_ALL_fq.txt";
		writeToFileSorted(ALL_fq, path); 
		    
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_title_text.txt";
		writeToFile(titletext, path);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_content_text.txt";
		writeToFile(contenttext, path);
		
		return slideText;
	}
	
	public void termExtractLocation(String fname) throws Exception {
		int MAXSIZE = 20000;
		String[][] TITLE_tokenArr = new String[MAXSIZE][3];
		String[][] CONTENT_tokenArr = new String[MAXSIZE][3];
		String title_text = "", content_text = "", path = "";
		
		Hashtable<String, Integer> TITLE_NP_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> TITLE_NN_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> TITLE_ALL_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> CONTENT_NP_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> CONTENT_NN_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> CONTENT_ALL_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> temp = new Hashtable<String, Integer>();
		Hashtable<String, Integer> mergedTemp = new Hashtable<String, Integer>();
		
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_title_text.txt";
		title_text = ReadFile(path);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_content_text.txt";
		content_text = ReadFile(path);
		
		Properties props = new Properties();
		String tagString_title = "", tagString_content = "";
		int i = 0, tokenArr_len_t, tokenArr_len_c;
		
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		Annotation document_title = new Annotation(title_text);
		pipeline.annotate(document_title);
		List<CoreMap> sentences_title = document_title.get(SentencesAnnotation.class);		
		for(CoreMap sentence: sentences_title) {
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		TITLE_tokenArr[i][0] = (token.get(TextAnnotation.class)).toString();
	    		//System.out.println("tokenArrGlo[i][0]:"+tokenArrGlo[i][0]);
	    		
	    		// this is the POS tag of the token
	    		TITLE_tokenArr[i][1] = token.get(PartOfSpeechAnnotation.class);
	    		//System.out.println("pos:"+tokenArrGlo[i][1]);
	    		tagString_title += TITLE_tokenArr[i][1]+" ";
	    		
	    		TITLE_tokenArr[i][2] = token.get(LemmaAnnotation.class);
	    		//System.out.println("lem:"+tokenArrGlo[i][2]);
	    		i++;	    		
	    	}
	    }
		tokenArr_len_t = i;
		
		int tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NP_regexs) {
			Pattern pattern = Pattern.compile(r);
			Matcher matcher = pattern.matcher(tagString_title);
			//TITLE_NP_fq = saveWord(matcher, TITLE_tokenArr, tagString_title);
		  	temp = saveWord(matcher, TITLE_tokenArr, tagString_title);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, TITLE_NP_fq);
			TITLE_NP_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
			System.out.println((++tmp)+"-Th TITLE_NP_fq: "+TITLE_NP_fq);
		}
		
		temp = deleteduplicated(TITLE_NP_fq);
		TITLE_NP_fq.clear();
		TITLE_NP_fq = (Hashtable<String, Integer>) temp.clone();
		temp.clear();
		
		tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NN_regexs) {
			Pattern pattern = Pattern.compile(r);
			Matcher matcher = pattern.matcher(tagString_title);
			//TITLE_NN_fq = saveWord(matcher, TITLE_tokenArr, tagString_title);
		   	temp = saveWord(matcher, TITLE_tokenArr, tagString_title);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, TITLE_NN_fq);
			TITLE_NN_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
		   	System.out.println((++tmp)+"-Th TITLE_NN_fq: "+TITLE_NN_fq);
		}
		
		Set set1= TITLE_NP_fq.keySet();
		Iterator itr1 = set1.iterator();
		while(itr1.hasNext()) {
	    	String key_NP = (String)itr1.next();
	    	String strNP[] = key_NP.split(" ");
	    	Set set2 = TITLE_NN_fq.keySet();
	    	Iterator itr2 = set2.iterator();
	    	//System.out.println("key_NP : "+key_NP);
	    	while(itr2.hasNext()) {
	    		String key_NN = (String)itr2.next();
	    		//System.out.println("key_NN : "+key_NN);
	    		if((strNP[0].trim()).equals(key_NN) || (strNP[1].trim()).equals(key_NN)) {
	    			int nvalue = TITLE_NN_fq.get(key_NN) - TITLE_NP_fq.get(key_NP);
	    			TITLE_NN_fq.put(key_NN, nvalue);
	    		}
	    	}
	    }
		TITLE_ALL_fq = mergeWord(TITLE_NN_fq, TITLE_NP_fq);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_TITLE_ALL_fq.txt";
		writeToFileSorted(TITLE_ALL_fq, path);
		
		i = 0;
		Annotation document_content = new Annotation(content_text);
		pipeline.annotate(document_content);
		List<CoreMap> sentences_content = document_content.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences_content) {
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		CONTENT_tokenArr[i][0] = (token.get(TextAnnotation.class)).toString();
	    		//System.out.println("tokenArrGlo[i][0]:"+tokenArrGlo[i][0]);
	    		// this is the POS tag of the token
	    		CONTENT_tokenArr[i][1] = token.get(PartOfSpeechAnnotation.class);
	    		//System.out.println("pos:"+tokenArrGlo[i][1]);
	    		tagString_content += CONTENT_tokenArr[i][1]+" ";
	    		CONTENT_tokenArr[i][2] = token.get(LemmaAnnotation.class);
	    		//System.out.println("lem:"+tokenArrGlo[i][2]);
	    		i++;	    		
	    	}
	    }		
		tokenArr_len_c = i;		
		
		tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NP_regexs) {
			Pattern pattern = Pattern.compile(r);
			Matcher matcher = pattern.matcher(tagString_content);
			//CONTENT_NP_fq = saveWord(matcher, CONTENT_tokenArr, tagString_content);
		   	temp = saveWord(matcher, CONTENT_tokenArr, tagString_content);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, CONTENT_NP_fq);
			CONTENT_NP_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
		   	System.out.println((++tmp)+"-Th CONTENT_NP_fq: "+CONTENT_NP_fq);
		}
		
		temp = deleteduplicated(CONTENT_NP_fq);
		CONTENT_NP_fq.clear();
		CONTENT_NP_fq = (Hashtable<String, Integer>) temp.clone();
		temp.clear();
		
		tmp=0;
		temp=null;
		mergedTemp=null;
		for(String r : NN_regexs) {
			Pattern pattern = Pattern.compile(r);
			Matcher matcher = pattern.matcher(tagString_content);
			//CONTENT_NN_fq = saveWord(matcher, CONTENT_tokenArr, tagString_content);
		   	temp = saveWord(matcher, CONTENT_tokenArr, tagString_content);
			mergedTemp = mergeTwoHashWithBiggerValue(temp, CONTENT_NN_fq);
			CONTENT_NN_fq.putAll(mergedTemp);
			temp = null;
			mergedTemp = null;
		   	System.out.println((++tmp)+"-Th CONTENT_NN_fq: "+CONTENT_NN_fq);
		}
		
		Set set3= CONTENT_NP_fq.keySet();
		Iterator itr3 = set3.iterator();
		while(itr3.hasNext()) {
	    	String key_NP = (String)itr3.next();
	    	String strNP[] = key_NP.split(" ");
	    	Set set4 = CONTENT_NN_fq.keySet();
	    	Iterator itr4 = set4.iterator();
	    	//System.out.println("key_NP : "+key_NP);
	    	while(itr4.hasNext()) {
	    		String key_NN = (String)itr4.next();
	    		//System.out.println("key_NN : "+key_NN);
	    		if((strNP[0].trim()).equals(key_NN) || (strNP[1].trim()).equals(key_NN)) {
	    			int nvalue = CONTENT_NN_fq.get(key_NN) - CONTENT_NP_fq.get(key_NP);
	    			CONTENT_NN_fq.put(key_NN, nvalue);
	    		}
	    	}
	    }
		CONTENT_ALL_fq = mergeWord(CONTENT_NN_fq, CONTENT_NP_fq);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_CONTENT_ALL_fq.txt";
		writeToFileSorted(CONTENT_ALL_fq, path);	
	}
	
	public void weightedTerm(String fname) {
		
		Hashtable<String, Integer> ALL_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> TITLE_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> CONTENT_fq = new Hashtable<String, Integer>();
		
		Hashtable<String, Integer> NP_fq = new Hashtable<String, Integer>();
		Hashtable<String, Integer> NN_fq = new Hashtable<String, Integer>();
		
		Hashtable<String, Double> ALL_Nfq = new Hashtable<String, Double>();
		Hashtable<String, Double> TITLE_Nfq = new Hashtable<String, Double>();
		Hashtable<String, Double> CONTENT_Nfq = new Hashtable<String, Double>();
		
		Hashtable<String, Double> WeightedHash = new Hashtable<String, Double>();
		Hashtable<String, Integer> intWeightedHash = new Hashtable<String, Integer>();
				
		List<String> list_all = new ArrayList<String>();
		List<String> list_title = new ArrayList<String>();
		List<String> list_content = new ArrayList<String>();
		List<String> list_weighted = new ArrayList<String>();
		
		int a,b,c;
		String path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_ALL_fq.txt";	
		ALL_fq = intoHash(path, ALL_fq);
		System.out.println("ALL_fq: "+ALL_fq);
		
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_TITLE_ALL_fq.txt";		
		TITLE_fq = intoHash(path, TITLE_fq);
		System.out.println("TITLE_fq: "+TITLE_fq);
		
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_CONTENT_ALL_fq.txt";		
		CONTENT_fq = intoHash(path, CONTENT_fq);
		System.out.println("CONTENT_fq: "+CONTENT_fq);
		
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_NP_fq.txt";	
		NP_fq = intoHash(path, NP_fq);
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_NN_fq.txt";	
		NN_fq = intoHash(path, NN_fq);

		String[] deletelist = {"example", "water", "result", "number", "rock", "problem", "step", "blood type", "value", 
				"node", "vertex", "set", "object", "model", "point", "record", "method", "attack", "neighbor", "worm", 
				"content", "approach", "analysis", "method case", "case", "study", "limitation", "advantage", 
				"disadvantage", "importance", "overview", "rule", "over view", "system", "split", "birth", "income",
				"round", "chance", "unit", "age", "distance d", "distance dk", "distance k",
				"event", "item", "element", "e1 e2", "k-1", "time", "mining", "algorithm",
				"c d", "abc", "sequence w1", "candidate k-sequence", "tree"};
		for(int ii=0; ii<deletelist.length; ii++) {
			if(ALL_fq.containsKey(deletelist[ii])) {
				ALL_fq.remove(deletelist[ii]);
			}
			if(TITLE_fq.containsKey(deletelist[ii])) {
				TITLE_fq.remove(deletelist[ii]);
			}
			if(CONTENT_fq.containsKey(deletelist[ii])) {
				CONTENT_fq.remove(deletelist[ii]);
			}
		}

		intWeightedHash = putIntWeightedhash(ALL_fq, TITLE_fq, CONTENT_fq, intWeightedHash);
		intWeightedHash = intWeightedHash(1,4,1,ALL_fq,TITLE_fq,CONTENT_fq,intWeightedHash);
		
		a = findMAX(ALL_fq);
		b = findMAX(TITLE_fq);
		c = findMAX(CONTENT_fq);
		
		list_all = printSortedList(ALL_fq, list_all);
		list_title = printSortedList(TITLE_fq, list_title);
		list_content = printSortedList(CONTENT_fq, list_content);
		
		//weightedHash = weightedHash(1,2,1,ALL_fq,TITLE_fq,CONTENT_fq,weightedHash);
		//System.out.println("WEIGHTED HASH: "+weightedHash);
		
		ALL_Nfq = normalize_TF(a,ALL_fq,ALL_Nfq);		
		System.out.println(ALL_Nfq);
		TITLE_Nfq = normalize_TF(a,TITLE_fq,TITLE_Nfq);
		System.out.println(TITLE_Nfq);
		CONTENT_Nfq = normalize_TF(a,CONTENT_fq,CONTENT_Nfq);
		System.out.println(CONTENT_Nfq);
				
		WeightedHash = putWeightedhash(ALL_Nfq, TITLE_Nfq, CONTENT_Nfq, WeightedHash);
		WeightedHash = weightedHash(1,4,1,ALL_Nfq,TITLE_Nfq,CONTENT_Nfq,WeightedHash);
		System.out.println("WEIGHTED HASH: "+WeightedHash);
		
		//list_weighted = printSortedDoubleList(WeightedHash, list_weighted);
		list_weighted = printSortedIntList(intWeightedHash, list_weighted);
						
		path = "C:/workspace/PPTBased_KS_CreationProcess/output/"+fname+"_WEIGHTED_ALL_fq.txt";	
		
		try{
			BufferedWriter bw = new	BufferedWriter(new FileWriter(path));			
			//bw.write(list_weighted.size()+"\n");
			bw.write("");
			for(int i=0; i<list_weighted.size(); i++) {
				if(WeightedHash.containsKey(list_weighted.get(i))) {
					bw.write(list_weighted.get(i)+" = "+intWeightedHash.get(list_weighted.get(i)));
					bw.newLine();
				}
			}
			bw.close();
		}catch(Exception e){
		    System.out.println(e);
		}
		
		System.out.println("***ALL***");
		System.out.println(list_all);
		System.out.println("***TITLE***");
		System.out.println(list_title);
		System.out.println("***CONTENT***");
		System.out.println(list_content);
		System.out.println("***WEIGHTED***");
		System.out.println(list_weighted);
		ArrayList deflist = new ArrayList();
		deflist = generateDEF(NP_fq, NN_fq);
		System.out.println("***TOP "+INDEX+"***");
		for(int i=0; i<INDEX; i++) {
			top_list.add(list_weighted.get(i));
		}	
	}	
	
	// String에 담긴 내용을 txt 파일에 쓰는 메소드
	public static void writeToFile(String text, String path){		
		   try{
			   BufferedWriter bw = new	BufferedWriter(new FileWriter(path));			
			   bw.write(text+"\r\n");//				
			   bw.close();
		   }catch(Exception e){
			   System.out.println(e);
		   }
	  }
	
	// HashTable에 들어있는 내용을 TF를 바탕으로 내림차순으로 파일에 쓰는 메소드
	public static void writeToFileSorted(Hashtable<String, Integer> write_hash, String path) {
		List<Map.Entry<String, Integer>> myArrayList = new ArrayList<Map.Entry<String, Integer>>(write_hash.entrySet());
		Collections.sort(myArrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				if(e1.getValue() == e2.getValue())
					return e1.getKey().compareTo(e2.getKey());
				else
					return e2.getValue().compareTo(e1.getValue());
			}			
		});
		
		Iterator itr = myArrayList.iterator();
		String key = "";
		int value;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			while(itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				key = (String)e.getKey();
				value = (Integer) e.getValue();
				bw.write(key+" = "+value+"\r\n");
			}
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Hashtable<String, Integer> saveWord(Matcher matcher, String[][] tokenArr, String tagString) throws Exception {
		Hashtable<String, Integer> savehash = new Hashtable<String, Integer>();
		String str = "";
		int cnt;
		
		while(matcher.find()) {
			int shortStrCnt = tagString.substring(matcher.start()+1, matcher.end()-1).split("\\ ",-1).length-1;
			int longStrCnt = tagString.substring(0, matcher.end()-1).split("\\ ",-1).length-1;
  	  		  	  		
  	  		for(int i = longStrCnt - shortStrCnt; i<=longStrCnt; i++) {
  	  			if(tokenArr[i][1].equals("NNS") && !(tokenArr[i][2].equals("datum")) && !(tokenArr[i][2].equals("ginus"))) {
  	  				str += tokenArr[i][2] + " ";
  	  			}
  	  			else {
  	  				str += tokenArr[i][0]+" ";
  	  			}
  	  		}
  	  		str = str.trim();
  	  		
  	  		if(isDeleted(str)) {
  	  			str = "";
  	  		}
  	  		if(str.contains("|") || str.contains("<") || str.contains(">")|| str.contains("=") || str.contains("_")
	  				|| str.contains("%") || str.contains("≥")) {
	  			str = "";
	  		}
  	  		if(!(str.equals(""))) {
	  			if(savehash.containsKey(str)) {
	  				cnt = savehash.get(str);
	  				cnt++;
	  				savehash.put(str, cnt);
	  			}
	  			else {
	  				savehash.put(str, 1);
	  			}
	  		}
  	  		
  	  		str = "";
		}		
		return savehash;
	}
	
	public static Hashtable<String, Integer> mergeWord(Hashtable<String, Integer> NN_hash, Hashtable<String, Integer> NP_hash) throws Exception {
		Hashtable<String, Integer> mergedhash = new Hashtable<String, Integer>();
		Set setNN = NN_hash.keySet();
		Set setNP = NP_hash.keySet();
		Iterator itrNN = setNN.iterator();
		Iterator itrNP = setNP.iterator();
		
		while(itrNN.hasNext()) {
			String NN_key = (String)itrNN.next();
			int NN_value = NN_hash.get(NN_key);
			if(!(mergedhash.containsKey(NN_key))) {
				mergedhash.put(NN_key, NN_value);
			}
		}
		
		while(itrNP.hasNext()) {
			String NP_key = (String)itrNP.next();
			int NP_value = NP_hash.get(NP_key);
			if(!(mergedhash.containsKey(NP_key))) {
				mergedhash.put(NP_key, NP_value);
			}
		}		
		
		return mergedhash;
	}
	
	public static Hashtable<String, Integer> deleteduplicated(Hashtable<String, Integer> nphash) {
		Hashtable<String, Integer> nhash = new Hashtable<String, Integer>();
		Set seth = nphash.keySet();
		Iterator itr = seth.iterator();
		
		while(itr.hasNext()) {
			String key = (String)itr.next();
			int value = nphash.get(key);
			String[] keyarr = key.split(" ");
			if(!((keyarr[0].trim()).equals((keyarr[1].trim())))) {
				nhash.put(key, value);
			}
		}		
		return nhash;
	}
	
	public static boolean isDeleted(String s) {
		boolean del = false;
		Pattern pattern1 = Pattern.compile("[a-z]");
		Pattern pattern2 = Pattern.compile("[0-9]");
		Pattern pattern3 = Pattern.compile("[a-z].");

		if(pattern1.matcher(s).matches()) {
			del = true;
		}
		else if(pattern2.matcher(s).matches()) {
			del = true;
		}
		else if(pattern3.matcher(s).matches()) {
			del = true;
		}

		return del;
	}
	
	public String ReadFile(String path) {
		String input = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null) {
				input += line;
				input += "\n";
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public ArrayList generateDEF(Hashtable<String, Integer> nphash, Hashtable<String, Integer> nnhash) {
		List<String> listnp = new ArrayList<String>();
		List<String> listnn = new ArrayList<String>();
		Set setnp = nphash.keySet();
		Set setnn = nnhash.keySet();
		Iterator itrnp = setnp.iterator();
		Iterator itrnn = setnn.iterator();
		while(itrnp.hasNext()) {
			String key = (String)itrnp.next();
			listnp.add(key);
		}
		while(itrnn.hasNext()) {
			String key = (String)itrnn.next();
			listnn.add(key);
		}
		int listnp_len = listnp.size(), listnn_len = listnn.size();
		ArrayList alldef = new ArrayList();
		for(int i=0; i<slide_len; i++) {
			String str = slide[i];
			for(int j=0; j<listnp_len; j++) {
				String t1 = listnp.get(j);
				String temp = "";
				for(int k=0; k<listnn_len; k++) {
					String t2 = listnn.get(k);
					int inum = 1;
					if((str != null) && (t1 != null) && (t2 != null)) {
						if((str.contains(t1)) && (str.contains(t2)) && (t1.contains(t2))) {
							//System.out.println("DEF : "+t1+" = "+t2);
							temp+=t2;
							temp+="*";
						}
					}
				}
				if(!(temp.equals(""))) {
					ArrayList tlist = new ArrayList();
					tlist.add(t1);
					tlist.add(temp);
					alldef.add(tlist);
				}
			}
		}
		return alldef;
	}
	
	public Hashtable<String, Integer> intoHash(String path, Hashtable<String, Integer> hash) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null) {
				String[] oline;
				oline = line.split(" = ");
				hash.put(oline[0].toString(), Integer.parseInt(oline[1]));
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return hash;
	}
	
	public Hashtable<String, Double> normalize_TF(int max, Hashtable<String, Integer> hash, Hashtable<String, Double> nhash) {
		double new_value;
		double new_max = (Double.parseDouble(Integer.toString(max)));
		Set sethash = hash.keySet();		
		Iterator itr = sethash.iterator();
		while(itr.hasNext()) {
			String key = (String)itr.next();
			int value = hash.get(key);
			double dvalue = (Double.parseDouble(Integer.toString(value)));
			new_value = dvalue/new_max;
			nhash.put(key, new_value);
		}
		
		return nhash;
	}

	public List<String> printSortedList(Hashtable hash, List<String> list) {
		// Put keys and values in to an arraylist using entryset
		List<Map.Entry<String, Integer>> myArrayList = new ArrayList<Map.Entry<String, Integer>>(hash.entrySet());
		// Sort the values based on values first and then keys
		Collections.sort(myArrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				if(e1.getValue() == e2.getValue())
					return e1.getKey().compareTo(e2.getKey());
				else
					return e2.getValue().compareTo(e1.getValue());
			}
		});	
		
		// Show sorted results
		Iterator itr = myArrayList.iterator();
		String key = "";
		String value = "";
		int cnt = 0;
		list.clear();
		while(itr.hasNext()) {
			cnt++;
			Map.Entry e = (Map.Entry)itr.next();
			key = (String)e.getKey();
			value = (e.getValue()).toString();
			//System.out.println(key.trim() + ", " + value);
			if(cnt<=TERMCNT) {
				list.add(key);
			}
		}		
		
		return list;
	}
	
	public List<String> printSortedDoubleList(Hashtable hash, List<String> list) {
		// Put keys and values in to an arraylist using entryset
		List<Map.Entry<String, Double>> myArrayList = new ArrayList<Map.Entry<String, Double>>(hash.entrySet());
		
		// Sort the values based on values first and then keys
		Collections.sort(myArrayList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
				if(e1.getValue() == e2.getValue())
					return e1.getKey().compareTo(e2.getKey());
				else
					return e2.getValue().compareTo(e1.getValue());
			}
		});		
		
		// Show sorted results
		Iterator itr = myArrayList.iterator();
		String key = "";
		String value = "";
		int cnt = 0;
		list.clear();
		
		while(itr.hasNext()) {
			cnt++;
			Map.Entry e = (Map.Entry)itr.next();
			key = (String)e.getKey();
			value = (e.getValue()).toString();
			//System.out.println(key.trim() + ", " + value);
			if(cnt<=TERMCNT) {
				list.add(key);
			}
		}
		return list;
	}
	
	public List<String> printSortedIntList(Hashtable hash, List<String> list) {
		// Put keys and values in to an arraylist using entryset
		List<Map.Entry<String, Integer>> myArrayList = new ArrayList<Map.Entry<String, Integer>>(hash.entrySet());
		
		// Sort the values based on values first and then keys
		Collections.sort(myArrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				if(e1.getValue() == e2.getValue())
					return e1.getKey().compareTo(e2.getKey());
				else
					return e2.getValue().compareTo(e1.getValue());
			}
		});
		
		// Show sorted results
		Iterator itr = myArrayList.iterator();
		String key = "";
		String value = "";
		int cnt = 0;
		list.clear();
		
		while(itr.hasNext()) {
			cnt++;
			Map.Entry e = (Map.Entry)itr.next();
			key = (String)e.getKey();
			value = (e.getValue()).toString();
			//System.out.println(key.trim() + ", " + value);
			
			// n(Entity)<=50 && Entity_Count >= 3
			if(cnt<=TERMCNT && Integer.parseInt(value)>=3) {
				list.add(key);
			}
		}
		
		auto_extracted_terms_cnt = list.size();
		return list;
	}

	public Hashtable<String, Integer> putIntWeightedhash(Hashtable<String, Integer> h1, Hashtable<String, Integer> h2, Hashtable<String, Integer> h3, Hashtable<String, Integer> h4) {
		Set set1 = h1.keySet(), set2 = h2.keySet(), set3 = h3.keySet();
		Iterator itr1 = set1.iterator(), itr2 = set2.iterator(), itr3 = set3.iterator();
		while(itr1.hasNext()) {
			String key = (String)itr1.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0);
			}
		}
		while(itr2.hasNext()) {
			String key = (String)itr2.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0);
			}
		}
		while(itr3.hasNext()) {
			String key = (String)itr3.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0);
			}
		}
		return h4;
	}
		
	
	public Hashtable<String, Double> putWeightedhash(Hashtable<String, Double> h1, Hashtable<String, Double> h2, Hashtable<String, Double> h3, Hashtable<String, Double> h4) {
		Set set1 = h1.keySet(), set2 = h2.keySet(), set3 = h3.keySet();
		Iterator itr1 = set1.iterator(), itr2 = set2.iterator(), itr3 = set3.iterator();
		while(itr1.hasNext()) {
			String key = (String)itr1.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0.0);
			}
		}
		while(itr2.hasNext()) {
			String key = (String)itr2.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0.0);
			}
		}
		while(itr3.hasNext()) {
			String key = (String)itr3.next();
			if(!(h4.contains(key))) {
				h4.put(key, 0.0);
			}
		}
		return h4;
	}
	
	public int findMAX(Hashtable<String, Integer> hash) {
		int max = 0;		
		Set sethash = hash.keySet();
		Iterator itr = sethash.iterator();
		
		while(itr.hasNext()) {
			String key = (String)itr.next();
			int value = hash.get(key);
			if(value > max) {
				max = value;
			}
		}		
		return max;
	}
	
	public Hashtable<String, Integer> intWeightedHash(int a, int b, int c, Hashtable<String, Integer> h1, Hashtable<String, Integer> h2, Hashtable<String, Integer> h3, Hashtable<String, Integer> h4) {
		Set sethash = h4.keySet();
		Iterator itr = sethash.iterator();
		
		while(itr.hasNext()) {
			String key = (String)itr.next();
			int value1 = 0, value2 = 0, value3 = 0;
			int tvalue = 0;
			if(h1.containsKey(key)) {
				value1 = h1.get(key);
			}
			if(h2.containsKey(key)) {
				value2 = h2.get(key);
			}
			if(h3.containsKey(key)) {
				value3 = h3.get(key);
			}
			tvalue = a*value1 + b*value2 + c*value3;
			System.out.println(tvalue);
			h4.put(key, tvalue);
		}		
		return h4;		
	}	
	
	public Hashtable<String, Double> weightedHash(double a, double b, double c, Hashtable<String, Double> h1, Hashtable<String, Double> h2, Hashtable<String, Double> h3, Hashtable<String, Double> h4) {
		Set sethash = h4.keySet();
		Iterator itr = sethash.iterator();
		
		while(itr.hasNext()) {
			String key = (String)itr.next();
			double value1 = 0.0, value2 = 0.0, value3 = 0.0;
			double tvalue = 0.0;
			if(h1.containsKey(key)) {
				value1 = h1.get(key);
			}
			if(h2.containsKey(key)) {
				value2 = h2.get(key);
			}
			if(h3.containsKey(key)) {
				value3 = h3.get(key);
			}
			tvalue = a*value1 + b*value2 + c*value3;
			h4.put(key, tvalue);
		}		
		return h4;		
	}	
	
	// double[][] 행렬에 들어있는 relationship의 값을 가져와서 각 노드간 weight 계산 및 KNOT 툴에 맞는 input type인
	// .prx 파일로 써주는 메소드
	public void writeToFilePRX(int nodeLength) throws Exception {
		try {
			String inputFile = FileUtil.getRootPath()+"/KNOTData/data.prx";
			
			BufferedWriter writePRX = new BufferedWriter(new FileWriter(inputFile));
			writePRX.write("data"); writePRX.newLine();
			writePRX.write("distances"); writePRX.newLine();
			writePRX.write(nodeLength +" nodes"); writePRX.newLine();
			writePRX.write("0 decimal places"); writePRX.newLine();
			writePRX.write("1 minimum"); writePRX.newLine();
			writePRX.write("7 maximum"); writePRX.newLine();
			writePRX.write("upper triangle:"); writePRX.newLine();
						
			// Upper triangle symmetric matrix
			for(int i=0; i<INDEX; i++) {
				for(int j=i; j<INDEX; j++) {
					writePRX.write(relationcnt[i][j] + "\t");
				}
				writePRX.newLine();
			}
			
			writePRX.close();
			
			String input = FileUtil.readStringFromFileWithLineSapartor(inputFile);
						
			//Request KNOT Result 
			KnotFileResultModel result = KnotProgram.getKnotFileDataFromRemoteServer(KnowledgeStructureModel.KNOT_FILE_SERVER_ADDRESS,input);
			
			//Response KNOT Result
			String prx = result.getPrx();
			String pf = result.getPf();
			String out = result.getOut();

			//Write Result File
			FileUtil.writeStringToFile(prx, FileUtil.getRootPath()+"/KNOTData/data_prx.prx");
			FileUtil.writeStringToFile(pf, FileUtil.getRootPath()+"/KNOTData/data_pf.pf");
			FileUtil.writeStringToFile(out, FileUtil.getRootPath()+"/KNOTData/data_out.out");
		} catch (Exception e) {
	        System.err.println(e); // 에러가 있다면 메시지 출력
		}
	}
	
	public void extractKeywords(String fname, String path) throws Exception {		
		try {		
			PPTBased_KS_CreationProcess pe2 = new PPTBased_KS_CreationProcess();		
			
			String[] slideText = pe2.extractAllterm(fname, path);
			pe2.termExtractLocation(fname);
			pe2.weightedTerm(fname);		
		} catch(Exception e) {
			System.err.println(e); // 에러가 있다면 메시지 출력	
		}
	}
	
	public void calculationKSAndWriteToPRX(String fname, String path, int noOfTerm) throws Exception {		
		try {
			PPTBased_KS_CreationProcess pe2 = new PPTBased_KS_CreationProcess(noOfTerm);		
			
			String[] slideText = pe2.extractAllterm(fname, path);
			pe2.termExtractLocation(fname);
			pe2.weightedTerm(fname);
			pe2.calculateKSM1();
			pe2.writeToFilePRX(relationcnt.length);
						
		} catch(Exception e) {
			System.err.println(e); // 에러가 있다면 메시지 출력	
		}
	}
	
	// Merge Two Hash
	public Hashtable<String, Integer> mergeTwoHashWithBiggerValue(Hashtable<String, Integer> left, Hashtable<String, Integer> right){
		Hashtable<String, Integer> result = new Hashtable<String, Integer>();
		
		// Put All Left Value
		result.putAll(right);
		
		Set<String> setLeftNP = left.keySet();
	    Iterator<String> itrLeftNP = setLeftNP.iterator();
	    while(itrLeftNP.hasNext()) {
	    	String key = (String)itrLeftNP.next();
	    	int leftValue = left.get(key);
	    	
	    	if(right.get(key)!= null){
	    		int rightValue = right.get(key);
	    		int biggerValue = leftValue;
	    		if(leftValue<rightValue){
	    			biggerValue = rightValue;
	    		}
	    		result.put(key, biggerValue);
	    	}
	    	else{
	    		result.put(key, leftValue);
	    	}
	    }
	    return result;
	}		
}
