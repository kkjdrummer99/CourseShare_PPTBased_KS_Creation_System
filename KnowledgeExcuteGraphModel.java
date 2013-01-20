package com.kse.model;

import java.util.ArrayList;
import java.util.Date;

public class KnowledgeExcuteGraphModel {
  

	
	private ArrayList<String> selectedTermArray;
	private ArrayList<String> selectedTermWithCountArray;
	
	private ArrayList<EdgeModel> edgeArray;
	private double coherence;
	private Date regDate;
	private String excuteName;
	private String coTableKind;
	
	private long excuteId;
	
	private String graphKind;
	
	
	
	
	





	public KnowledgeExcuteGraphModel(long excuteId,String excuteName){
		this.excuteId = excuteId;
		this.excuteName = excuteName;
	}
	
	
	
	
	public String getCoTableKind() {
		return coTableKind;
	}
	public void setCoTableKind(String coTableKind) {
		this.coTableKind = coTableKind;
	}
	public ArrayList<String> getSelectedTermWithCountArray() {
		return selectedTermWithCountArray;
	}

	public void setSelectedTermWithCountArray(
			ArrayList<String> selectedTermWithCountArray) {
		this.selectedTermWithCountArray = selectedTermWithCountArray;
	}

	public long getExcuteId() {
		return excuteId;
	}
	public void setExcuteId(long excuteId) {
		this.excuteId = excuteId;
	}

	public ArrayList<String> getSelectedTermArray() {
		return selectedTermArray;
	}
	public void setSelectedTermArray(ArrayList<String> selectedTermArray) {
		this.selectedTermArray = selectedTermArray;
	}
	public ArrayList<EdgeModel> getEdgeArray() {
		return edgeArray;
	}
	public void setEdgeArray(ArrayList<EdgeModel> edgeArray) {
		this.edgeArray = edgeArray;
	}
	public double getCoherence() {
		return coherence;
	}
	public void setCoherence(double coherence) {
		this.coherence = coherence;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getExcuteName() {
		return excuteName;
	}
	public void setExcuteName(String excuteName) {
		this.excuteName = excuteName;
	}

	public String getGraphKind() {
		return graphKind;
	}
	
	public void setGraphKind(String graphKind) {
		this.graphKind = graphKind;
	}
	

	public String toString(){
		return this.excuteName;
	}
	

}
