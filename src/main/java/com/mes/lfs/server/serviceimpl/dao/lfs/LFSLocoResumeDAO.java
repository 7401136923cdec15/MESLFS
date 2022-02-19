package com.mes.lfs.server.serviceimpl.dao.lfs;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.lfs.server.service.mesenum.MESDBSource;
import com.mes.lfs.server.service.mesenum.MESException;
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.lfs.LFSBureau;
import com.mes.lfs.server.service.po.lfs.LFSLocoResume;
import com.mes.lfs.server.service.po.lfs.LFSManufacturer;
import com.mes.lfs.server.service.po.lfs.LFSSection;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.fmc.FMCLineUnit;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.LFSServiceImpl;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;

public class LFSLocoResumeDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSLocoResumeDAO.class);

	private static LFSLocoResumeDAO Instance = null;

	private LFSLocoResumeDAO() {
		super();
	}

	public static LFSLocoResumeDAO getInstance() {
		if (Instance == null)
			Instance = new LFSLocoResumeDAO();
		return Instance;
	}

	/**
	 * 娣诲姞鎴栦慨鏀�
	 * 
	 * @param wLFSLocoResume
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, LFSLocoResume wLFSLocoResume, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSLocoResume == null)
				return 0;

			String wSQL = "";
			if (wLFSLocoResume.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.lfs_locoresume(Code,OrderID,ProductID,CarNum,LFSBureauID,"
						+ "LFSSectionID,LFSManufacturerID,NewCreateDate,OnlineDate,LineID,Mileage,InPlantDate,"
						+ "OutPlantDate,Preparation,CreateID,CreateTime) VALUES(:Code,:OrderID,:ProductID,:CarNum,"
						+ ":LFSBureauID,:LFSSectionID,:LFSManufacturerID,:NewCreateDate,:OnlineDate,:LineID,"
						+ ":Mileage,:InPlantDate,:OutPlantDate,:Preparation,:CreateID,:CreateTime);", wInstance.Result);

				wLFSLocoResume.CreateTime = Calendar.getInstance();
				wLFSLocoResume.Code = GetNewCode(wLoginUser, wErrorCode);
			} else {
				wSQL = MessageFormat
						.format("UPDATE {0}.lfs_locoresume SET Code = :Code,OrderID = :OrderID,ProductID = :ProductID,"
								+ "CarNum = :CarNum,LFSBureauID = :LFSBureauID,LFSSectionID = :LFSSectionID,"
								+ "LFSManufacturerID = :LFSManufacturerID,NewCreateDate = :NewCreateDate,"
								+ "OnlineDate = :OnlineDate,LineID = :LineID,Mileage = :Mileage,"
								+ "InPlantDate = :InPlantDate,OutPlantDate = :OutPlantDate,Preparation = :Preparation,"
								+ "CreateID = :CreateID,CreateTime = now() WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSLocoResume.ID);
			wParamMap.put("Code", wLFSLocoResume.Code);
			wParamMap.put("OrderID", wLFSLocoResume.OrderID);
			wParamMap.put("ProductID", wLFSLocoResume.ProductID);
			wParamMap.put("CarNum", wLFSLocoResume.CarNum);
			wParamMap.put("LFSBureauID", wLFSLocoResume.LFSBureauID);
			wParamMap.put("LFSSectionID", wLFSLocoResume.LFSSectionID);
			wParamMap.put("LFSManufacturerID", wLFSLocoResume.LFSManufacturerID);
			wParamMap.put("NewCreateDate", wLFSLocoResume.NewCreateDate);
			wParamMap.put("OnlineDate", wLFSLocoResume.OnlineDate);
			wParamMap.put("LineID", wLFSLocoResume.LineID);
			wParamMap.put("Mileage", wLFSLocoResume.Mileage);
			wParamMap.put("InPlantDate", wLFSLocoResume.InPlantDate);
			wParamMap.put("OutPlantDate", wLFSLocoResume.OutPlantDate);
			wParamMap.put("Preparation", wLFSLocoResume.Preparation);
			wParamMap.put("CreateID", wLFSLocoResume.CreateID);
			wParamMap.put("CreateTime", wLFSLocoResume.CreateTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSLocoResume.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSLocoResume.setID(wResult);
			} else {
				wResult = wLFSLocoResume.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 鍒犻櫎闆嗗悎
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<LFSLocoResume> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (LFSLocoResume wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.lfs_locoresume WHERE ID IN({0}) ;",
					StringUtils.Join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 鏌ュ崟鏉�
	 * 
	 * @return
	 */
	public LFSLocoResume SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		LFSLocoResume wResult = new LFSLocoResume();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<LFSLocoResume> wList = SelectList(wLoginUser, wID, -1, "", -1, -1, -1, -1, null, null, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 鏉′欢鏌ヨ闆嗗悎
	 * 
	 * @return
	 */
	public List<LFSLocoResume> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, String wCarNum,
			int wLFSBureauID, int wLFSSectionID, int wLFSManufacturerID, int wLineID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<LFSLocoResume> wResultList = new ArrayList<LFSLocoResume>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.lfs_locoresume WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wCarNum is null or :wCarNum = '''' or :wCarNum = CarNum ) "
					+ "and ( :wLFSBureauID <= 0 or :wLFSBureauID = LFSBureauID ) "
					+ "and ( :wLFSSectionID <= 0 or :wLFSSectionID = LFSSectionID ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
					+ "and ( :wLFSManufacturerID <= 0 or :wLFSManufacturerID = LFSManufacturerID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wCarNum", wCarNum);
			wParamMap.put("wLFSBureauID", wLFSBureauID);
			wParamMap.put("wLFSSectionID", wLFSSectionID);
			wParamMap.put("wLFSManufacturerID", wLFSManufacturerID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			// 配属局
			List<LFSBureau> wBureauList = LFSServiceImpl.getInstance().LFS_QueryBureauList(wLoginUser).Result;
			// 配属段
			List<LFSSection> wSectionList = LFSServiceImpl.getInstance().LFS_QuerySectionList(wLoginUser, -1).Result;
			// 新造厂商
			List<LFSManufacturer> wManufList = LFSServiceImpl.getInstance()
					.LFS_QueryManufacturerList(wLoginUser).Result;

			SetValue(wResultList, wQueryResult, wBureauList, wSectionList, wManufList);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 根据订单ID查询机车履历
	 * 
	 * @return
	 */
	public List<LFSLocoResume> SelectByOrderID(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		List<LFSLocoResume> wResultList = new ArrayList<LFSLocoResume>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.lfs_locoresume WHERE  1=1  " + "and ( :wOrderID <= 0 or :wOrderID = OrderID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			// 配属局
			List<LFSBureau> wBureauList = LFSServiceImpl.getInstance().LFS_QueryBureauList(wLoginUser).Result;
			// 配属段
			List<LFSSection> wSectionList = LFSServiceImpl.getInstance().LFS_QuerySectionList(wLoginUser, -1).Result;
			// 新造厂商
			List<LFSManufacturer> wManufList = LFSServiceImpl.getInstance()
					.LFS_QueryManufacturerList(wLoginUser).Result;

			SetValue(wResultList, wQueryResult, wBureauList, wSectionList, wManufList);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<LFSLocoResume> wResultList, List<Map<String, Object>> wQueryResult,
			List<LFSBureau> wBureauList, List<LFSSection> wSectionList, List<LFSManufacturer> wManufList) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				LFSLocoResume wItem = new LFSLocoResume();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.CarNum = StringUtils.parseString(wReader.get("CarNum"));
				wItem.LFSBureauID = StringUtils.parseInt(wReader.get("LFSBureauID"));
				wItem.LFSSectionID = StringUtils.parseInt(wReader.get("LFSSectionID"));
				wItem.LFSManufacturerID = StringUtils.parseInt(wReader.get("LFSManufacturerID"));
				wItem.NewCreateDate = StringUtils.parseCalendar(wReader.get("NewCreateDate"));
				wItem.OnlineDate = StringUtils.parseCalendar(wReader.get("OnlineDate"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.Mileage = StringUtils.parseDouble(wReader.get("Mileage"));
				wItem.InPlantDate = StringUtils.parseCalendar(wReader.get("InPlantDate"));
				wItem.OutPlantDate = StringUtils.parseCalendar(wReader.get("OutPlantDate"));
				wItem.Preparation = StringUtils.parseString(wReader.get("Preparation"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));

				wItem.Creator = LFSConstants.GetBMSEmployeeName(wItem.CreateID);
				wItem.LFSBureauName = wBureauList.stream().anyMatch(p -> p.ID == wItem.LFSBureauID)
						? wBureauList.stream().filter(p -> p.ID == wItem.LFSBureauID).findFirst().get().Name
						: "";
				wItem.LFSManufacturerName = wManufList.stream().anyMatch(p -> p.ID == wItem.LFSManufacturerID)
						? wManufList.stream().filter(p -> p.ID == wItem.LFSManufacturerID).findFirst().get().Name
						: "";
				wItem.LFSSectionName = wSectionList.stream().anyMatch(p -> p.ID == wItem.LFSSectionID)
						? wSectionList.stream().filter(p -> p.ID == wItem.LFSSectionID).findFirst().get().Name
						: "";
				wItem.LineName = LFSConstants.GetFMCLineName(wItem.LineID);
				wItem.ProductNo = LFSConstants.GetFPCProductNo(wItem.ProductID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取最新的编码
	 */
	public String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select count(*)+1 as Number from {0}.lfs_locoresume;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				if (wReader.containsKey("Number")) {
					wNumber = StringUtils.parseInt(wReader.get("Number"));
					break;
				}
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			String wTimestamp = wSDF.format(Calendar.getInstance().getTime());

			wResult = StringUtils.Format("JC{0}{1}", wTimestamp, String.format("%03d", wNumber));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据上线时间查询机车履历
	 */
	public List<LFSLocoResume> SelectListByOnlineTime(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<LFSLocoResume> wResultList = new ArrayList<LFSLocoResume>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.lfs_locoresume WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) ;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			// 配属局
			List<LFSBureau> wBureauList = LFSServiceImpl.getInstance().LFS_QueryBureauList(wLoginUser).Result;
			// 配属段
			List<LFSSection> wSectionList = LFSServiceImpl.getInstance().LFS_QuerySectionList(wLoginUser, -1).Result;
			// 新造厂商
			List<LFSManufacturer> wManufList = LFSServiceImpl.getInstance()
					.LFS_QueryManufacturerList(wLoginUser).Result;

			SetValue(wResultList, wQueryResult, wBureauList, wSectionList, wManufList);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取所有重复的产线单元明细
	 */
	public List<FMCLineUnit> GetRepeatLineUnitList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<FMCLineUnit> wResult = new ArrayList<FMCLineUnit>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select distinct t1.ID,t1.UnitID,t1.ParentUnitID,t1.LineID,t1.ProductID "
					+ "from {0}.fmc_lineunit t1,{0}.fmc_lineunit t2 "
					+ "where t1.LevelID=3 and t2.LevelID=3 and t1.UnitID=t2.UnitID "
					+ "and t1.ParentUnitID!=t2.ParentUnitID order by t1.UnitID,t1.ProductID asc,t1.ParentUnitID asc;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FMCLineUnit wItem = new FMCLineUnit();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.UnitID = StringUtils.parseInt(wReader.get("UnitID"));
				wItem.ParentUnitID = StringUtils.parseInt(wReader.get("ParentUnitID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工序ID查询所属真实工位ID
	 */
	public int GetRealPartID(BMSEmployee wLoginUser, int wPartPointID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select PartID from {0}.fpc_routepartpoint where id in "
							+ "( select max(ID) from {0}.fpc_routepartpoint where PartPointID=:PartPointID);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("PartPointID", wPartPointID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("PartID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除产线单元明细
	 */
	public void DeleteLineUnitList(BMSEmployee wLoginUser, String wIDs, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("delete from {0}.fmc_lineunit where ID in ({1});", wInstance.Result, wIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
