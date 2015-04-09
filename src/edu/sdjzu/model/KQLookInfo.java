package edu.sdjzu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KQLookInfo {
	private List<HashMap<String, String>> hashMap = new ArrayList<HashMap<String, String>>();
	private String week = "";
	private String classTime = "";
	private String description = "";
	private int choseType = 0;//0:周次全部，上课时间全部；1:周次不全部，上课时间全部；2:周次全部，上课时间不全部；3:周次不全部，上课时间不全部

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
	 * 0:周次全部，上课时间全部；1:周次不全部，上课时间全部；2:周次全部，上课时间不全部；3:周次不全部，上课时间不全部
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
