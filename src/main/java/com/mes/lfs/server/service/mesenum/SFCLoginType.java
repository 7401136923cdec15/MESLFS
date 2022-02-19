package com.mes.lfs.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.lfs.server.service.po.cfg.CFGItem;

/**
 * 打卡类型
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-2-21 13:31:33
 * @LastEditTime 2020-2-21 13:31:36
 *
 */
public enum SFCLoginType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 上班打卡
	 */
	StartWork(1, "上班打卡"),
	/**
	 * 下班打卡
	 */
	AfterWork(2, "下班打卡");

	private int value;
	private String lable;

	private SFCLoginType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCLoginType getEnumType(int val) {
		for (SFCLoginType type : SFCLoginType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (SFCLoginType type : SFCLoginType.values()) {
			CFGItem wItem = new CFGItem();
			wItem.ID = type.getValue();
			wItem.ItemName = type.getLable();
			wItem.ItemText = type.getLable();
			wItemList.add(wItem);
		}
		return wItemList;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
