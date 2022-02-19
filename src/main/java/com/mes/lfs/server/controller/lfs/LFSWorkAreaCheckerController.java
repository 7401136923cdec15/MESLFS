package com.mes.lfs.server.controller.lfs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.mes.lfs.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.lfs.server.service.utils.CloneTool;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.utils.RetCode;

/**
 * 工区检验员控制器
 * 
 * @author PengYouWang
 * @CreateTime 2020-3-4 14:43:42
 * @LastEditTime 2020-3-4 14:43:48
 */
@RestController
@RequestMapping("/api/LFSWorkAreaChecker")
public class LFSWorkAreaCheckerController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(LFSWorkAreaCheckerController.class);

	@Autowired
	LFSService wLFSService;

	/**
	 * 查单条
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<LFSWorkAreaChecker> wServiceResult = wLFSService.LFS_QueryWorkAreaChecker(wLoginUser, wID);

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
	 * 条件查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));
			int wActive = StringUtils.parseInt(request.getParameter("Active"));

			ServiceResult<List<LFSWorkAreaChecker>> wServiceResult = wLFSService
					.LFS_QueryWorkAreaCheckerList(wLoginUser, wID, wWorkAreaID, wActive);

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

	/**
	 * 新增或更新
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Update")
	public Object Update(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			LFSWorkAreaChecker wLFSWorkAreaChecker = CloneTool.Clone(wParam.get("data"), LFSWorkAreaChecker.class);
			if (wLFSWorkAreaChecker == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			ServiceResult<Integer> wServiceResult = wLFSService.LFS_UpdateWorkAreaChecker(wLoginUser,
					wLFSWorkAreaChecker);

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
	 * 批量激活或禁用
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Active")
	public Object Active(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			if (!wParam.containsKey("Active"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
			int wActive = StringUtils.parseInt(wParam.get("Active"));

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			List<LFSWorkAreaChecker> wList = CloneTool.CloneArray(wParam.get("data"), LFSWorkAreaChecker.class);
			if (wList == null || wList.size() <= 0)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			List<Integer> wIDList = wList.stream().map(p -> p.getID()).collect(Collectors.toList());

			wLFSService.LFS_ActiveWorkAreaCheckerList(wLoginUser, wIDList, wActive);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
