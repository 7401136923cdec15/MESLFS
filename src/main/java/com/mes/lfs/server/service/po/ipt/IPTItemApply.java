package com.mes.lfs.server.service.po.ipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.lfs.server.service.po.bpm.BPMTaskBase;

/**
 * 预检项申请单
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-2-12 13:41:28
 * @LastEditTime 2020-2-12 13:41:33
 *
 */
public class IPTItemApply extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 预检项名称/问题简述
	 */
	public String ItemName = "";
	// 辅助属性
	/**
	 * 问题描述详情
	 */
	public String Details = "";
	/**
	 * 问题描述图片列表
	 */
	public List<String> ImageList = new ArrayList<String>();
	/**
	 * 问题描述视频列表
	 */
	public List<String> VideoList = new ArrayList<String>();
	/**
	 * 解决方案列表
	 */
	public List<IPTSOP> IPTSOPList = new ArrayList<IPTSOP>();
	/**
	 * 解决方案完整描述
	 */
	public String Describe = "";

	// 暂时不用的属性
	/**
	 * 解决方案ID
	 */
	public int SolveID = 0;
	/**
	 * 预检项ID
	 */
	public int IPTItemID = 0;
	/**
	 * 预检单ID
	 */
	public int IPTPreCheckTaskID = 0;
	// 申请节点
	public int ApplyID = 0;
	public String ApplyName = "";
	public Calendar ApplyTime = Calendar.getInstance();

	// 现场工艺节点
	public int CraftID = 0;
	public String CraftName = "";
	public Calendar CraftTime = Calendar.getInstance();

	// 技术中心节点
	public int TechID = 0;
	public String TechName = "";
	public Calendar TechTime = Calendar.getInstance();

//	/**
//	 * 主键
//	 */
//	public int ID = 0;

//	/**
//	 * 申请单状态
//	 */
//	public int Status = 0;

	public IPTItemApply() {
		ApplyTime.set(2000, 0, 1);
		CraftTime.set(2000, 0, 1);
		TechTime.set(2000, 0, 1);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getIPTPreCheckTaskID() {
		return IPTPreCheckTaskID;
	}

	public void setIPTPreCheckTaskID(int iPTPreCheckTaskID) {
		IPTPreCheckTaskID = iPTPreCheckTaskID;
	}

	public int getIPTItemID() {
		return IPTItemID;
	}

	public void setIPTItemID(int iPTItemID) {
		IPTItemID = iPTItemID;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	public List<String> getImageList() {
		return ImageList;
	}

	public void setImageList(List<String> imageList) {
		ImageList = imageList;
	}

	public List<String> getVideoList() {
		return VideoList;
	}

	public void setVideoList(List<String> videoList) {
		VideoList = videoList;
	}

	public int getSolveID() {
		return SolveID;
	}

	public void setSolveID(int solveID) {
		SolveID = solveID;
	}

	public List<IPTSOP> getIPTSOPList() {
		return IPTSOPList;
	}

	public void setIPTSOPList(List<IPTSOP> iPTSOPList) {
		IPTSOPList = iPTSOPList;
	}

	public String getDescribe() {
		return Describe;
	}

	public void setDescribe(String describe) {
		Describe = describe;
	}

	public int getApplyID() {
		return ApplyID;
	}

	public void setApplyID(int applyID) {
		ApplyID = applyID;
	}

	public String getApplyName() {
		return ApplyName;
	}

	public void setApplyName(String applyName) {
		ApplyName = applyName;
	}

	public Calendar getApplyTime() {
		return ApplyTime;
	}

	public void setApplyTime(Calendar applyTime) {
		ApplyTime = applyTime;
	}

	public int getCraftID() {
		return CraftID;
	}

	public void setCraftID(int craftID) {
		CraftID = craftID;
	}

	public String getCraftName() {
		return CraftName;
	}

	public void setCraftName(String craftName) {
		CraftName = craftName;
	}

	public Calendar getCraftTime() {
		return CraftTime;
	}

	public void setCraftTime(Calendar craftTime) {
		CraftTime = craftTime;
	}

	public int getTechID() {
		return TechID;
	}

	public void setTechID(int techID) {
		TechID = techID;
	}

	public String getTechName() {
		return TechName;
	}

	public void setTechName(String techName) {
		TechName = techName;
	}

	public Calendar getTechTime() {
		return TechTime;
	}

	public void setTechTime(Calendar techTime) {
		TechTime = techTime;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
