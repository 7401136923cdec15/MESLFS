package com.mes.lfs.server.controller.lfs;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.lfs.server.controller.BaseController;
import com.mes.lfs.server.service.LFSService;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.lfs.LFSOperationLog;
import com.mes.lfs.server.service.po.lfs.LFSStoreHouse;
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.lfs.server.service.utils.CloneTool;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.serviceimpl.utils.lfs.LFSConstants;
import com.mes.lfs.server.utils.RetCode;

@RestController
@RequestMapping("/api/LFS")
public class LFSController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(LFSController.class);

	@Autowired
	LFSService wLFSService;

	@GetMapping("/StoreHouseAll")
	public Object StoreHouseAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wActive = StringUtils.parseInt(request.getParameter("Active"));

			ServiceResult<List<LFSStoreHouse>> wServiceResult = wLFSService.LFS_QueryStoreHouseList(wLoginUser, wID,
					wActive);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/StoreHouseUpdate")
	public Object StoreHouseUpdate(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			LFSStoreHouse wLFSStoreHouse = CloneTool.Clone(wParam.get("data"), LFSStoreHouse.class);

			if (wLFSStoreHouse != null) {
				wLFSStoreHouse.CreateID = wLoginUser.ID;
				wLFSStoreHouse.CreateTime = Calendar.getInstance();
			}

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_UpdateStoreHouse(wLoginUser, wLFSStoreHouse);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@GetMapping("/Move")
	public Object Move(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wLFSWorkStaionID = StringUtils.parseInt(request.getParameter("LFSWorkStaionID"));
			int wType = StringUtils.parseInt(request.getParameter("Type"));
			int wNumbers = StringUtils.parseInt(request.getParameter("Numbers"));

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_MoveItem(wLoginUser, wLFSWorkStaionID, wType,
					wNumbers);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@GetMapping("/WorkAreaAll")
	public Object WorkAreaAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));
			int wStationID = StringUtils.parseInt(request.getParameter("StationID"));
			int wActive = StringUtils.parseInt(request.getParameter("Active"));

			ServiceResult<List<LFSWorkAreaStation>> wServiceResult = wLFSService.LFS_QueryWorkAreaList(wLoginUser, wID,
					wWorkAreaID, wStationID, wActive);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}

			// 翻译中文
			if (wServiceResult.Result != null && wServiceResult.Result.size() > 0) {
				for (LFSWorkAreaStation wLFSWorkAreaStation : wServiceResult.Result) {
					// 工区
					wLFSWorkAreaStation.WorkArea = LFSConstants.GetBMSDepartmentName(wLFSWorkAreaStation.WorkAreaID);
					// 工位
					wLFSWorkAreaStation.StationName = LFSConstants.GetFPCPartName(wLFSWorkAreaStation.StationID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/WorkAreaUpdate")
	public Object WorkAreaUpdate(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}
			LFSWorkAreaStation wLFSWorkAreaStation = CloneTool.Clone(wParam.get("data"), LFSWorkAreaStation.class);

			if (wLFSWorkAreaStation == null) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_UpdateWorkArea(wLoginUser, wLFSWorkAreaStation);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 工序单元明细同步工序
	 */
	@GetMapping("/SynchronizedStep")
	public Object SynchronizedStep(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_SynchronizedStep(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 更新操作日志
	 */
	@PostMapping("/UpdateOperationLog")
	public Object UpdateOperationLog(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			LFSOperationLog wLFSOperationLog = CloneTool.Clone(wParam.get("data"), LFSOperationLog.class);

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_UpdateOperationLog(wLoginUser, wLFSOperationLog);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询操作日志
	 */
	@GetMapping("/OperationLogAll")
	public Object OperationLogAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wSourceID = StringUtils.parseInt(request.getParameter("SourceID"));
			int wType = StringUtils.parseInt(request.getParameter("Type"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<LFSOperationLog>> wServiceResult = wLFSService.LFS_QueryOperationLogAll(wLoginUser,
					wType, wSourceID, wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}