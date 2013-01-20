package com.kse.knot;

import java.util.ArrayList;
import java.util.HashMap;

public class CoOccurence implements GetCoTable{

  @Override
	public double[][] getCoTable(ArrayList<HashMap<String, Integer>> compareArray,ArrayList<String> selectedListTermArray) {
		int rowCount = selectedListTermArray.size();
		double[][] coTable = new double[rowCount][rowCount];
		
		for(int i =0;i<rowCount;i++){
			for(int j =0;j<rowCount;j++){
				coTable[i][j]=0;
			}
		}
		
		for(int i=0;i<selectedListTermArray.size();i++){
			String left = selectedListTermArray.get(i);
			for(int j=0;j<selectedListTermArray.size();j++){
				if(i!=j){
					String right = selectedListTermArray.get(j);
					for(int k=0;k<compareArray.size();k++){
						HashMap<String, Integer> source =compareArray.get(k); 
						if(source.containsKey(left)&&source.containsKey(right)){
							coTable[i][j] = coTable[i][j]+1;
						}
					}
				}
			}	
		}
		
		
		double maxCo = 1;
		
		//find max co occurance
		for(int i =0;i<rowCount;i++){
			for(int j =0;j<rowCount;j++){
				if(coTable[i][j]>maxCo){
					maxCo = coTable[i][j];
				}
			}
		}
		
		//adjust 7 scale
		for(int i =0;i<rowCount;i++){
			for(int j =0;j<rowCount;j++){
				double temp =7.0-(6.0*(coTable[i][j]/maxCo));
				
				if(temp == 0.0) {
					temp = 1.0;
				}
				coTable[i][j] = temp;
			}
		}
		return coTable;
	}
	

}
