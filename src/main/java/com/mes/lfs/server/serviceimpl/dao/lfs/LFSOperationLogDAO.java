package com.mes.lfs.server.serviceimpl.dao.lfs;

import java.text.MessageFormat;
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
import com.mes.lfs.server.service.po.lfs.LFSOperationLog;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;

public class LFSOperationLogDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSOperationLogDAO.class);

	private static LFSOperationLogDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wLFSOperationLog
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, LFSOperationLog wLFSOperationLog, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSOperationLog == null)
				return 0;

			String wSQL = "";
			if (wLFSOperationLog.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.lfs_operationlog(Type,TypeName,CreateID,Creator,CreateTime,Content,SourceID) VALUES(:Type,:TypeName,:CreateID,:Creator,:CreateTime,:Content,:SourceID);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.lfs_operationlog SET Type = :Type,TypeName = :TypeName,CreateID = :CreateID,Creator = :Creator,CreateTime = :CreateTime,Content = :Content,SourceID=:SourceID WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSOperationLog.ID);
			wParamMap.put("Type", wLFSOperationLog.Type);
			wParamMap.put("TypeName", wLFSOperationLog.TypeName);
			wParamMap.put("CreateID", wLFSOperationLog.CreateID);
			wParamMap.put("Creator", wLFSOperationLog.Creator);
			wParamMap.put("CreateTime", wLFSOperationLog.CreateTime);
			wParamMap.put("Content", wLFSOperationLog.Content);
			wParamMap.put("SourceID", wLFSOperationLog.SourceID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSOperationLog.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSOperationLog.setID(wResult);
			} else {
				wResult = wLFSOperationLog.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<LFSOperationLog> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (LFSOperationLog wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.lfs_operationlog WHERE ID IN({0}) ;",
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
	public LFSOperationLog SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		LFSOperationLog wResult = new LFSOperationLog();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<LFSOperationLog> wList = SelectList(wLoginUser, wID, -1, -1, -1, null, null, wErrorCode);
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
	public List<LFSOperationLog> SelectList(BMSEmployee wLoginUser, int wID, int wSourceID, int wType, int wCreateID,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<LFSOperationLog> wResultList = new ArrayList<LFSOperationLog>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.lfs_operationlog WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wType <= 0 or :wType = Type ) "
					+ "and ( :wCreateID <= 0 or :wCreateID = CreateID ) "
					+ "and ( :wSourceID <= 0 or :wSourceID = SourceID ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wType", wType);
			wParamMap.put("wCreateID", wCreateID);
			wParamMap.put("wSourceID", wSourceID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSOperationLog wItem = new LFSOperationLog();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.TypeName = StringUtils.parseString(wReader.get("TypeName"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.Creator = StringUtils.parseString(wReader.get("Creator"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Content = StringUtils.parseString(wReader.get("Content"));
				wItem.SourceID = StringUtils.parseInt(wReader.get("SourceID"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private LFSOperationLogDAO() {
		super();
	}

	public static LFSOperationLogDAO getInstance() {
		if (Instance == null)
			Instance = new LFSOperationLogDAO();
		return Instance;
	}
}
