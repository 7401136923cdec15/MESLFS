package com.mes.lfs.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.lfs.server.service.po.cfg.CFGItem;

public enum APSTaskStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 保存
	 */
	Saved(1, "保存"),
	/**
	 * 下达
	 */
	Issued(2, "下达"),

	/**
	 * 已确认 用于日计划 与排程无关
	 */
	Confirm(3, "已确认"),
	/**
	 * 开工
	 */
	Started(4, "开工"),
	/**
	 * 自检完工
	 */
	SelfDone(5, "自检完工"),
	/**
	 * 互检完工
	 */
	MutualDone(6, "互检完工"),
	/**
	 * 专检完工
	 */
	SpeDone(7, "专检完工"),

	/**
	 * 暂停
	 */
	Suspend(8, "暂停"),
	/**
	 * 终止
	 */
	Aborted(9, "终止");

	private int value;
	private String lable;

	private APSTaskStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static APSTaskStatus getEnumType(int val) {
		for (APSTaskStatus type : APSTaskStatus.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (APSTaskStatus type : APSTaskStatus.values()) {
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
