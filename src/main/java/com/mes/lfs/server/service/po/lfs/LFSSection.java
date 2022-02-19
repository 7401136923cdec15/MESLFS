package com.mes.lfs.server.service.po.lfs;

import java.io.Serializable;

/**
 * 配属段(Focas对接)
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-21 10:24:40
 * @LastEditTime 2020-12-21 10:24:43
 *
 */
public class LFSSection implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;

	/**
	 * 局ID
	 */
	public int BureauID = 0;

	/**
	 * 段名称
	 */
	public String Name = "";

	public LFSSection() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
}
