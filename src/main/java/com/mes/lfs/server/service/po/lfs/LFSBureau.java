package com.mes.lfs.server.service.po.lfs;

import java.io.Serializable;

/**
 * 配属局(Focas对接)
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-21 10:21:40
 * @LastEditTime 2020-12-21 10:21:46
 *
 */
public class LFSBureau implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 局名称
	 */
	public String Name = "";

	public LFSBureau() {
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
