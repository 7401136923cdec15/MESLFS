package com.mes.lfs.server.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.mes.lfs.server.service.LFSService;
import com.mes.lfs.server.service.mesenum.BMSDepartmentType;
import com.mes.lfs.server.service.mesenum.MESException;
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSDepartment;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.excel.ExcelData;
import com.mes.lfs.server.service.po.excel.ExcelLineData;
import com.mes.lfs.server.service.po.fmc.FMCLineUnit;
import com.mes.lfs.server.service.po.fmc.FMCWorkCharge;
import com.mes.lfs.server.service.po.fpc.FPCPart;
import com.mes.lfs.server.service.po.lfs.LFSBureau;
import com.mes.lfs.server.service.po.lfs.LFSLocoResume;
import com.mes.lfs.server.service.po.lfs.LFSManufacturer;
import com.mes.lfs.server.service.po.lfs.LFSOperationLog;
import com.mes.lfs.server.service.po.lfs.LFSSection;
import com.mes.lfs.server.service.po.lfs.LFSStoreHouse;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.lfs.server.service.po.oms.OMSOrder;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.service.utils.XmlTool;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.dao.lfs.LFSLocoResumeDAO;
import com.mes.lfs.server.serviceimpl.dao.lfs.LFSOperationLogDAO;
import com.mes.lfs.server.serviceimpl.dao.lfs.LFSStoreHouseDAO;
import com.mes.lfs.server.serviceimpl.dao.lfs.LFSWorkAreaCheckerDAO;
import com.mes.lfs.server.serviceimpl.dao.lfs.LFSWorkAreaStationDAO;
import com.mes.lfs.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;
import com.mes.lfs.server.utils.Constants;

@Service
public class LFSServiceImpl implements LFSService {

	private static Logger logger = LoggerFactory.getLogger(LFSServiceImpl.class);

	private static LFSService Instance;

	public static LFSService getInstance() {
		if (Instance == null)
			Instance = new LFSServiceImpl();
		return Instance;
	}

	public LFSServiceImpl() {
	}

	@Override
	public ServiceResult<Integer> LFS_UpdateStoreHouse(BMSEmployee wLoginUser, LFSStoreHouse wLFSStoreHouse) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = (int) LFSStoreHouseDAO.getInstance().Update(wLoginUser, wLFSStoreHouse, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSStoreHouse>> LFS_QueryStoreHouseList(BMSEmployee wLoginUser, int wID, int wActive) {
		ServiceResult<List<LFSStoreHouse>> wResult = new ServiceResult<List<LFSStoreHouse>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = LFSStoreHouseDAO.getInstance().SelectList(wLoginUser, wID, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> LFS_UpdateWorkArea(BMSEmployee wLoginUser, LFSWorkAreaStation wLFSWorkAreaStation) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = (int) LFSWorkAreaStationDAO.getInstance().Update(wLoginUser, wLFSWorkAreaStation,
					wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSWorkAreaStation>> LFS_QueryWorkAreaList(BMSEmployee wLoginUser, int wID,
			int wWorkAreaID, int wStationID, int wActive) {
		ServiceResult<List<LFSWorkAreaStation>> wResult = new ServiceResult<List<LFSWorkAreaStation>>();
		try {
			wResult.Result = new ArrayList<LFSWorkAreaStation>();

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = LFSWorkAreaStationDAO.getInstance().SelectList(wLoginUser, wID, wWorkAreaID, wStationID,
					wActive, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());

			// 工位列表
			List<FPCPart> wFPCPartList = FMCServiceImpl.getInstance().FPC_QueryPartList(wLoginUser, 0, 0, 0)
					.List(FPCPart.class);
			if (wFPCPartList == null || wFPCPartList.size() <= 0) {
				return wResult;
			}

			wFPCPartList = wFPCPartList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());

			List<LFSWorkAreaStation> wWSList = null;
			LFSWorkAreaStation wLFSWorkAreaStation = null;
			int wOrderNumber = LFSWorkAreaStationDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, wErrorCode)
					.size();

			// 班组工位
			List<FMCWorkCharge> wWCList = CoreServiceImpl.getInstance().FMC_QueryWorkChargeList(BaseDAO.SysAdmin)
					.List(FMCWorkCharge.class);

			List<BMSDepartment> wDepartmentList = CoreServiceImpl.getInstance()
					.BMS_QueryDepartmentList(wLoginUser, 0, 0).List(BMSDepartment.class);

			int wTempArea = 0;
			List<FMCWorkCharge> wTempList = null;
			long wTempID = 0;
			for (FPCPart wFPCPart : wFPCPartList) {
				wTempList = wWCList.stream().filter(p -> p.StationID == wFPCPart.ID).collect(Collectors.toList());
				if (wTempList != null && wTempList.size() > 0) {
					int wClassID = wTempList.get(0).ClassID;
					wTempArea = GetAreaIDByClassID(wClassID, wDepartmentList);
				} else {
					wTempArea = 0;
				}

				wWSList = LFSWorkAreaStationDAO.getInstance().SelectList(wLoginUser, -1, -1, wFPCPart.ID, -1,
						wErrorCode);
				if (wWSList != null && wWSList.size() == 1) {
					wLFSWorkAreaStation = wWSList.get(0);
					wLFSWorkAreaStation.WorkAreaID = wTempArea;
				} else if (wWSList != null && wWSList.size() > 1) {
					if (wWSList.stream().anyMatch(p -> p.Active == 0)) {
						List<LFSWorkAreaStation> wList = wWSList.stream().filter(p -> p.Active == 0)
								.collect(Collectors.toList());
						LFSWorkAreaStationDAO.getInstance().DeleteList(BaseDAO.SysAdmin, wList, wErrorCode);
					} else {
						int wKey = wWSList.get(0).ID;
						List<LFSWorkAreaStation> wList = wWSList.stream().filter(p -> p.ID != wKey)
								.collect(Collectors.toList());
						LFSWorkAreaStationDAO.getInstance().DeleteList(BaseDAO.SysAdmin, wList, wErrorCode);
					}
				} else {
					wLFSWorkAreaStation = new LFSWorkAreaStation();
					wLFSWorkAreaStation.Active = 1;
					wLFSWorkAreaStation.CreateID = wLoginUser.ID;
					wLFSWorkAreaStation.CreateTime = Calendar.getInstance();
					wLFSWorkAreaStation.ID = 0;
					wLFSWorkAreaStation.OrderNum = ++wOrderNumber;
					wLFSWorkAreaStation.StationID = wFPCPart.ID;
					wLFSWorkAreaStation.WorkAreaID = wTempArea;
				}
				wTempID = LFSWorkAreaStationDAO.getInstance().Update(wLoginUser, wLFSWorkAreaStation, wErrorCode);
				if (wTempID > 0) {
					wLFSWorkAreaStation.ID = (int) wTempID;
					wResult.Result.add(wLFSWorkAreaStation);
				}
			}

			if (wResult.Result != null && wResult.Result.size() > 0) {
				// 去重
				wResult.Result = new ArrayList<LFSWorkAreaStation>(wResult.Result.stream()
						.collect(Collectors.toMap(LFSWorkAreaStation::getID, account -> account, (k1, k2) -> k2))
						.values());
				if (wWorkAreaID > 0) {
					wResult.Result = wResult.Result.stream().filter(p -> p.WorkAreaID == wWorkAreaID)
							.collect(Collectors.toList());
				}
				if (wStationID > 0) {
					wResult.Result = wResult.Result.stream().filter(p -> p.StationID == wStationID)
							.collect(Collectors.toList());
				}
				if (wActive == 0 || wActive == 1) {
					wResult.Result = wResult.Result.stream().filter(p -> p.Active == wActive)
							.collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据班组ID查询工区ID
	 * 
	 * @param wClassID
	 * @param wDepartmentList
	 * @return
	 */
	private int GetAreaIDByClassID(int wClassID, List<BMSDepartment> wDepartmentList) {
		int wResult = 0;
		try {
			Optional<BMSDepartment> wOption = wDepartmentList.stream().filter(p -> p.ID == wClassID).findFirst();
			if (wOption.isPresent()) {
				BMSDepartment wThis = wOption.get();
				wOption = wDepartmentList.stream().filter(p -> p.ID == wThis.ParentID).findFirst();
				if (wOption.isPresent()) {
					if (wOption.get().Type == BMSDepartmentType.Area.getValue()) {
						wResult = wOption.get().ID;
						return wResult;
					} else {
						wResult = GetAreaIDByClassID(wOption.get().ID, wDepartmentList);
					}
				} else {
					return wResult;
				}
			} else {
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ID查询
	 */
	@Override
	public ServiceResult<LFSWorkAreaChecker> LFS_QueryWorkAreaChecker(BMSEmployee wLoginUser, int wID) {
		ServiceResult<LFSWorkAreaChecker> wResult = new ServiceResult<LFSWorkAreaChecker>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = LFSWorkAreaCheckerDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询
	 */
	@Override
	public ServiceResult<List<LFSWorkAreaChecker>> LFS_QueryWorkAreaCheckerList(BMSEmployee wLoginUser, int wID,
			int wWorkAreaID, int wActive) {
		ServiceResult<List<LFSWorkAreaChecker>> wResult = new ServiceResult<List<LFSWorkAreaChecker>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = LFSWorkAreaCheckerDAO.getInstance().SelectList(wLoginUser, wID, wWorkAreaID, wActive,
					wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 更新或修改
	 */
	@Override
	public ServiceResult<Integer> LFS_UpdateWorkAreaChecker(BMSEmployee wLoginUser,
			LFSWorkAreaChecker wLFSWorkAreaChecker) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// 新增，处理相同的数据
			if (wLFSWorkAreaChecker.ID == 0) {
				List<LFSWorkAreaChecker> wList = LFSWorkAreaCheckerDAO.getInstance().SelectList(wLoginUser, -1,
						wLFSWorkAreaChecker.WorkAreaID, -1, wErrorCode);
				if (wList != null && wList.size() > 0) {
					wResult.setFaultCode("该工区已存在!");
					return wResult;
				}
				wLFSWorkAreaChecker.CreateID = wLoginUser.ID;
				wLFSWorkAreaChecker.CreateTime = Calendar.getInstance();
			}

			wResult.Result = LFSWorkAreaCheckerDAO.getInstance().Update(wLoginUser, wLFSWorkAreaChecker, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 批量激活或禁用
	 */
	@Override
	public ServiceResult<Integer> LFS_ActiveWorkAreaCheckerList(BMSEmployee wLoginUser, List<Integer> wIDList,
			int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			LFSWorkAreaCheckerDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 移动
	 */
	@Override
	public ServiceResult<Integer> LFS_MoveItem(BMSEmployee wLoginUser, int wLFSWorkStaionID, int wType, int wNumbers) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<LFSWorkAreaStation> wList = LFSWorkAreaStationDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			// 排序
			wList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			Optional<LFSWorkAreaStation> wOption = wList.stream().filter(p -> p.ID == wLFSWorkStaionID).findFirst();
			if (!wOption.isPresent()) {
				return wResult;
			}

			LFSWorkAreaStation wLFSWorkAreaStation = wOption.get();
			int wOrderNumber = wLFSWorkAreaStation.OrderNum;

			// 得到插入顺序
			switch (wType) {
			case 1:// 上移
				wOrderNumber -= wNumbers;
				if (wOrderNumber < 1) {
					wOrderNumber = 1;
				}
				break;
			case 2:// 下移
				wOrderNumber += wNumbers;
				if (wOrderNumber > wList.size()) {
					wOrderNumber = wList.size();
				}
				break;
			default:
				break;
			}

			int wInsertOrder = wOrderNumber - 1;
			wList.removeIf(p -> p.ID == wLFSWorkStaionID);

			if (wInsertOrder >= wList.size()) {
				wList.add(wLFSWorkAreaStation);
			} else {
				wList.add(wInsertOrder, wLFSWorkAreaStation);
			}
			int wIndex = 0;
			for (LFSWorkAreaStation wItem : wList) {
				wItem.OrderNum = ++wIndex;
				wItem.CreateTime = Calendar.getInstance();
				wItem.CreateID = wLoginUser.ID;
				LFSWorkAreaStationDAO.getInstance().Update(wLoginUser, wItem, wErrorCode);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSBureau>> LFS_QueryBureauList(BMSEmployee wLoginUser) {
		ServiceResult<List<LFSBureau>> wResult = new ServiceResult<List<LFSBureau>>();
		wResult.Result = new ArrayList<LFSBureau>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = XmlTool.ReadXml(Constants.getConfigPath() + "LFSBureau.xml");

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSSection>> LFS_QuerySectionList(BMSEmployee wLoginUser, int wLFSBureauID) {
		ServiceResult<List<LFSSection>> wResult = new ServiceResult<List<LFSSection>>();
		wResult.Result = new ArrayList<LFSSection>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<LFSSection> wList = XmlTool.ReadXml(Constants.getConfigPath() + "LFSSection.xml");
			if (wList != null && wList.size() > 0) {
				if (wLFSBureauID <= 0) {
					wResult.Result = wList;
				} else {
					wResult.Result = wList.stream().filter(p -> p.BureauID == wLFSBureauID)
							.collect(Collectors.toList());
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSManufacturer>> LFS_QueryManufacturerList(BMSEmployee wLoginUser) {
		ServiceResult<List<LFSManufacturer>> wResult = new ServiceResult<List<LFSManufacturer>>();
		wResult.Result = new ArrayList<LFSManufacturer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = XmlTool.ReadXml(Constants.getConfigPath() + "LFSManufacturer.xml");

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSLocoResume>> LFS_QueryLocoResumeList(BMSEmployee wLoginUser, int wProductID,
			String wCarNum, int wLFSBureauID, int wLFSSectionID, int wLFSManufacturerID, int wLineID,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<LFSLocoResume>> wResult = new ServiceResult<List<LFSLocoResume>>();
		wResult.Result = new ArrayList<LFSLocoResume>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {

			wResult.Result = LFSLocoResumeDAO.getInstance().SelectList(wLoginUser, -1, wProductID, wCarNum,
					wLFSBureauID, wLFSSectionID, wLFSManufacturerID, wLineID, wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<LFSLocoResume> LFS_CreateLocoResume(BMSEmployee wLoginUser) {
		ServiceResult<LFSLocoResume> wResult = new ServiceResult<LFSLocoResume>();
		wResult.Result = new LFSLocoResume();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 基本
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			String wCode = LFSLocoResumeDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);

			wResult.Result = new LFSLocoResume(0, wCode, 0, 0, "", "", 0, "", 0, "", 0, "", wBaseTime, wBaseTime, 0, "",
					0, wBaseTime, wBaseTime, "", wLoginUser.ID, "", Calendar.getInstance());

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> LFS_UpdateLocoResume(BMSEmployee wLoginUser, LFSLocoResume wData) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = LFSLocoResumeDAO.getInstance().Update(wLoginUser, wData, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<LFSLocoResume> LFs_QueryLocoResumeInfo(BMSEmployee wLoginUser, int wID) {
		ServiceResult<LFSLocoResume> wResult = new ServiceResult<LFSLocoResume>();
		wResult.Result = new LFSLocoResume();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = LFSLocoResumeDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSLocoResume>> LFS_QueryLOCOResumeList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<LFSLocoResume>> wResult = new ServiceResult<List<LFSLocoResume>>();
		wResult.Result = new ArrayList<LFSLocoResume>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = LFSLocoResumeDAO.getInstance().SelectListByOnlineTime(wLoginUser, wStartTime, wEndTime,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> LFS_ImportLocoResume(BMSEmployee wLoginUser, ExcelData result) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (result.sheetData == null || result.sheetData.size() <= 0 || result.sheetData.get(0).lineData == null
					|| result.sheetData.get(0).lineData.size() <= 0) {
				wResult.FaultCode += "提示：Excel数据解析失败，请检查文件是否损坏!";
				return wResult;
			}

			// ①获取配属局列表
			List<LFSBureau> wJList = LFSServiceImpl.getInstance().LFS_QueryBureauList(wLoginUser).Result;
			// ②获取配属段列表
			List<LFSSection> wDList = LFSServiceImpl.getInstance().LFS_QuerySectionList(wLoginUser, -1).Result;
			// ③获取新造厂商列表
			List<LFSManufacturer> wCSList = LFSServiceImpl.getInstance().LFS_QueryManufacturerList(wLoginUser).Result;
			// ④解析数据
			List<LFSLocoResume> wList = GetLocoResumeList(wJList, wDList, wCSList, result.sheetData.get(0).lineData);
			// ⑤遍历导入
			for (LFSLocoResume lfsLocoResume : wList) {
//				List<LFSLocoResume> wHistList = LFSLocoResumeDAO.getInstance().SelectByOrderID(wLoginUser,
//						lfsLocoResume.OrderID, wErrorCode);
//				if (wHistList.size() > 0) {
//					lfsLocoResume.ID = wHistList.get(0).ID;
//				} else {
//					lfsLocoResume.ID = 0;
//				}
				LFSLocoResumeDAO.getInstance().Update(wLoginUser, lfsLocoResume, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据Excel内容，解析机车履历数据
	 */
	private List<LFSLocoResume> GetLocoResumeList(List<LFSBureau> wJList, List<LFSSection> wDList,
			List<LFSManufacturer> wCSList, List<ExcelLineData> wLineData) {
		List<LFSLocoResume> wResult = new ArrayList<LFSLocoResume>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// 订单集合
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().ConditionAll(BaseDAO.SysAdmin, -1, -1, -1, "", null,
					null, -1, wErrorCode);
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			// 数据解析
			int wIndex = 0;
			for (ExcelLineData wExcelLineData : wLineData) {
				if (wIndex == 0) {
					wIndex++;
					continue;
				}

				String wProductNo = wExcelLineData.colData.get(1);
				String wCarNum = wExcelLineData.colData.get(2);
				String wLJName = wExcelLineData.colData.get(3);
				String wJWDName = wExcelLineData.colData.get(4);
				String wCSName = wExcelLineData.colData.get(5);
				String wNewDate = wExcelLineData.colData.get(6);
				String wOnlineDate = wExcelLineData.colData.get(7);
				String wLineName = wExcelLineData.colData.get(8);
				String wLC = wExcelLineData.colData.get(9);
				String wPrepare = wExcelLineData.colData.get(10);
				String wInPlantDate = wExcelLineData.colData.get(11);
				String wOutPlantDate = wExcelLineData.colData.get(12);

				// 订单
				int wOrderID = 0;
				String wPartNo = StringUtils.Format("{0}#{1}", wProductNo, wCarNum);
				if (wOrderList.stream().anyMatch(p -> p.PartNo.equals(wPartNo))) {
					wOrderID = wOrderList.stream().filter(p -> p.PartNo.equals(wPartNo)).findFirst().get().ID;
				}
				// 车型
				int wProductID = LFSConstants.GetFPCProduct(wProductNo).ID;
				// 车号
				// 局
				int lFSBureauID = 0;
				if (wJList.stream().anyMatch(p -> p.Name.equals(wLJName))) {
					lFSBureauID = wJList.stream().filter(p -> p.Name.equals(wLJName)).findFirst().get().ID;
				}
				// 段
				int lFSSectionID = 0;
				if (wDList.stream().anyMatch(p -> p.Name.equals(wJWDName))) {
					lFSSectionID = wDList.stream().filter(p -> p.Name.equals(wJWDName)).findFirst().get().ID;
				}
				// 厂商
				int lFSManufacturerID = 0;
				if (wCSList.stream().anyMatch(p -> p.Name.equals(wCSName))) {
					lFSManufacturerID = wCSList.stream().filter(p -> p.Name.equals(wCSName)).findFirst().get().ID;
				}
				// 新造日期
				Calendar newCreateDate = wBaseTime;
				if (StringUtils.isNotEmpty(wNewDate)) {
					newCreateDate = StringUtils.parseCalendar(wNewDate);
				}

				// 上线日期
				Calendar onlineDate = wBaseTime;
				if (StringUtils.isNotEmpty(wOnlineDate)) {
					onlineDate = StringUtils.parseCalendar(wOnlineDate);
				}
				// 修程
				int wLineID = LFSConstants.GetFMCLine(wLineName).ID;
				// 里程
				double mileage = StringUtils.parseDouble(wLC);
				// 入厂日期
				Calendar inPlantDate = wBaseTime;
				if (StringUtils.isNotEmpty(wInPlantDate)) {
					inPlantDate = StringUtils.parseCalendar(wInPlantDate);
				}
				// 出厂日期
				Calendar outPlantDate = wBaseTime;
				if (StringUtils.isNotEmpty(wOutPlantDate)) {
					outPlantDate = StringUtils.parseCalendar(wOutPlantDate);
				}
				// 零公里准备

				LFSLocoResume wLFSLocoResume = new LFSLocoResume(0, "", wOrderID, wProductID, wProductNo, wCarNum,
						lFSBureauID, wLJName, lFSSectionID, wJWDName, lFSManufacturerID, wCSName, newCreateDate,
						onlineDate, wLineID, wLineName, mileage, inPlantDate, outPlantDate, wPrepare, 10915, "kaifa",
						Calendar.getInstance());
				wResult.add(wLFSLocoResume);

				wIndex++;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> LFS_SynchronizedStep(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<FMCLineUnit> wFMCLineUnitList = LFSLocoResumeDAO.getInstance().GetRepeatLineUnitList(wLoginUser,
					wErrorCode);
			if (wFMCLineUnitList == null || wFMCLineUnitList.size() <= 0) {
				return wResult;
			}

			List<Integer> wRList = new ArrayList<Integer>();
			// 多条件
			List<FMCLineUnit> wOnlyList = wFMCLineUnitList.stream().collect(Collectors.collectingAndThen(
					Collectors.toCollection(() -> new TreeSet<>(
							Comparator.comparing(o -> o.getProductID() + ";" + o.getLineID() + ";" + o.getUnitID()))),
					ArrayList::new));
			for (FMCLineUnit wFMCLineUnit : wOnlyList) {
				// 根据车型、修程、工序查询数据集合
				List<FMCLineUnit> wList = wFMCLineUnitList.stream().filter(p -> p.ProductID == wFMCLineUnit.ProductID
						&& p.LineID == wFMCLineUnit.LineID && p.UnitID == wFMCLineUnit.UnitID)
						.collect(Collectors.toList());
				// 根据工序查询真实所属工位
				int wPartID = LFSLocoResumeDAO.getInstance().GetRealPartID(wLoginUser, wFMCLineUnit.UnitID, wErrorCode);
				if (wPartID < 0) {
					continue;
				}

				List<Integer> wIDList = wList.stream().filter(p -> p.ParentUnitID != wPartID).map(p -> p.ID)
						.collect(Collectors.toList());
				wRList.addAll(wIDList);
			}
			if (wRList.size() <= 0) {
				return wResult;
			}

			String wIDs = StringUtils.Join(",", wRList);
			LFSLocoResumeDAO.getInstance().DeleteLineUnitList(wLoginUser, wIDs, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> LFS_UpdateOperationLog(BMSEmployee wLoginUser, LFSOperationLog wLFSOperationLog) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = LFSOperationLogDAO.getInstance().Update(wLoginUser, wLFSOperationLog, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<LFSOperationLog>> LFS_QueryOperationLogAll(BMSEmployee wLoginUser, int wType,
			int wSourceID, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<LFSOperationLog>> wResult = new ServiceResult<List<LFSOperationLog>>();
		wResult.Result = new ArrayList<LFSOperationLog>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = LFSOperationLogDAO.getInstance().SelectList(wLoginUser, -1, wSourceID, wType, -1,
					wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}
}
