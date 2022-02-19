package com.mes.lfs.server.serviceimpl.dao.oms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.lfs.server.service.mesenum.MESDBSource;
import com.mes.lfs.server.service.po.OutResult;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.oms.OMSOrder;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;

public class OMSOrderDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(OMSOrderDAO.class);

	private static OMSOrderDAO Instance = null;

	public static OMSOrderDAO getInstance() {
		if (Instance == null)
			Instance = new OMSOrderDAO();
		return Instance;
	}

	public List<OMSOrder> ConditionAll(BMSEmployee wLoginUser, int wProductID, int wLine, int wCustomerID,
			String wWBSNo, Calendar wStartTime, Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}
			if (wEndTime.compareTo(wStartTime) < 0) {
				return wResult;
			}
			if (StringUtils.isEmpty(wWBSNo)) {
				wWBSNo = "";
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual FROM {0}.oms_order t1 , {0}.oms_command t2 "
							+ "where t1.CommandID=t2.ID and ( :wCustomerID <= 0 or :wCustomerID = t2.CustomerID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ "and ( :wStatus <= 0 or :wStatus = t1.Status ) "
							+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ "and ( :wWBSNo is null or :wWBSNo = '''' or t2.WBSNo like ''%{1}%'') "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or (:wStartTime <= t1.PlanReceiveDate and t1.PlanReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealReceiveDate and t1.RealReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.PlanFinishDate and t1.PlanFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealStartDate and t1.RealStartDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealFinishDate and t1.RealFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.CreateTime and t1.CreateTime<=:wEndTime) "
							+ "or (:wStartTime <= t1.EditTime and t1.EditTime<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealSendDate and t1.RealSendDate<=:wEndTime) );",
					wInstance.Result, wWBSNo);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wLineID", Integer.valueOf(wLine));
			wParamMap.put("wCustomerID", Integer.valueOf(wCustomerID));
			wParamMap.put("wWBSNo", wWBSNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", wStatus);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResult, wQueryResult, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public String CreateNo(int wNumber) {
		String wResult = "";
		try {
			wResult = StringUtils.Format("PO-{0}",
					new Object[] { String.format("%07d", new Object[] { Integer.valueOf(wNumber) }) });
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private void SetVaue(BMSEmployee wLoginUser, List<OMSOrder> wResultList, List<Map<String, Object>> wQueryResult,
			OutResult<Integer> wErrorCode) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wItem = new OMSOrder();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.CommandID = StringUtils.parseInt(wReader.get("CommandID")).intValue();
				wItem.ERPID = StringUtils.parseInt(wReader.get("ERPID")).intValue();
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID")).intValue();
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID")).intValue();
				wItem.BureauSectionID = StringUtils.parseInt(wReader.get("BureauSectionID")).intValue();
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority")).intValue();
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.PlanReceiveDate = StringUtils.parseCalendar(wReader.get("PlanReceiveDate"));
				wItem.RealReceiveDate = StringUtils.parseCalendar(wReader.get("RealReceiveDate"));
				wItem.PlanFinishDate = StringUtils.parseCalendar(wReader.get("PlanFinishDate"));
				wItem.RealStartDate = StringUtils.parseCalendar(wReader.get("RealStartDate"));
				wItem.RealFinishDate = StringUtils.parseCalendar(wReader.get("RealFinishDate"));
				wItem.RealSendDate = StringUtils.parseCalendar(wReader.get("RealSendDate"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID")).intValue();
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID")).intValue();
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID")).intValue();
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active")).intValue();
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID")).intValue();
				wItem.TelegraphTime = StringUtils.parseCalendar(wReader.get("TelegraphTime"));
				wItem.TelegraphRealTime = StringUtils.parseCalendar(wReader.get("TelegraphRealTime"));

				wItem.LineName = LFSConstants.GetFMCLineName(wItem.LineID);
				wItem.ProductNo = (LFSConstants.GetFPCProduct(wItem.ProductID) == null) ? ""
						: (LFSConstants.GetFPCProduct(wItem.ProductID)).ProductNo;
				wItem.BureauSection = LFSConstants.GetCRMCustomerName(wItem.BureauSectionID);

				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID")).intValue();
				wItem.Customer = LFSConstants.GetCRMCustomerName(wItem.CustomerID);
				wItem.ContactCode = StringUtils.parseString(wReader.get("ContactCode"));
				wItem.No = StringUtils.parseString(wReader.get("No"));
				wItem.LinkManID = StringUtils.parseInt(wReader.get("LinkManID")).intValue();
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID")).intValue();
				wItem.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID")).intValue();
				wItem.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan")).intValue();
				wItem.FQTYActual = StringUtils.parseInt(wReader.get("FQTYActual")).intValue();

				wItem.TimeAway = StringUtils.parseCalendar(wReader.get("TimeAway"));
				wItem.CompletionTelegramTime = StringUtils.parseCalendar(wReader.get("CompletionTelegramTime"));
				wItem.DriverOnTime = StringUtils.parseCalendar(wReader.get("DriverOnTime"));
				wItem.ToSegmentTime = StringUtils.parseCalendar(wReader.get("ToSegmentTime"));
				wItem.ActualRepairStopTimes = StringUtils.parseInt(wReader.get("ActualRepairStopTimes"));
				wItem.TelegraphRepairStopTimes = StringUtils.parseInt(wReader.get("TelegraphRepairStopTimes"));
				wItem.InPlantStopTimes = StringUtils.parseInt(wReader.get("InPlantStopTimes"));
				wItem.OnTheWayStopTimes = StringUtils.parseInt(wReader.get("OnTheWayStopTimes"));
				wItem.PosterioriStopTimes = StringUtils.parseInt(wReader.get("PosterioriStopTimes"));

				wItem.OrderType = StringUtils.parseInt(wReader.get("OrderType"));
				wItem.ParentID = StringUtils.parseInt(wReader.get("ParentID"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
