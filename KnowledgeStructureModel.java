package com.kse.model;

import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeStructureModel {
  public static final String GRAPH_KIND_DOCUMENT = "GRAPH_KIND_DOCUMENT";
	public static final String GRAPH_KIND_APPLICATION = "GRAPH_KIND_APPLICATION";
	
	
	public static final String KNOT_SERVER_ADDRESS="http://143.248.90.232:8080/knowledge_service/knot";
	public static final String KNOT_FILE_SERVER_ADDRESS="http://143.248.90.232:8080/knowledge_service/knot_file";
	

	public static final String ICON_SERVER_ADDRESS="http://143.248.90.11:8080/alfred_image/upload_file";
	
	
	
	private String fileName;


	private HashMap<String,Integer> allTermsHash;
	private ArrayList<HashMap<String,Integer>> sentenceArray;
	private ArrayList<HashMap<String,Integer>> paragraphArray;
	private ArrayList<KnowledgeExcuteGraphModel> excuteArray;
	
	private String sourceString;
	

	private long structureId;


	private String graphKind;
	
	
	

	

	public String getGraphKind() {
		return graphKind;
	}


	public void setGraphKind(String graphKind) {
		this.graphKind = graphKind;
	}


	public long getStructureId() {
		return structureId;
	}


	public void setStructureId(long structureId) {
		this.structureId = structureId;
	}


	public KnowledgeStructureModel(long structureId,String fileName){
		this.structureId = structureId;
		this.fileName = fileName;
		this.excuteArray = new ArrayList<KnowledgeExcuteGraphModel>(); 
	}
	

	public String getSourceString() {
		return sourceString;
	}




	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}




	public static String getServeraddress() {
		return KNOT_SERVER_ADDRESS;
	}


	public String getFileNameWithoutExtension() {
		return fileName;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashMap<String, Integer> getAllTermsHash() {
		return allTermsHash;
	}
	public void setAllTermsHash(HashMap<String, Integer> allTermsHash) {
		this.allTermsHash = allTermsHash;
	}
	public ArrayList<HashMap<String, Integer>> getSentenceArray() {
		return sentenceArray;
	}
	public void setSentenceArray(ArrayList<HashMap<String, Integer>> sentenceArray) {
		this.sentenceArray = sentenceArray;
	}
	public ArrayList<HashMap<String, Integer>> getParagraphArray() {
		return paragraphArray;
	}
	public void setParagraphArray(ArrayList<HashMap<String, Integer>> paragraphArray) {
		this.paragraphArray = paragraphArray;
	}
	public ArrayList<KnowledgeExcuteGraphModel> getExcuteArray() {
		return excuteArray;
	}
	public void setExcuteArray(ArrayList<KnowledgeExcuteGraphModel> excuteArray) {
		this.excuteArray = excuteArray;
	}
	
	public String toString(){
		return this.fileName;
	}
	
}
