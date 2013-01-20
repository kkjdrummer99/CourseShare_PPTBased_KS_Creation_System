package com.kse.knot;

import java.util.ArrayList;
import java.util.HashMap;

public class CoTableFactory {
  	
	
	public static final String CO_OCCURENCE = "CO_OCCURENCE";
	public static final String CO_SIMILILARITY = "CO_SIMILILARITY";
	
	
	public static double[][] makeCoTable(String kind, ArrayList<HashMap<String,Integer>> compareArray, ArrayList<String> selectedListTermArray){
		
		GetCoTable temp=null;;
		if(kind.equals(CO_OCCURENCE)){
			temp = new CoOccurence();
		}
		else if(kind.equals(CO_SIMILILARITY)){
			temp = new CoSimililarity();
		}	
		return temp.getCoTable(compareArray, selectedListTermArray);
	}
	
}
