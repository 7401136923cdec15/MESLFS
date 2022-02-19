package com.mes.lfs.server.service;

import com.mes.lfs.server.service.po.APIResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.utils.Configuration;

public interface WMSService {

	static String ServerUrl = Configuration.readConfigString("wms.server.url", "config/config");

	static String ServerName = Configuration.readConfigString("wms.server.project.name", "config/config");

	APIResult MSS_QueryBOM(BMSEmployee wLoginUser,int wBOMID, String wBOMNo);
	
	APIResult MSS_QueryBOMAll(BMSEmployee wLoginUser, String wName,String wBOMNo,
			int wWorkShopID,int wBOMType, int wProductID, int wStatus);
	
	APIResult MSS_QueryUnitList(BMSEmployee wLoginUser);
	
	APIResult MSS_QueryMaterialList(BMSEmployee wLoginUser);
}
