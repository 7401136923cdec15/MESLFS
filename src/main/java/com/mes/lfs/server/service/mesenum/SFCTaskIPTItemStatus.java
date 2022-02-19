package com.mes.lfs.server.service.mesenum;

public enum SFCTaskIPTItemStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 已自检，待互检
	 */
	QChecked(1, "已自检，待互检"),
	/**
	 * 已互检，待专检
	 */
	MCheced(2, "已互检，待专检"),
	/**
	 * 已专检
	 */
	SChecked(3, "已专检");

	private int value;
	private String lable;

	private SFCTaskIPTItemStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCTaskIPTItemStatus getEnumType(int val) {
		for (SFCTaskIPTItemStatus type : SFCTaskIPTItemStatus.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return SFCTaskIPTItemStatus.Default;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
