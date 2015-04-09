package edu.sdjzu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KQLookInfo {
	private List<HashMap<String, String>> hashMap = new ArrayList<HashMap<String, String>>();
	private String week = "";
	private String classTime = "";
	private String description = "";
	private int choseType = 0;//0:�ܴ�ȫ�����Ͽ�ʱ��ȫ����1:�ܴβ�ȫ�����Ͽ�ʱ��ȫ����2:�ܴ�ȫ�����Ͽ�ʱ�䲻ȫ����3:�ܴβ�ȫ�����Ͽ�ʱ�䲻ȫ��

	public KQLookInfo(List<HashMap<String, String>> hashMap, String week, String classTime, String description,
			int choseType) {
		super();
		this.hashMap = hashMap;
		this.week = week;
		this.classTime = classTime;
		this.description = description;
		this.choseType = choseType;
	}

	public List<HashMap<String, String>> getHashMap() {
		return hashMap;
	}

	public void setHashMap(List<HashMap<String, String>> hashMap) {
		this.hashMap = hashMap;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public KQLookInfo() {
		super();
	}

	/**
	 * 0:�ܴ�ȫ�����Ͽ�ʱ��ȫ����1:�ܴβ�ȫ�����Ͽ�ʱ��ȫ����2:�ܴ�ȫ�����Ͽ�ʱ�䲻ȫ����3:�ܴβ�ȫ�����Ͽ�ʱ�䲻ȫ��
	 * 
	 * @return
	 */

	public int getChoseType() {
		return choseType;
	}

	public void setChoseType(int choseType) {
		this.choseType = choseType;
	}

}
