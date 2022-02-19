package com.mes.lfs.server.service.po.bms;

import java.io.Serializable;
import java.util.Calendar;

public class BMSWorkCharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BMSWorkCharge() {
		// TODO Auto-generated constructor stub
	}

	public int ID;
	/**
	 * 部门ID
	 */
	public int ClassID = 0;

	public String ClassName = "";

	/**
	 * 工位ID 对应系统中的工序
	 */
	public int StationID = 0;

	public String StationName = "";

	/**
	 * 是否激活
	 */
	public int Active = 0;

	public int CreatorID = 0;
	public String Creator = "";
	public Calendar CreateTime = Calendar.getInstance();
	public int EditorID = 0;
	public String Editor = "";
	public Calendar EditTime = Calendar.getInstance();

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getCreatorID() {
		return CreatorID;
	}

	public void setCreatorID(int creatorID) {
		CreatorID = creatorID;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public int getEditorID() {
		return EditorID;
	}

	public void setEditorID(int editorID) {
		EditorID = editorID;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public int getClassID() {
		return ClassID;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public int getStationID() {
		return StationID;
	}

	public void setStationID(int stationID) {
		StationID = stationID;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

}
