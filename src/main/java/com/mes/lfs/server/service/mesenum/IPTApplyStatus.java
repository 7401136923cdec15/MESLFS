package com.mes.lfs.server.service.mesenum;

/**
 * 预检项申请任务状态
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-2-16 14:01:32
 * @LastEditTime 2020-2-16 14:01:36
 *
 */
public enum IPTApplyStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 待现场工艺审批
	 */
	ToCraftAudit(1, "待现场工艺审批"),
	/**
	 * 待技术中心审批
	 */
	ToTechAudit(2, "待技术中心审批"),
	/**
	 * 已通过
	 */
	Passed(3, "已通过"),
	/**
	 * 技术中心已驳回
	 */
	Rejected(4, "技术中心已驳回");

	private int value;
	private String lable;

	private IPTApplyStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static IPTApplyStatus getEnumType(int val) {
		for (IPTApplyStatus type : IPTApplyStatus.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return IPTApplyStatus.Default;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
