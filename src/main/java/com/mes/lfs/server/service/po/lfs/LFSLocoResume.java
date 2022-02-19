package com.mes.lfs.server.service.po.lfs;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 机车履历(focas对接)
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-21 11:33:39
 * @LastEditTime 2020-12-21 11:33:42
 *
 */
public class LFSLocoResume implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 机车履历编号
	 */
	public String Code = "";
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 车号
	 */
	public String CarNum = "";
	/**
	 * 配属局ID
	 */
	public int LFSBureauID = 0;
	/**
	 * 配属局名称
	 */
	public String LFSBureauName = "";
	/**
	 * 配属段ID
	 */
	public int LFSSectionID = 0;
	/**
	 * 配属段名称
	 */
	public String LFSSectionName = "";
	/**
	 * 新造厂商ID
	 */
	public int LFSManufacturerID = 0;
	/**
	 * 新造厂商名称
	 */
	public String LFSManufacturerName = "";
	/**
	 * 新造日期
	 */
	public Calendar NewCreateDate = Calendar.getInstance();
	/**
	 * 上线日期
	 */
	public Calendar OnlineDate = Calendar.getInstance();
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 入修累计里程
	 */
	public double Mileage = 0.0;
	/**
	 * 入厂日期
	 */
	public Calendar InPlantDate = Calendar.getInstance();
	/**
	 * 出厂日期
	 */
	public Calendar OutPlantDate = Calendar.getInstance();
	/**
	 * 零公里准备情况
	 */
	public String Preparation = "";
	/**
	 * 创建人ID
	 */
	public int CreateID = 0;
	/**
	 * 创建人名称
	 */
	public String Creator = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();

	public LFSLocoResume(int iD, String code, int orderID, int productID, String productNo, String carNum,
			int lFSBureauID, String lFSBureauName, int lFSSectionID, String lFSSectionName, int lFSManufacturerID,
			String lFSManufacturerName, Calendar newCreateDate, Calendar onlineDate, int lineID, String lineName,
			double mileage, Calendar inPlantDate, Calendar outPlantDate, String preparation, int createID,
			String creator, Calendar createTime) {
		super();
		ID = iD;
		Code = code;
		OrderID = orderID;
		ProductID = productID;
		ProductNo = productNo;
		CarNum = carNum;
		LFSBureauID = lFSBureauID;
		LFSBureauName = lFSBureauName;
		LFSSectionID = lFSSectionID;
		LFSSectionName = lFSSectionName;
		LFSManufacturerID = lFSManufacturerID;
		LFSManufacturerName = lFSManufacturerName;
		NewCreateDate = newCreateDate;
		OnlineDate = onlineDate;
		LineID = lineID;
		LineName = lineName;
		Mileage = mileage;
		InPlantDate = inPlantDate;
		OutPlantDate = outPlantDate;
		Preparation = preparation;
		CreateID = createID;
		Creator = creator;
		CreateTime = createTime;
	}

	public LFSLocoResume() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getCreateID() {
		return CreateID;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
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

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
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

	public String getCarNum() {
		return CarNum;
	}

	public void setCarNum(String carNum) {
		CarNum = carNum;
	}

	public int getLFSBureauID() {
		return LFSBureauID;
	}

	public void setLFSBureauID(int lFSBureauID) {
		LFSBureauID = lFSBureauID;
	}

	public String getLFSBureauName() {
		return LFSBureauName;
	}

	public void setLFSBureauName(String lFSBureauName) {
		LFSBureauName = lFSBureauName;
	}

	public int getLFSSectionID() {
		return LFSSectionID;
	}

	public void setLFSSectionID(int lFSSectionID) {
		LFSSectionID = lFSSectionID;
	}

	public String getLFSSectionName() {
		return LFSSectionName;
	}

	public void setLFSSectionName(String lFSSectionName) {
		LFSSectionName = lFSSectionName;
	}

	public int getLFSManufacturerID() {
		return LFSManufacturerID;
	}

	public void setLFSManufacturerID(int lFSManufacturerID) {
		LFSManufacturerID = lFSManufacturerID;
	}

	public String getLFSManufacturerName() {
		return LFSManufacturerName;
	}

	public void setLFSManufacturerName(String lFSManufacturerName) {
		LFSManufacturerName = lFSManufacturerName;
	}

	public Calendar getNewCreateDate() {
		return NewCreateDate;
	}

	public void setNewCreateDate(Calendar newCreateDate) {
		NewCreateDate = newCreateDate;
	}

	public Calendar getOnlineDate() {
		return OnlineDate;
	}

	public void setOnlineDate(Calendar onlineDate) {
		OnlineDate = onlineDate;
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

	public double getMileage() {
		return Mileage;
	}

	public void setMileage(double mileage) {
		Mileage = mileage;
	}

	public Calendar getInPlantDate() {
		return InPlantDate;
	}

	public void setInPlantDate(Calendar inPlantDate) {
		InPlantDate = inPlantDate;
	}

	public Calendar getOutPlantDate() {
		return OutPlantDate;
	}

	public void setOutPlantDate(Calendar outPlantDate) {
		OutPlantDate = outPlantDate;
	}

	public String getPreparation() {
		return Preparation;
	}

	public void setPreparation(String preparation) {
		Preparation = preparation;
	}
}
