package com.kse.model;

import java.util.ArrayList;
import java.util.Date;

public class KnowledgeProjectModel {
  
	private String filePath;
	private String projectName;
	private ArrayList<KnowledgeStructureModel> knowldgeStructureArray;
	private Date regDate;

	private long projectId;
	
	private ArrayList<KnowledgeExcuteGraphModel> domainExcuteArray;
	private ArrayList<KnowledgeStructureModel> domainKnowldgeStructureArray;
	



	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	


	public ArrayList<KnowledgeExcuteGraphModel> getDomainExcuteArray() {
		return domainExcuteArray;
	}

	public void setDomainExcuteArray(
			ArrayList<KnowledgeExcuteGraphModel> domainExcuteArray) {
		this.domainExcuteArray = domainExcuteArray;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public KnowledgeProjectModel(long projectId,String projectName){

		this.projectId = projectId;
		this.projectName = projectName;
		this.regDate = new Date();
		knowldgeStructureArray = new ArrayList<KnowledgeStructureModel>();
		domainKnowldgeStructureArray = new ArrayList<KnowledgeStructureModel>();
		domainExcuteArray = new ArrayList<KnowledgeExcuteGraphModel>();
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public ArrayList<KnowledgeStructureModel> getKnowldgeStructureArray() {
		return knowldgeStructureArray;
	}
	public void setKnowldgeStructureArray(
			ArrayList<KnowledgeStructureModel> knowldgeStructureArray) {
		this.knowldgeStructureArray = knowldgeStructureArray;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	public String toString(){
		
		return this.projectName;
	}
	
	
	public ArrayList<KnowledgeStructureModel> getDomainKnowldgeStructureArray() {
		return domainKnowldgeStructureArray;
	}

	public void setDomainKnowldgeStructureArray(
			ArrayList<KnowledgeStructureModel> domainKnowldgeStructureArray) {
		this.domainKnowldgeStructureArray = domainKnowldgeStructureArray;
	}

}
