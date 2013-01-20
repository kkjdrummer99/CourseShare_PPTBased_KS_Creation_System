package com.kse.knot;

import java.util.ArrayList;
import java.util.HashMap;

public class CoSimililarity implements GetCoTable{
  
	
	
	@Override
	public double[][] getCoTable(ArrayList<HashMap<String,Integer>> compareArray, ArrayList<String> selectedListTermArray){
		
		int rowCount = selectedListTermArray.size();
		double[][] coTable = new double[rowCount][rowCount];
		
		for(int i =0;i<rowCount;i++){
			for(int j =0;j<rowCount;j++){
				coTable[i][j]=0;
			}
		}
		
		for(int i=0;i<rowCount;i++){
			String left = selectedListTermArray.get(i);
			for(int j=0;j<selectedListTermArray.size();j++){
				if(i!=j){
					String right = selectedListTermArray.get(j);
					ArrayList<Integer> leftVector = getKeywordSentenceNumberArray(compareArray,left);
					ArrayList<Integer> rightVector = getKeywordSentenceNumberArray(compareArray,right);
					double simililarity = calculateCosimilarity(leftVector,rightVector);
					coTable[i][j]=simililarity;
				}
			}	
		}
		return coTable;
	}
	
	
	private double calculateCosimilarity(ArrayList<Integer> leftVector,ArrayList<Integer> rightVector){
		
		double result = 0;
		
		double sizeLeft = 0;
		double sizeRight = 0;
		
		System.out.println("leftVector = "+leftVector);
		System.out.println("rightVector = "+rightVector);
		
		
		for(int i =0;i<leftVector.size();i++){
			sizeLeft +=Math.pow(1, 2);
		}
		sizeLeft = Math.pow(sizeLeft,0.5);
		
		for(int i =0;i<rightVector.size();i++){
			sizeRight +=Math.pow(1, 2);
		}
		sizeRight = Math.pow(sizeRight,0.5);
		
	
		int maxValue = 0;
		
		for(int i =0;i<leftVector.size();i++){
			int temp =  leftVector.get(i);
			if(temp>maxValue){
				maxValue = temp;
			}
		}
		
		for(int i =0;i<rightVector.size();i++){
			int temp =  rightVector.get(i);
			if(temp>maxValue){
				maxValue = temp;
			}
		}
		
		
		double dot = 0;
		for(int i=0;i<maxValue;i++){
			if(leftVector.contains(i) && rightVector.contains(i)){
				dot+=1;
			}
		}
		
		result =(double)7-(double)6*(dot/(sizeLeft*sizeRight));
		return result;
	}
	
	
	private ArrayList<Integer> getKeywordSentenceNumberArray(ArrayList<HashMap<String,Integer>> totalSentenceArray,String keyword){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0;i<totalSentenceArray.size();i++){
			HashMap<String,Integer> aHash = totalSentenceArray.get(i);
			if(aHash.containsKey(keyword)){

				result.add(i);
			}
		}
		return result;
	}
	
}
