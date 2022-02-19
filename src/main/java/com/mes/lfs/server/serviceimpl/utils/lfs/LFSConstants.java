package com.mes.lfs.server.serviceimpl.utils.lfs;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.mes.lfs.server.service.mesenum.APSShiftPeriod;
import com.mes.lfs.server.service.po.bms.BMSDepartment;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.bms.BMSPosition;
import com.mes.lfs.server.service.po.bms.BMSWorkCharge;
import com.mes.lfs.server.service.po.crm.CRMCustomer;
import com.mes.lfs.server.service.po.fmc.FMCBusinessUnit;
import com.mes.lfs.server.service.po.fmc.FMCFactory;
import com.mes.lfs.server.service.po.fmc.FMCLine;
import com.mes.lfs.server.service.po.fmc.FMCStation;
import com.mes.lfs.server.service.po.fmc.FMCWorkShop;
import com.mes.lfs.server.service.po.fmc.FMCWorkspace;
import com.mes.lfs.server.service.po.fpc.FPCPart;
import com.mes.lfs.server.service.po.fpc.FPCPartPoint;
import com.mes.lfs.server.service.po.fpc.FPCProduct;
import com.mes.lfs.server.service.po.mss.MSSMaterial;
import com.mes.lfs.server.serviceimpl.CoreServiceImpl;
import com.mes.lfs.server.serviceimpl.FMCServiceImpl;
import com.mes.lfs.server.serviceimpl.SCMServiceImpl;
import com.mes.lfs.server.serviceimpl.WMSServiceImpl;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.serviceimpl.utils.MESServer;

public class LFSConstants {
	private static Logger logger = LoggerFactory.getLogger(LFSConstants.class);

	public LFSConstants() {
		// TODO Auto-generated constructor stub
	}

	private static String ConfigPath = null;

	public static synchronized String getConfigPath() {
		if (ConfigPath == null) {
			try {
				ConfigPath = ResourceUtils.getURL("classpath:config").getPath().replace("%20", " ");

				if (ConfigPath != null && ConfigPath.length() > 3 && ConfigPath.indexOf(":") > 0) {
					if (ConfigPath.indexOf("/") == 0)
						ConfigPath = ConfigPath.substring(1);

					if (!ConfigPath.endsWith("/"))
						ConfigPath = ConfigPath + "/";
				}
			} catch (FileNotFoundException e) {
				return "config/";
			}
		}
		return ConfigPath;
	}

	// region ShiftID
	private static Calendar RefreshShiftTime = Calendar.getInstance();

	private static Map<Integer, Integer> ShiftIDL = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> ShiftID = new HashMap<Integer, Integer>();;

	public static synchronized int GetShiftLID(BMSEmployee wLoginUser, int wWorkShopID) {
		int wResult = 0;
		try {

			if (!ShiftIDL.containsKey(wWorkShopID) || ShiftIDL.get(wWorkShopID) <= 0) {
				ShiftIDL.put(wWorkShopID, MESServer.MES_QueryShiftID(wLoginUser, wWorkShopID, Calendar.getInstance(),
						APSShiftPeriod.Shift, -1));
			}
			wResult = ShiftIDL.get(wWorkShopID);

		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	// 接口函数
	public static synchronized int GetShiftID(BMSEmployee wLoginUser, int wWorkShopID) {
		int wResult = 0;
		try {

			if (!ShiftID.containsKey(wWorkShopID))
				ShiftID.put(wWorkShopID, 0);

			if (ShiftID.get(wWorkShopID) <= 0 || RefreshShiftTime.compareTo(Calendar.getInstance()) <= 0) {

				wResult = MESServer.MES_QueryShiftID(wLoginUser, wWorkShopID, Calendar.getInstance(),
						APSShiftPeriod.Shift, 0);

				if (wResult != ShiftID.get(wWorkShopID)) {
					ShiftIDL.put(wWorkShopID, ShiftID.get(wWorkShopID));
					ShiftID.put(wWorkShopID, wResult);

				}
				RefreshShiftTime = Calendar.getInstance();
				RefreshShiftTime.add(Calendar.MINUTE, 3);
			} else {
				wResult = ShiftID.get(wWorkShopID);
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	// endRegion

	// region 台位全局数据
	private static Calendar RefreshWorkspaceTime = Calendar.getInstance();

	private static Map<Integer, FMCWorkspace> FMCWorkspaceDic = new HashMap<Integer, FMCWorkspace>();

	public static synchronized Map<Integer, FMCWorkspace> GetFMCWorkspaceList() {
		if (FMCWorkspaceDic == null || FMCWorkspaceDic.size() <= 0
				|| RefreshWorkspaceTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkspace> wFMCWorkspaceList = FMCServiceImpl.getInstance()
					.FMC_GetFMCWorkspaceList(BaseDAO.SysAdmin, 0, 0, "", 0, 0).List(FMCWorkspace.class);
			if (wFMCWorkspaceList != null && wFMCWorkspaceList.size() > 0) {
				FMCWorkspaceDic = wFMCWorkspaceList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkspaceTime = Calendar.getInstance();
			RefreshWorkspaceTime.add(Calendar.MINUTE, 3);
		}
		return FMCWorkspaceDic;
	}

	public static String GetFMCWorkspaceName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCWorkspaceList().containsKey(wID)) {
			if (LFSConstants.GetFMCWorkspaceList().get(wID) != null) {
				wResult = LFSConstants.GetFMCWorkspaceList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCWorkspace GetFMCWorkspace(int wID) {
		FMCWorkspace wResult = new FMCWorkspace();
		if (LFSConstants.GetFMCWorkspaceList().containsKey(wID)) {
			if (LFSConstants.GetFMCWorkspaceList().get(wID) != null) {
				wResult = LFSConstants.GetFMCWorkspaceList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 用户全局数据
	private static Calendar RefreshEmployeeTime = Calendar.getInstance();

	private static Map<Integer, BMSEmployee> BMSEmployeeDic = new HashMap<Integer, BMSEmployee>();

	public static synchronized Map<Integer, BMSEmployee> GetBMSEmployeeList() {
		if (BMSEmployeeDic == null || BMSEmployeeDic.size() <= 0
				|| RefreshEmployeeTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSEmployee> wBMSEmployeeList = CoreServiceImpl.getInstance()
					.BMS_GetEmployeeAll(BaseDAO.SysAdmin, 0, 0, -1).List(BMSEmployee.class);
			if (wBMSEmployeeList != null && wBMSEmployeeList.size() > 0) {
				BMSEmployeeDic = wBMSEmployeeList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshEmployeeTime = Calendar.getInstance();
			RefreshEmployeeTime.add(Calendar.MINUTE, 3);
		}
		return BMSEmployeeDic;
	}

	public static BMSEmployee GetBMSEmployee(int wID) {
		if (LFSConstants.GetBMSEmployeeList().containsKey(wID)) {
			if (LFSConstants.GetBMSEmployeeList().get(wID) != null) {
				return LFSConstants.GetBMSEmployeeList().get(wID);
			}
		}
		return new BMSEmployee();
	}

	public static String GetBMSEmployeeName(int wID) {
		String wResult = "";
		if (LFSConstants.GetBMSEmployeeList().containsKey(wID)) {
			if (LFSConstants.GetBMSEmployeeList().get(wID) != null) {
				wResult = LFSConstants.GetBMSEmployeeList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	// region 部门全局数据
	private static Calendar RefreshDepartmentTime = Calendar.getInstance();

	private static Map<Integer, BMSDepartment> BMSDepartmentDic = new HashMap<Integer, BMSDepartment>();

	public static synchronized Map<Integer, BMSDepartment> GetBMSDepartmentList() {
		if (BMSDepartmentDic == null || BMSDepartmentDic.size() <= 0
				|| RefreshDepartmentTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSDepartment> wBMSDepartmentList = CoreServiceImpl.getInstance()
					.BMS_QueryDepartmentList(BaseDAO.SysAdmin, 0, 0).List(BMSDepartment.class);
			if (wBMSDepartmentList != null && wBMSDepartmentList.size() > 0) {
				BMSDepartmentDic = wBMSDepartmentList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshDepartmentTime = Calendar.getInstance();
			RefreshDepartmentTime.add(Calendar.MINUTE, 3);
		}
		return BMSDepartmentDic;
	}

	public static String GetBMSDepartmentName(int wID) {
		String wResult = "";
		if (LFSConstants.GetBMSDepartmentList().containsKey(wID)) {
			if (LFSConstants.GetBMSDepartmentList().get(wID) != null) {
				wResult = LFSConstants.GetBMSDepartmentList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	// region 岗位全局数据
	private static Calendar RefreshPositionTime = Calendar.getInstance();

	private static Map<Integer, BMSPosition> BMSPositionDic = new HashMap<Integer, BMSPosition>();

	public static synchronized Map<Integer, BMSPosition> GetBMSPositionList() {
		if (BMSPositionDic == null || BMSPositionDic.size() <= 0
				|| RefreshPositionTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSPosition> wBMSPositionList = CoreServiceImpl.getInstance()
					.BMS_QueryPositionList(BaseDAO.SysAdmin, 0).List(BMSPosition.class);
			if (wBMSPositionList != null && wBMSPositionList.size() > 0) {
				BMSPositionDic = wBMSPositionList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPositionTime = Calendar.getInstance();
			RefreshPositionTime.add(Calendar.MINUTE, 3);
		}
		return BMSPositionDic;
	}

	public static String GetBMSPositionName(int wID) {
		String wResult = "";
		if (LFSConstants.GetBMSPositionList().containsKey(wID)) {
			if (LFSConstants.GetBMSPositionList().get(wID) != null) {
				wResult = LFSConstants.GetBMSPositionList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	// region 产线全局数据
	private static Calendar RefreshLineTime = Calendar.getInstance();

	private static Map<Integer, FMCLine> FMCLineDic = new HashMap<Integer, FMCLine>();

	public static synchronized Map<Integer, FMCLine> GetFMCLineList() {
		if (FMCLineDic == null || FMCLineDic.size() <= 0 || RefreshLineTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCLine> wFMCLineList = FMCServiceImpl.getInstance().FMC_QueryLineList(BaseDAO.SysAdmin, 0, 0, 0)
					.List(FMCLine.class);
			if (wFMCLineList != null && wFMCLineList.size() > 0) {
				FMCLineDic = wFMCLineList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshLineTime = Calendar.getInstance();
			RefreshLineTime.add(Calendar.MINUTE, 3);
		}
		return FMCLineDic;
	}

	public static String GetFMCLineName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCLineList().containsKey(wID)) {
			if (LFSConstants.GetFMCLineList().get(wID) != null) {
				wResult = LFSConstants.GetFMCLineList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCLine GetFMCLine(int wID) {
		FMCLine wResult = new FMCLine();
		if (LFSConstants.GetFMCLineList().containsKey(wID)) {
			if (LFSConstants.GetFMCLineList().get(wID) != null) {
				wResult = LFSConstants.GetFMCLineList().get(wID);
			}
		}
		return wResult;
	}

	public static FMCLine GetFMCLine(String wName) {
		for (FMCLine wFMCLine : LFSConstants.GetFMCLineList().values()) {
			if (wFMCLine.Name.equals(wName))
				return wFMCLine;
		}
		return new FMCLine();
	}

	// endRegion

	// region 班组工位全局数据
	private static Calendar RefreshWorkChargeTime = Calendar.getInstance();

	private static Map<Integer, BMSWorkCharge> BMSWorkChargeDic = new HashMap<Integer, BMSWorkCharge>();

	public static synchronized Map<Integer, BMSWorkCharge> GetBMSWorkChargeList() {
		if (BMSWorkChargeDic == null || BMSWorkChargeDic.size() <= 0
				|| RefreshWorkChargeTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSWorkCharge> wBMSWorkChargeList = CoreServiceImpl.getInstance()
					.FMC_QueryWorkChargeList(BaseDAO.SysAdmin).List(BMSWorkCharge.class);
			if (wBMSWorkChargeList != null && wBMSWorkChargeList.size() > 0) {
				BMSWorkChargeDic = wBMSWorkChargeList.stream()
						.collect(Collectors.toMap(p -> p.StationID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkChargeTime = Calendar.getInstance();
			RefreshWorkChargeTime.add(Calendar.MINUTE, 3);
		}
		return BMSWorkChargeDic;
	}

	public static BMSWorkCharge GetBMSWorkCharge(int wID) {
		BMSWorkCharge wResult = new BMSWorkCharge();
		if (LFSConstants.GetBMSWorkChargeList().containsKey(wID)) {
			if (LFSConstants.GetBMSWorkChargeList().get(wID) != null) {
				wResult = LFSConstants.GetBMSWorkChargeList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 车间全局数据
	private static Calendar RefreshWorkShopTime = Calendar.getInstance();

	private static Map<Integer, FMCWorkShop> FMCWorkShopDic = new HashMap<Integer, FMCWorkShop>();

	public static synchronized Map<Integer, FMCWorkShop> GetFMCWorkShopList() {
		if (FMCWorkShopDic == null || FMCWorkShopDic.size() <= 0
				|| RefreshWorkShopTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkShop> wFMCWorkShopList = FMCServiceImpl.getInstance()
					.FMC_QueryWorkShopList(BaseDAO.SysAdmin, 0, 0).List(FMCWorkShop.class);
			if (wFMCWorkShopList != null && wFMCWorkShopList.size() > 0) {
				FMCWorkShopDic = wFMCWorkShopList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkShopTime = Calendar.getInstance();
			RefreshWorkShopTime.add(Calendar.MINUTE, 3);
		}
		return FMCWorkShopDic;
	}

	public static String GetFMCWorkShopName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCWorkShopList().containsKey(wID)) {
			if (LFSConstants.GetFMCWorkShopList().get(wID) != null) {
				wResult = LFSConstants.GetFMCWorkShopList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCWorkShop GetFMCWorkShop(int wID) {
		FMCWorkShop wResult = new FMCWorkShop();
		if (LFSConstants.GetFMCWorkShopList().containsKey(wID)) {
			if (LFSConstants.GetFMCWorkShopList().get(wID) != null) {
				wResult = LFSConstants.GetFMCWorkShopList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 工段全局数据
	private static Calendar RefreshPartTime = Calendar.getInstance();

	private static Map<Integer, FPCPart> FPCPartDic = new HashMap<Integer, FPCPart>();

	public static synchronized Map<Integer, FPCPart> GetFPCPartList() {
		if (FPCPartDic == null || FPCPartDic.size() <= 0 || RefreshPartTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPart> wFPCPartList = FMCServiceImpl.getInstance().FPC_QueryPartList(BaseDAO.SysAdmin, 0, 0, 0)
					.List(FPCPart.class);
			if (wFPCPartList != null && wFPCPartList.size() > 0) {
				FPCPartDic = wFPCPartList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPartTime = Calendar.getInstance();
			RefreshPartTime.add(Calendar.MINUTE, 3);
		}
		return FPCPartDic;
	}

	public static String GetFPCPartName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFPCPartList().containsKey(wID)) {
			if (LFSConstants.GetFPCPartList().get(wID) != null) {
				wResult = LFSConstants.GetFPCPartList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FPCPart GetFPCPart(String wName) {
		for (FPCPart wFPCPart : LFSConstants.GetFPCPartList().values()) {
			if (wFPCPart.Name.equals(wName))
				return wFPCPart;
		}
		return new FPCPart();
	}

	// endRegion

	// region 产品全局数据
	private static Calendar RefreshProductTime = Calendar.getInstance();

	private static Map<Integer, FPCProduct> FPCProductDic = new HashMap<Integer, FPCProduct>();

	public static synchronized Map<Integer, FPCProduct> GetFPCProductList() {
		if (FPCProductDic == null || FPCProductDic.size() <= 0
				|| RefreshProductTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCProduct> wFPCProductList = FMCServiceImpl.getInstance().FPC_QueryProductList(BaseDAO.SysAdmin, 0, 0)
					.List(FPCProduct.class);
			if (wFPCProductList != null && wFPCProductList.size() > 0) {
				FPCProductDic = wFPCProductList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshProductTime = Calendar.getInstance();
			RefreshProductTime.add(Calendar.MINUTE, 3);
		}
		return FPCProductDic;
	}

	public static FPCProduct GetFPCProduct(int wID) {
		if (LFSConstants.GetFPCProductList().containsKey(wID)) {
			if (LFSConstants.GetFPCProductList().get(wID) != null) {
				return LFSConstants.GetFPCProductList().get(wID);
			}
		}
		return new FPCProduct();
	}

	public static FPCProduct GetFPCProduct(String wProductNo) {
		for (FPCProduct wFPCProduct : LFSConstants.GetFPCProductList().values()) {
			if (wFPCProduct.ProductNo.equals(wProductNo)) {
				return wFPCProduct;
			}
		}
		return new FPCProduct();
	}

	public static String GetFPCProductName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFPCProductList().containsKey(wID)) {
			if (LFSConstants.GetFPCProductList().get(wID) != null) {
				wResult = LFSConstants.GetFPCProductList().get(wID).getProductName();
			}
		}
		return wResult;
	}

	public static String GetFPCProductNo(int wID) {
		String wResult = "";
		if (LFSConstants.GetFPCProductList().containsKey(wID)) {
			if (LFSConstants.GetFPCProductList().get(wID) != null) {
				wResult = LFSConstants.GetFPCProductList().get(wID).getProductNo();
			}
		}
		return wResult;
	}

	// endRegion

	// region 工序全局数据
	private static Calendar RefreshStepTime = Calendar.getInstance();

	private static Map<Integer, FPCPartPoint> FPCStepDic = new HashMap<Integer, FPCPartPoint>();

	public static synchronized Map<Integer, FPCPartPoint> GetFPCStepList() {
		if (FPCStepDic == null || FPCStepDic.size() <= 0 || RefreshStepTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPartPoint> wFPCStepList = FMCServiceImpl.getInstance()
					.FPC_QueryPartPointList(BaseDAO.SysAdmin, 0, 0, 0).List(FPCPartPoint.class);
			if (wFPCStepList != null && wFPCStepList.size() > 0) {
				FPCStepDic = wFPCStepList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshStepTime = Calendar.getInstance();
			RefreshStepTime.add(Calendar.MINUTE, 3);
		}
		return FPCStepDic;
	}

	public static String GetFPCStepName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFPCStepList().containsKey(wID)) {
			if (LFSConstants.GetFPCStepList().get(wID) != null) {
				wResult = LFSConstants.GetFPCStepList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FPCPartPoint GetFPCStep(String wName) {
		for (FPCPartPoint wFPCPartPoint : LFSConstants.GetFPCStepList().values()) {
			if (wFPCPartPoint.Name.equals(wName))
				return wFPCPartPoint;
		}
		return new FPCPartPoint();
	}

	// endRegion

	// region 工位全局数据
	private static Calendar RefreshStationTime = Calendar.getInstance();

	private static Map<Integer, FMCStation> FMCStationDic = new HashMap<Integer, FMCStation>();

	public static synchronized Map<Integer, FMCStation> GetFMCStationList() {
		if (FMCStationDic == null || FMCStationDic.size() <= 0
				|| RefreshStationTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCStation> wFMCStationList = FMCServiceImpl.getInstance().FMC_QueryStationList(BaseDAO.SysAdmin, 0, 0)
					.List(FMCStation.class);
			if (wFMCStationList != null && wFMCStationList.size() > 0) {
				FMCStationDic = wFMCStationList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshStationTime = Calendar.getInstance();
			RefreshStationTime.add(Calendar.MINUTE, 3);
		}
		return FMCStationDic;
	}

	public static String GetFMCStationName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCStationList().containsKey(wID)) {
			if (LFSConstants.GetFMCStationList().get(wID) != null) {
				wResult = LFSConstants.GetFMCStationList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCStation GetFMCStation(int wID) {
		FMCStation wResult = new FMCStation();
		if (LFSConstants.GetFMCStationList().containsKey(wID)) {
			if (LFSConstants.GetFMCStationList().get(wID) != null) {
				wResult = LFSConstants.GetFMCStationList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 物料全局数据
	private static Calendar RefreshMaterialTime = Calendar.getInstance();

	private static Map<Integer, MSSMaterial> MSSMaterialDic = new HashMap<Integer, MSSMaterial>();

	public static synchronized Map<Integer, MSSMaterial> GetMSSMaterialList() {
		if (MSSMaterialDic == null || MSSMaterialDic.size() <= 0
				|| RefreshMaterialTime.compareTo(Calendar.getInstance()) < 0) {
			List<MSSMaterial> wMSSMaterialList = WMSServiceImpl.getInstance().MSS_QueryMaterialList(BaseDAO.SysAdmin)
					.List(MSSMaterial.class);
			if (wMSSMaterialList != null && wMSSMaterialList.size() > 0) {
				MSSMaterialDic = wMSSMaterialList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshMaterialTime = Calendar.getInstance();
			RefreshMaterialTime.add(Calendar.MINUTE, 3);
		}
		return MSSMaterialDic;
	}

	public static MSSMaterial GetMSSMaterialByNo(String wMaterialNo) {
		MSSMaterial wResult = new MSSMaterial();
		try {
			Optional<MSSMaterial> wMSSMaterialOptional = LFSConstants.GetMSSMaterialList().values().stream()
					.filter(p -> p.MaterialNo.equals(wMaterialNo)).findAny();
			if (wMSSMaterialOptional.isPresent()) {
				wResult = wMSSMaterialOptional.get();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}

		return wResult;
	}

	public static String GetMSSMaterialName(int wID) {
		String wResult = "";
		if (LFSConstants.GetMSSMaterialList().containsKey(wID)) {
			if (LFSConstants.GetMSSMaterialList().get(wID) != null) {
				wResult = LFSConstants.GetMSSMaterialList().get(wID).getMaterialName();
			}
		}
		return wResult;
	}

	public static MSSMaterial GetMSSMaterial(int wID) {
		MSSMaterial wResult = new MSSMaterial();
		if (LFSConstants.GetMSSMaterialList().containsKey(wID)) {
			if (LFSConstants.GetMSSMaterialList().get(wID) != null) {
				wResult = LFSConstants.GetMSSMaterialList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 工厂全局数据
	private static Calendar RefreshFactoryTime = Calendar.getInstance();

	private static Map<Integer, FMCFactory> FMCFactoryDic = new HashMap<Integer, FMCFactory>();

	public static synchronized Map<Integer, FMCFactory> GetFMCFactoryList() {
		if (FMCFactoryDic == null || FMCFactoryDic.size() <= 0
				|| RefreshFactoryTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCFactory> wFMCFactoryList = FMCServiceImpl.getInstance().FMC_QueryFactoryList(BaseDAO.SysAdmin)
					.List(FMCFactory.class);
			if (wFMCFactoryList != null && wFMCFactoryList.size() > 0) {
				FMCFactoryDic = wFMCFactoryList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshFactoryTime = Calendar.getInstance();
			RefreshFactoryTime.add(Calendar.MINUTE, 3);
		}
		return FMCFactoryDic;
	}

	public static String GetFMCFactoryName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCFactoryList().containsKey(wID)) {
			if (LFSConstants.GetFMCFactoryList().get(wID) != null) {
				wResult = LFSConstants.GetFMCFactoryList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCFactory GetFMCFactory(int wID) {
		FMCFactory wResult = new FMCFactory();
		if (LFSConstants.GetFMCFactoryList().containsKey(wID)) {
			if (LFSConstants.GetFMCFactoryList().get(wID) != null) {
				wResult = LFSConstants.GetFMCFactoryList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 事业部全局数据
	private static Calendar RefreshBusinessUnitTime = Calendar.getInstance();

	private static Map<Integer, FMCBusinessUnit> FMCBusinessUnitDic = new HashMap<Integer, FMCBusinessUnit>();

	public static synchronized Map<Integer, FMCBusinessUnit> GetFMCBusinessUnitList() {
		if (FMCBusinessUnitDic == null || FMCBusinessUnitDic.size() <= 0
				|| RefreshBusinessUnitTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCBusinessUnit> wFMCBusinessUnitList = FMCServiceImpl.getInstance()
					.FMC_QueryBusinessUnitList(BaseDAO.SysAdmin).List(FMCBusinessUnit.class);
			if (wFMCBusinessUnitList != null && wFMCBusinessUnitList.size() > 0) {
				FMCBusinessUnitDic = wFMCBusinessUnitList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshBusinessUnitTime = Calendar.getInstance();
			RefreshBusinessUnitTime.add(Calendar.MINUTE, 3);
		}
		return FMCBusinessUnitDic;
	}

	public static String GetFMCBusinessUnitName(int wID) {
		String wResult = "";
		if (LFSConstants.GetFMCBusinessUnitList().containsKey(wID)) {
			if (LFSConstants.GetFMCBusinessUnitList().get(wID) != null) {
				wResult = LFSConstants.GetFMCBusinessUnitList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCBusinessUnit GetFMCBusinessUnit(int wID) {
		FMCBusinessUnit wResult = new FMCBusinessUnit();
		if (LFSConstants.GetFMCBusinessUnitList().containsKey(wID)) {
			if (LFSConstants.GetFMCBusinessUnitList().get(wID) != null) {
				wResult = LFSConstants.GetFMCBusinessUnitList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 客户全局数据
	private static Calendar RefreshCustomerTime = Calendar.getInstance();

	private static Map<Integer, CRMCustomer> CRMCustomerDic = new HashMap<Integer, CRMCustomer>();

	public static synchronized Map<Integer, CRMCustomer> GetCRMCustomerList() {
		if (CRMCustomerDic == null || CRMCustomerDic.size() <= 0
				|| RefreshCustomerTime.compareTo(Calendar.getInstance()) < 0) {
			List<CRMCustomer> wCRMCustomerList = SCMServiceImpl.getInstance()
					.CRM_QueryCustomerList(BaseDAO.SysAdmin, "", 0, 0, 0, 1).List(CRMCustomer.class);
			if (wCRMCustomerList != null && wCRMCustomerList.size() > 0) {
				CRMCustomerDic = wCRMCustomerList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshCustomerTime = Calendar.getInstance();
			RefreshCustomerTime.add(Calendar.MINUTE, 3);
		}
		return CRMCustomerDic;
	}

	public static String GetCRMCustomerName(int wID) {
		String wResult = "";
		if (LFSConstants.GetCRMCustomerList().containsKey(wID)) {
			if (LFSConstants.GetCRMCustomerList().get(wID) != null) {
				wResult = LFSConstants.GetCRMCustomerList().get(wID).getCustomerName();
			}
		}
		return wResult;
	}

	public static CRMCustomer GetCRMCustomer(int wID) {
		CRMCustomer wResult = new CRMCustomer();
		if (LFSConstants.GetCRMCustomerList().containsKey(wID)) {
			if (LFSConstants.GetCRMCustomerList().get(wID) != null) {
				wResult = LFSConstants.GetCRMCustomerList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion
}
