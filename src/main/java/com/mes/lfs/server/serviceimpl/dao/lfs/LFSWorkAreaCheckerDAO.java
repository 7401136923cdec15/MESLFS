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

import com.mes.lfs.server.service.mesenum.BMSDepartmentType;
import com.mes.lfs.server.service.mesenum.BMSDutyType;
import com.mes.lfs.server.service.mesenum.MESDBSource;
import com.mes.lfs.server.service.mesenum.MESException;
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;

/**
 * 工区检验员【工区人员设置】
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-4-29 14:33:07
 *
 */
public class LFSWorkAreaCheckerDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSWorkAreaCheckerDAO.class);

	private static LFSWorkAreaCheckerDAO Instance = null;

	/**
	 * 权限码
	 */
	private static int AccessCode = 502500;

	/**
	 * 添加或修改
	 * 
	 * @param wLFSWorkAreaChecker
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, LFSWorkAreaChecker wLFSWorkAreaChecker, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSWorkAreaChecker == null)
				return 0;

			String wSQL = "";
			if (wLFSWorkAreaChecker.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.lfs_workareachecker(WorkAreaID,CheckerIDList,CreateID,CreateTime,Active) "
								+ "VALUES(:WorkAreaID,:CheckerIDList,:CreateID,:CreateTime,:Active);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.lfs_workareachecker SET "
						+ "WorkAreaID = :WorkAreaID,CheckerIDList = :CheckerIDList,"
						+ "Active = :Active WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSWorkAreaChecker.ID);
			wParamMap.put("WorkAreaID", wLFSWorkAreaChecker.WorkAreaID);
			wParamMap.put("CheckerIDList", StringUtils.Join(";", wLFSWorkAreaChecker.CheckerIDList));
			wParamMap.put("CreateID", wLFSWorkAreaChecker.CreateID);
			wParamMap.put("CreateTime", wLFSWorkAreaChecker.CreateTime);
			wParamMap.put("Active", wLFSWorkAreaChecker.Active);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSWorkAreaChecker.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSWorkAreaChecker.setID(wResult);
			} else {
				wResult = wLFSWorkAreaChecker.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<LFSWorkAreaChecker> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (LFSWorkAreaChecker wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.lfs_workareachecker WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public LFSWorkAreaChecker SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		LFSWorkAreaChecker wResult = new LFSWorkAreaChecker();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<LFSWorkAreaChecker> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<LFSWorkAreaChecker> SelectList(BMSEmployee wLoginUser, int wID, int wWorkAreaID, int wActive,
			OutResult<Integer> wErrorCode) {
		List<LFSWorkAreaChecker> wResultList = new ArrayList<LFSWorkAreaChecker>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.lfs_workareachecker WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wWorkAreaID <= 0 or :wWorkAreaID = WorkAreaID ) "
					+ "and ( :wActive < 0 or :wActive = Active );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wWorkAreaID", wWorkAreaID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSWorkAreaChecker wItem = new LFSWorkAreaChecker();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.WorkAreaID = StringUtils.parseInt(wReader.get("WorkAreaID"));

				wItem.CheckerIDList = StringUtils
						.parseIntList((StringUtils.parseString(wReader.get("CheckerIDList")).split(";")));
				wItem.LeaderIDList = this.SelectPersonIDList(wLoginUser, wItem.WorkAreaID,
						BMSDepartmentType.Area.getValue(), BMSDutyType.Director.getValue(), wErrorCode);
				wItem.ScheduleIDList = this.SelectPersonIDList(wLoginUser, wItem.WorkAreaID,
						BMSDepartmentType.Area.getValue(), BMSDutyType.Scheduler.getValue(), wErrorCode);
				if (wItem.ScheduleIDList == null || wItem.ScheduleIDList.size() <= 0) {
					wItem.ScheduleIDList = wItem.LeaderIDList;
				}
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));

				wItem.WorkArea = LFSConstants.GetBMSDepartmentName(wItem.WorkAreaID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 批量激活或禁用
	 */
	public ServiceResult<Integer> Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.EXC, AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return wResult;
			for (Integer wItem : wIDList) {
				LFSWorkAreaChecker wLFSWorkAreaChecker = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wLFSWorkAreaChecker == null || wLFSWorkAreaChecker.ID <= 0)
					continue;
				// 只有激活的才能禁用
				if (wActive == 2 && wLFSWorkAreaChecker.Active != 1) {
					wErrorCode.set(MESException.Logic.getValue());
					return wResult;
				}
				wLFSWorkAreaChecker.Active = wActive;
				long wID = Update(wLoginUser, wLFSWorkAreaChecker, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据部门ID、部门类型、职责类型获取人员ID集合
	 * 
	 * @param wLoginUser      登录信息
	 * @param wDepartmentID   部门ID
	 * @param wDepartmentType 部门类型
	 * @param wDutyID         职责类型
	 * @return 人员ID集合
	 */
	public List<Integer> SelectPersonIDList(BMSEmployee wLoginUser, int wDepartmentID, int wDepartmentType, int wDutyID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("SELECT t1.ID FROM {0}.mbs_user t1 "
					+ "	LEFT JOIN {0}.bms_department t2 ON t1.DepartmentID=t2.ID "
					+ "    LEFT JOIN {0}.bms_position t3 ON t1.Position=t3.ID " + "	WHERE t2.Type=:wDepartmentType "
					+ "AND t3.DutyID=:wDutyID AND t1.Active=1 " + "AND t1.DepartmentID=:wDepartmentID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wDepartmentID", wDepartmentID);
			wParamMap.put("wDepartmentType", wDepartmentType);
			wParamMap.put("wDutyID", wDutyID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private LFSWorkAreaCheckerDAO() {
		super();
	}

	public static LFSWorkAreaCheckerDAO getInstance() {
		if (Instance == null)
			Instance = new LFSWorkAreaCheckerDAO();
		return Instance;
	}
}
