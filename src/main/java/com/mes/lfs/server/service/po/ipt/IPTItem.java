package com.mes.lfs.server.service.po.ipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 标准项
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-13 09:58:19
 * @LastEditTime 2020-2-12 13:25:36
 *
 */
public class IPTItem implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public long ID;

	public String Text;

	public int StandardType;

	public String StandardValue;

	public String StandardBaisc;

	public String DefaultValue;

	public double StandardLeft;

	public double StandardRight;

	public String Standard;

	public String Unit;

	public boolean Again;

	public boolean Visiable;

	public int VID = 0;

	public List<String> ValueSource;

	/**
	 * 指导书列表
	 */
	public List<IPTSOP> IPTSOPList = new ArrayList<IPTSOP>();
	/**
	 * 激活、关闭
	 */
	public int Active = 0;

	/**
	 * 项类型 填写类型 问题类型
	 */
	public int ItemType = 1;
	/**
	 * 详细描述
	 */
	public String Details = "";

	public IPTItem() {
		Text = "";
		StandardType = 0;
		Standard = "";
		StandardBaisc = "";
		StandardValue = "";
		DefaultValue = "";
		Unit = "";
		Again = false;
		ValueSource = new ArrayList<String>();
	}

	public long getID() {
		return ID;
	}

	public int getVID() {
		return VID;
	}

	public void setVID(int vID) {
		VID = vID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public int getStandardType() {
		return StandardType;
	}

	public void setStandardType(int standardType) {
		StandardType = standardType;
	}

	public String getStandardValue() {
		return StandardValue;
	}

	public void setStandardValue(String standardValue) {
		StandardValue = standardValue;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	public String getStandardBaisc() {
		return StandardBaisc;
	}

	public void setStandardBaisc(String standardBaisc) {
		StandardBaisc = standardBaisc;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public String getDefaultValue() {
		return DefaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		DefaultValue = defaultValue;
	}

	public double getStandardLeft() {
		return StandardLeft;
	}

	public void setStandardLeft(double standardLeft) {
		StandardLeft = standardLeft;
	}

	public double getStandardRight() {
		return StandardRight;
	}

	public void setStandardRight(double standardRight) {
		StandardRight = standardRight;
	}

	public String getStandard() {
		return Standard;
	}

	public void setStandard(String standard) {
		Standard = standard;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public boolean isAgain() {
		return Again;
	}

	public void setAgain(boolean again) {
		Again = again;
	}

	public boolean isVisiable() {
		return Visiable;
	}

	public void setVisiable(boolean visiable) {
		Visiable = visiable;
	}

	public List<String> getValueSource() {
		return ValueSource;
	}

	public void setValueSource(List<String> valueSource) {
		ValueSource = valueSource;
	}

	public int getItemType() {
		return ItemType;
	}

	public void setItemType(int itemType) {
		ItemType = itemType;
	}

	public List<IPTSOP> getIPTSOPList() {
		return IPTSOPList;
	}

	public void setIPTSOPList(List<IPTSOP> iPTSOPList) {
		IPTSOPList = iPTSOPList;
	}
}
