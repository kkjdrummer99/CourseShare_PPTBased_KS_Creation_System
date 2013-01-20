package com.kse.model;

public class LinkModel {

  private double capacity;
	private double weight;
	private int id;
	
	public LinkModel(int id, double weight, double capacity){
		this.id = id;
		this.weight = weight;
		this.capacity = capacity;
		
	}
	
	
	
	
	public double getCapacity() {
		return capacity;
	}




	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}




	public double getWeight() {
		return weight;
	}




	public void setWeight(double weight) {
		this.weight = weight;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String toString(){
		return "E"+id;
	}

}
