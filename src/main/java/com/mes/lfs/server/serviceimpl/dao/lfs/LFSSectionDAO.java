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
import com.mes.lfs.server.service.mesenum.MESException;
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.lfs.LFSSection;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;

public class LFSSectionDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSSectionDAO.class);

	private static LFSSectionDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wLFSSection
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, LFSSection wLFSSection, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSSection == null)
				return 0;

			String wSQL = "";
			if (wLFSSection.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.lfs_section(BureauID,Name) VALUES(:BureauID,:Name);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.lfs_section SET BureauID = :BureauID,Name = :Name WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSSection.ID);
			wParamMap.put("BureauID", wLFSSection.BureauID);
			wParamMap.put("Name", wLFSSection.Name);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSSection.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSSection.setID(wResult);
			} else {
				wResult = wLFSSection.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<LFSSection> wList,
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
			for (LFSSection wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.lfs_section WHERE ID IN({0}) ;",
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
	public LFSSection SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		LFSSection wResult = new LFSSection();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<LFSSection> wList = SelectList(wLoginUser, wID, -1, wErrorCode);
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
	public List<LFSSection> SelectList(BMSEmployee wLoginUser, int wID, int wBureauID, OutResult<Integer> wErrorCode) {
		List<LFSSection> wResultList = new ArrayList<LFSSection>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat
					.format("SELECT * FROM {0}.lfs_section WHERE  1=1  and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wBureauID <= 0 or :wBureauID = BureauID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wBureauID", wBureauID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSSection wItem = new LFSSection();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.BureauID = StringUtils.parseInt(wReader.get("BureauID"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private LFSSectionDAO() {
		super();
	}

	public static LFSSectionDAO getInstance() {
		if (Instance == null)
			Instance = new LFSSectionDAO();
		return Instance;
	}
}
