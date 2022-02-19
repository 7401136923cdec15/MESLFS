package com.mes.lfs.server.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.mes.lfs.server.service.WMSService;
import com.mes.lfs.server.service.po.APIResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.utils.RemoteInvokeUtils;
import com.mes.lfs.server.service.utils.StringUtils;

public class WMSServiceImpl implements WMSService {

	private static Logger logger = LoggerFactory.getLogger(WMSServiceImpl.class);

	public WMSServiceImpl() {
		super();
	}

	private static WMSService Instance;

	public static WMSService getInstance() {
		if (Instance == null)
			Instance = new WMSServiceImpl();
		return Instance;
	}

	@Override
	public APIResult MSS_QueryBOM(BMSEmployee wLoginUser, int wBOMID, String wBOMNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_no", wBOMNo);
			wParms.put("bom_id", wBOMID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMAll(BMSEmployee wLoginUser, String wName, String wBOMNo, int wWorkShopID, int wBOMType,
			int wProductID, int wStatus) {
		;
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_no", wBOMNo);
			wParms.put("bom_name", wBOMNo);
			wParms.put("workshop_id", wWorkShopID);
			wParms.put("type_id", wBOMType);
			wParms.put("ProductID", wProductID);
			wParms.put("status", wStatus);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, StringUtils
					.Format("api/Bom/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	 

	@Override
	public APIResult MSS_QueryUnitList(BMSEmployee wLoginUser) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Unit/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryMaterialList(BMSEmployee wLoginUser) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

}
