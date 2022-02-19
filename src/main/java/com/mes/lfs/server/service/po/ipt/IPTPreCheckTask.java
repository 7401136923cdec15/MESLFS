package com.mes.lfs.server.service.po.ipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.lfs.server.service.po.bpm.BPMTaskBase;

/**
 * 预检单
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-2-12 14:15:24
 * @LastEditTime 2020-2-12 14:15:27
 *
 */
public class IPTPreCheckTask extends BPMTaskBase implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
//	public int ID = 0;
	/**
	 * 编码
	 */
	public String Code = "";
	// 预检班组
	public int IPTClassID = 0;
	public String IPTClassName = "";
	/**
	 * 修程
	 */
	public int LineID = 0;
	public String LineName = "";
	/**
	 * 车号
	 */
	public String CarNumber = "";
	/**
	 * 车型
	 */
	public int ProductID = 0;
	public String ProductNo = "";
	/**
	 * 局段
	 */
	public int CustomID = 0;
	public String CustomName = "";
	/**
	 * 预检标准项列表
	 */
	public List<IPTItem> IPTItemList = new ArrayList<IPTItem>();
	/**
	 * 预检申请项列表
	 */
	public List<IPTItemApply> IPTItemApplyList = new ArrayList<IPTItemApply>();
	/**
	 * 预检问题项列表
	 */
	public List<IPTPreCheckProblem> IPTPreCheckProblemList = new ArrayList<IPTPreCheckProblem>();
	/**
	 * 预检人
	 */
	public int SubmitID = 0;
	public String SubmitName = "";
	/**
	 * 预检时间
	 */
	public Calendar SubmitTime = Calendar.getInstance();
	/**
	 * 预检班次
	 */
	public int ShiftID = 0;
	/**
	 * 预检单状态
	 */
//	public int Status = 0;

	public IPTPreCheckTask() {
		SubmitTime.set(2000, 0, 1);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getIPTClass() {
		return IPTClassID;
	}

	public void setIPTClass(int iPTClass) {
		IPTClassID = iPTClass;
	}

	public String getIPTClassName() {
		return IPTClassName;
	}

	public void setIPTClassName(String iPTClassName) {
		IPTClassName = iPTClassName;
	}

	public String getCarNumber() {
		return CarNumber;
	}

	public void setCarNumber(String carNumber) {
		CarNumber = carNumber;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public int getCustomID() {
		return CustomID;
	}

	public void setCustomID(int customID) {
		CustomID = customID;
	}

	public String getCustomName() {
		return CustomName;
	}

	public void setCustomName(String customName) {
		CustomName = customName;
	}

	public List<IPTItem> getIPTItemList() {
		return IPTItemList;
	}

	public void setIPTItemList(List<IPTItem> iPTItemList) {
		IPTItemList = iPTItemList;
	}

	public List<IPTItemApply> getIPTItemApplyList() {
		return IPTItemApplyList;
	}

	public void setIPTItemApplyList(List<IPTItemApply> iPTItemApplyList) {
		IPTItemApplyList = iPTItemApplyList;
	}

	public List<IPTPreCheckProblem> getIPTPreCheckProblemList() {
		return IPTPreCheckProblemList;
	}

	public void setIPTPreCheckProblemList(List<IPTPreCheckProblem> iPTPreCheckProblemList) {
		IPTPreCheckProblemList = iPTPreCheckProblemList;
	}

	public int getSubmitID() {
		return SubmitID;
	}

	public void setSubmitID(int submitID) {
		SubmitID = submitID;
	}

	public String getSubmitName() {
		return SubmitName;
	}

	public void setSubmitName(String submitName) {
		SubmitName = submitName;
	}

	public Calendar getSubmitTime() {
		return SubmitTime;
	}

	public void setSubmitTime(Calendar submitTime) {
		SubmitTime = submitTime;
	}

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public int getIPTClassID() {
		return IPTClassID;
	}

	public void setIPTClassID(int iPTClassID) {
		IPTClassID = iPTClassID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
