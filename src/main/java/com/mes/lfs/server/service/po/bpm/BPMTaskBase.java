package com.mes.lfs.server.service.po.bpm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mes.lfs.server.service.mesenum.BPMFlowTypes;

/**
 * 流程业务基础表
 * 
 * @author ShrisJava
 *
 */
public class BPMTaskBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	public int ID = 0;

	/**
	 * 任务下操作记录
	 */
	public List<BPMStepAction> StepActionList = new ArrayList<>();

	/**
	 * 流程枚举 可不用
	 */
	public int FlowType = BPMFlowTypes.Default.getValue();

	/**
	 * 流程ID
	 */
	public int FlowID = 0;

	/**
	 * 任务状态
	 */
	public int Status = 0;

	public List<BPMStepAction> getStepActionList() {
		return StepActionList;
	}

	public void setStepActionList(List<BPMStepAction> stepActionList) {
		StepActionList = stepActionList;
	}

	public int getFlowID() {
		return FlowID;
	}

	public void setFlowID(int flowID) {
		FlowID = flowID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getFlowType() {
		return FlowType;
	}

	public void setFlowType(int flowType) {
		FlowType = flowType;
	}

	public BPMTaskBase() {
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

}
