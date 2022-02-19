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
import com.mes.lfs.server.service.po.lfs.LFSStoreHouse;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;

/**
 * 库位配置
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-4-17 15:26:30
 *
 */
public class LFSStoreHouseDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(LFSStoreHouseDAO.class);

	private static LFSStoreHouseDAO Instance = null;

	/**
	 * 权限码
	 */
	private static int AccessCode = 501600;

	/**
	 * 添加或修改
	 * 
	 * @param wLFSStoreHouse
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, LFSStoreHouse wLFSStoreHouse, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wLFSStoreHouse == null)
				return 0;

			String wSQL = "";
			if (wLFSStoreHouse.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.lfs_storehouse(Name,Code,Capacity,"
						+ "CreateID,CreateTime,Active,Length,Type,GateDoorWorkSpaceIDList,MoveStoreHouseIDList,AreaID) "
						+ "VALUES(:Name,:Code,:Capacity,:CreateID,"
						+ ":CreateTime,:Active,:Length,:Type,:GateDoorWorkSpaceIDList,:MoveStoreHouseIDList,:AreaID);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.lfs_storehouse SET Name = :Name,"
								+ "Code = :Code,Capacity = :Capacity,CreateID = :CreateID,"
								+ "CreateTime = :CreateTime,Active = :Active,Length=:Length,"
								+ "Type=:Type,GateDoorWorkSpaceIDList=:GateDoorWorkSpaceIDList,"
								+ "MoveStoreHouseIDList=:MoveStoreHouseIDList,AreaID=:AreaID WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wLFSStoreHouse.ID);
			wParamMap.put("Name", wLFSStoreHouse.Name);
			wParamMap.put("Code", wLFSStoreHouse.Code);
			wParamMap.put("Capacity", wLFSStoreHouse.Capacity);
			wParamMap.put("CreateID", wLFSStoreHouse.CreateID);
			wParamMap.put("CreateTime", wLFSStoreHouse.CreateTime);
			wParamMap.put("Active", wLFSStoreHouse.Active);
			wParamMap.put("Length", wLFSStoreHouse.Length);
			wParamMap.put("Type", wLFSStoreHouse.Type);
			wParamMap.put("GateDoorWorkSpaceIDList", StringUtils.Join(";", wLFSStoreHouse.GateDoorWorkSpaceIDList));
			wParamMap.put("MoveStoreHouseIDList", StringUtils.Join(";", wLFSStoreHouse.MoveStoreHouseIDList));
			wParamMap.put("AreaID", wLFSStoreHouse.AreaID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wLFSStoreHouse.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wLFSStoreHouse.setID(wResult);
			} else {
				wResult = wLFSStoreHouse.getID();
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
	public void DeleteList(BMSEmployee wLoginUser, List<LFSStoreHouse> wList, OutResult<Integer> wErrorCode) {
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
			for (LFSStoreHouse wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from lfs_storehouse WHERE ID IN({0}) ;",
					String.join(",", wIDList));
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
	public List<LFSStoreHouse> SelectList(BMSEmployee wLoginUser, int wID, int wActive, OutResult<Integer> wErrorCode) {
		List<LFSStoreHouse> wResultList = new ArrayList<LFSStoreHouse>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.lfs_storehouse WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wActive <= 0 or :wActive = Active );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSStoreHouse wItem = new LFSStoreHouse();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.Capacity = StringUtils.parseInt(wReader.get("Capacity"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.Length = StringUtils.parseInt(wReader.get("Length"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.GateDoorWorkSpaceIDList = StringUtils
						.parseIntList((StringUtils.parseString(wReader.get("GateDoorWorkSpaceIDList")).split(";")));
				wItem.MoveStoreHouseIDList = StringUtils
						.parseIntList((StringUtils.parseString(wReader.get("MoveStoreHouseIDList")).split(";")));
				wItem.AreaID = StringUtils.parseInt(wReader.get("AreaID"));

				// 创建人名称
				wItem.Creator = LFSConstants.GetBMSEmployeeName(wItem.CreateID);
				wItem.AreaName = LFSConstants.GetBMSDepartmentName(wItem.AreaID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private LFSStoreHouseDAO() {
		super();
	}

	public static LFSStoreHouseDAO getInstance() {
		if (Instance == null)
			Instance = new LFSStoreHouseDAO();
		return Instance;
	}
}
