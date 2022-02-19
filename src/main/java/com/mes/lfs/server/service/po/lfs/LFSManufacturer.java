package com.mes.lfs.server.service.po.lfs;

import java.io.Serializable;

/**
 * 新造厂商(Focas对接)
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-21 10:21:40
 * @LastEditTime 2020-12-21 10:21:46
 *
 */
public class LFSManufacturer implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 厂商名称
	 */
	public String Name = "";

	public LFSManufacturer() {
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
