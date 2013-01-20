package com.kse.model;

import java.util.ArrayList;

public class KnotResultModel {
  

	private double coherence;
	private ArrayList<EdgeModel> edgeArray;
	
	
	
	public double getCoherence() {
		return coherence;
	}
	public void setCoherence(double coherence) {
		this.coherence = coherence;
	}
	public ArrayList<EdgeModel> getEdgeArray() {
		return edgeArray;
	}
	public void setEdgeArray(ArrayList<EdgeModel> edgeArray) {
		this.edgeArray = edgeArray;
	}
	
	

}
