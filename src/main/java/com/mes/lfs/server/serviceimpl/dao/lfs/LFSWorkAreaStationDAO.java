package com.mes.lfs.server.serviceimpl.dao.lfs;

import java.text.MessageFormat;
import java.util.ArrayList;
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
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;

/**
 * 工区工位
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-4-17 15:26:55
 *
 */
public class LFSWorkAreaStationDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSWorkAreaStationDAO.class);

	private static LFSWorkAreaStationDAO Instance = null;

	/**
	 * 权限码
	 */
	private static int AccessCode = 0;

	/**
	 * 添加或修改
	 * 
	 * @param wLFSWorkAreaStation
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, LFSWorkAreaStation wLFSWorkAreaStation, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSWorkAreaStation == null)
				return 0;

			String wSQL = "";
			if (wLFSWorkAreaStation.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.lfs_workareastation(WorkAreaID,StationID,CreateID,"
						+ "CreateTime,Active,OrderNum,StationType,PageNumber) VALUES(:WorkAreaID,:StationID,:CreateID,"
						+ ":CreateTime,:Active,:OrderNum,:StationType,:PageNumber);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.lfs_workareastation SET WorkAreaID = :WorkAreaID,"
						+ "StationID = :StationID,CreateID = :CreateID," + "CreateTime = :CreateTime,Active = :Active,"
						+ "OrderNum=:OrderNum," + "StationType=:StationType,PageNumber=:PageNumber WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSWorkAreaStation.ID);
			wParamMap.put("WorkAreaID", wLFSWorkAreaStation.WorkAreaID);
			wParamMap.put("StationID", wLFSWorkAreaStation.StationID);
			wParamMap.put("CreateID", wLFSWorkAreaStation.CreateID);
			wParamMap.put("CreateTime", wLFSWorkAreaStation.CreateTime);
			wParamMap.put("Active", wLFSWorkAreaStation.Active);
			wParamMap.put("OrderNum", wLFSWorkAreaStation.OrderNum);
			wParamMap.put("StationType", wLFSWorkAreaStation.StationType);
			wParamMap.put("PageNumber", wLFSWorkAreaStation.PageNumber);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSWorkAreaStation.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSWorkAreaStation.setID(wResult);
			} else {
				wResult = wLFSWorkAreaStation.getID();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public void DeleteList(BMSEmployee wLoginUser, List<LFSWorkAreaStation> wList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wList == null || wList.size() <= 0)
				return;

			List<String> wIDList = new ArrayList<String>();
			for (LFSWorkAreaStation wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.lfs_workareastation WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<LFSWorkAreaStation> SelectList(BMSEmployee wLoginUser, int wID, int wWorkAreaID, int wStationID,
			int wActive, OutResult<Integer> wErrorCode) {
		List<LFSWorkAreaStation> wResultList = new ArrayList<LFSWorkAreaStation>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.lfs_workareastation WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wWorkAreaID <= 0 or :wWorkAreaID = WorkAreaID ) "
					+ "and ( :wStationID <= 0 or :wStationID = StationID ) "
					+ "and ( :wActive <= 0 or :wActive = Active );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wWorkAreaID", wWorkAreaID);
			wParamMap.put("wStationID", wStationID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSWorkAreaStation wItem = new LFSWorkAreaStation();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.WorkAreaID = StringUtils.parseInt(wReader.get("WorkAreaID"));
				wItem.StationID = StringUtils.parseInt(wReader.get("StationID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.OrderNum = StringUtils.parseInt(wReader.get("OrderNum"));
				wItem.StationType = StringUtils.parseInt(wReader.get("StationType"));
				wItem.PageNumber = StringUtils.parseInt(wReader.get("PageNumber"));

				// 创建者名称
				wItem.Creator = LFSConstants.GetBMSEmployeeName(wItem.CreateID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private LFSWorkAreaStationDAO() {
		super();
	}

	public static LFSWorkAreaStationDAO getInstance() {
		if (Instance == null)
			Instance = new LFSWorkAreaStationDAO();
		return Instance;
	}
}
