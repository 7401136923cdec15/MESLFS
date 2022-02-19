package com.mes.lfs.server.service;

import java.util.Calendar;
import java.util.List;

import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.excel.ExcelData;
import com.mes.lfs.server.service.po.lfs.LFSBureau;
import com.mes.lfs.server.service.po.lfs.LFSLocoResume;
import com.mes.lfs.server.service.po.lfs.LFSManufacturer;
import com.mes.lfs.server.service.po.lfs.LFSOperationLog;
import com.mes.lfs.server.service.po.lfs.LFSSection;
//import com.mes.lfs.server.service.po.lfs.LFSAreaDepartment;
import com.mes.lfs.server.service.po.lfs.LFSStoreHouse;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaStation;

public interface LFSService {

	/**
	 * 更新LFS
	 * 
	 * @param wLFSStoreHouse
	 * @return
	 */
	ServiceResult<Integer> LFS_UpdateStoreHouse(BMSEmployee wLoginUser, LFSStoreHouse wLFSStoreHouse);

	/**
	 * 条件查询LFS任务
	 * 
	 * @param wID
	 * @param wActive
	 * @return
	 */
	ServiceResult<List<LFSStoreHouse>> LFS_QueryStoreHouseList(BMSEmployee wLoginUser, int wID, int wActive);

	/**
	 * 更新LFS
	 * 
	 * @param wLFSWorkAreaStation
	 * @return
	 */
	ServiceResult<Integer> LFS_UpdateWorkArea(BMSEmployee wLoginUser, LFSWorkAreaStation wLFSWorkAreaStation);

	/**
	 * 条件查询LFS任务
	 * 
	 * @param wID
	 * @param wWorkAreaID
	 * @param wActive
	 * @return
	 */
	ServiceResult<List<LFSWorkAreaStation>> LFS_QueryWorkAreaList(BMSEmployee wLoginUser, int wID, int wWorkAreaID,
			int wStationID, int wActive);

//	/**
//	 * 根据ID查询
//	 * 
//	 * @return
//	 */
//	ServiceResult<LFSAreaDepartment> LFS_QueryAreaDepartment(BMSEmployee wLoginUser, int wID, String wName);
//
//	/**
//	 * 条件查询
//	 * 
//	 * @return
//	 */
//	ServiceResult<List<LFSAreaDepartment>> LFS_QueryAreaDepartmentList(BMSEmployee wLoginUser, int wWorkAreaID,
//			int wLeaderID, int wActive);

//	/**
//	 * 新增或修改
//	 * 
//	 * @return
//	 */
//	ServiceResult<Integer> LFS_UpdateAreaDepartment(BMSEmployee wLoginUser, LFSAreaDepartment wLFSAreaDepartment);

//	/**
//	 * 激活或禁用
//	 * 
//	 * @return
//	 */
//	ServiceResult<Integer> LFS_ActiveAreaDepartmentList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	/**
	 * 根据ID查询
	 * 
	 * @return
	 */
	ServiceResult<LFSWorkAreaChecker> LFS_QueryWorkAreaChecker(BMSEmployee wLoginUser, int wID);

	/**
	 * 条件查询
	 * 
	 * @return
	 */
	ServiceResult<List<LFSWorkAreaChecker>> LFS_QueryWorkAreaCheckerList(BMSEmployee wLoginUser, int wID,
			int wWorkAreaID, int wActive);

	/**
	 * 新增或修改
	 * 
	 * @return
	 */
	ServiceResult<Integer> LFS_UpdateWorkAreaChecker(BMSEmployee wLoginUser, LFSWorkAreaChecker wLFSWorkAreaChecker);

	/**
	 * 激活或禁用
	 * 
	 * @return
	 */
	ServiceResult<Integer> LFS_ActiveWorkAreaCheckerList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	/**
	 * 移动元素
	 * 
	 * @param wLoginUser
	 * @param wLFSWorkStaionID
	 * @param wType
	 * @param wNumbers
	 * @return
	 */
	ServiceResult<Integer> LFS_MoveItem(BMSEmployee wLoginUser, int wLFSWorkStaionID, int wType, int wNumbers);

	/**
	 * 获取配属局列表
	 */
	ServiceResult<List<LFSBureau>> LFS_QueryBureauList(BMSEmployee wLoginUser);

	/**
	 * 获取配属段列表
	 */
	ServiceResult<List<LFSSection>> LFS_QuerySectionList(BMSEmployee wLoginUser, int wLFSBureauID);

	/**
	 * 获取供应商列表
	 */
	ServiceResult<List<LFSManufacturer>> LFS_QueryManufacturerList(BMSEmployee wLoginUser);

	/**
	 * 条件查询所有机车履历
	 * 
	 * @param wEndTime
	 * @param wStartTime
	 */
	ServiceResult<List<LFSLocoResume>> LFS_QueryLocoResumeList(BMSEmployee wLoginUser, int wProductID, String wCarNum,
			int wLFSBureauID, int wLFSSectionID, int wLFSManufacturerID, int wLineID, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 创建机车履历
	 */
	ServiceResult<LFSLocoResume> LFS_CreateLocoResume(BMSEmployee wLoginUser);

	/**
	 * 新增或更新机车履历
	 */
	ServiceResult<Integer> LFS_UpdateLocoResume(BMSEmployee wLoginUser, LFSLocoResume wData);

	/**
	 * 获取单条机车履历
	 */
	ServiceResult<LFSLocoResume> LFs_QueryLocoResumeInfo(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<LFSLocoResume>> LFS_QueryLOCOResumeList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 导入机车履历
	 */
	ServiceResult<Integer> LFS_ImportLocoResume(BMSEmployee wLoginUser, ExcelData result);

	/**
	 * 同步工序
	 */
	ServiceResult<Integer> LFS_SynchronizedStep(BMSEmployee wLoginUser);

	/**
	 * 更新操作日志
	 */
	ServiceResult<Integer> LFS_UpdateOperationLog(BMSEmployee wLoginUser, LFSOperationLog wLFSOperationLog);

	/**
	 * 查询操作日志
	 */
	ServiceResult<List<LFSOperationLog>> LFS_QueryOperationLogAll(BMSEmployee wLoginUser, int wType, int wSourceID,
			Calendar wStartTime, Calendar wEndTime);
}
