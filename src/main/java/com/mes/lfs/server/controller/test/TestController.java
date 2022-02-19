package com.mes.lfs.server.controller.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.lfs.server.controller.BaseController;
import com.mes.lfs.server.service.po.ServiceResult;
import com.mes.lfs.server.service.po.bms.BMSEmployee;
import com.mes.lfs.server.service.po.lfs.LFSBureau;
import com.mes.lfs.server.service.po.lfs.LFSManufacturer;
import com.mes.lfs.server.service.po.lfs.LFSSection;
import com.mes.lfs.server.service.utils.StringUtils;
import com.mes.lfs.server.service.utils.XmlTool;
import com.mes.lfs.server.serviceimpl.dao.BaseDAO;
import com.mes.lfs.server.utils.Constants;
import com.mes.lfs.server.utils.QRCodeUtil;
import com.mes.lfs.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-4-2 16:57:38
 * @LastEditTime 2020-4-2 16:57:41
 */
@RestController
@RequestMapping("/api/Test")
public class TestController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	/**
	 * 接口测试
	 */
	@GetMapping("/Test")
	public Object Test(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			testAndon(BaseDAO.SysAdmin);

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();

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

	private void testAndon(BMSEmployee wLoginUser) {
		try {
			// 存放在二维码中的内容
			String text = "kaifa,012100090015,2021-12-17 09:21:17";
			// 嵌入二维码的图片路径
			String imgPath = "";
			// 生成的二维码的路径及名称
			String wDirePath = StringUtils.Format("{0}static/export/{1}.jpg",
					Constants.getConfigPath().replace("config/", ""), UUID.randomUUID().toString().replaceAll("-", ""));
			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}
			// 生成二维码
			QRCodeUtil.encode(text, imgPath, wDirePath, true);
			// 解析二维码
			String str = QRCodeUtil.decode(wDirePath);
			// 打印出解析出的内容
			System.out.println(str);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@SuppressWarnings("unused")
	private void WriteCS() {
		try {
			List<LFSManufacturer> wList = new ArrayList<LFSManufacturer>();

			LFSManufacturer wItem = new LFSManufacturer();
			wItem.ID = 1;
			wItem.Name = "大连机车车辆有限公司";
			wList.add(wItem);

			wItem = new LFSManufacturer();
			wItem.ID = 2;
			wItem.Name = "株洲电力机车有限公司";
			wList.add(wItem);

			XmlTool.SaveXml("C:\\Users\\Shris\\Desktop\\LFSManufacturer.xml", wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@SuppressWarnings("unused")
	private void WriteSection() {
		try {
			List<LFSSection> wList = new ArrayList<LFSSection>();

			LFSSection wItem = new LFSSection();
			wItem.BureauID = 1;
			wItem.Name = "北京";
			wList.add(wItem);

//			wItem = new LFSSection();
//			wItem.BureauID = 2;
//			wItem.Name = "延安";
//			wList.add(wItem);
//
//			wItem = new LFSSection();
//			wItem.BureauID = 2;
//			wItem.Name = "新丰镇";
//			wList.add(wItem);

//			wItem = new LFSSection();
//			wItem.BureauID = 3;
//			wItem.Name = "广州";
//			wList.add(wItem);
//
//			wItem = new LFSSection();
//			wItem.BureauID = 3;
//			wItem.Name = "长沙";
//			wList.add(wItem);
//
//			wItem = new LFSSection();
//			wItem.BureauID = 3;
//			wItem.Name = "株洲";
//			wList.add(wItem);
//
//			wItem = new LFSSection();
//			wItem.BureauID = 3;
//			wItem.Name = "怀化";
//			wList.add(wItem);
//
//			wItem = new LFSSection();
//			wItem.BureauID = 3;
//			wItem.Name = "龙川";
//			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 4;
			wItem.Name = "南昌";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 4;
			wItem.Name = "向塘";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 4;
			wItem.Name = "鹰潭";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 4;
			wItem.Name = "福州";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 5;
			wItem.Name = "南宁";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 5;
			wItem.Name = "柳州";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 6;
			wItem.Name = "昆明";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 7;
			wItem.Name = "武汉";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 7;
			wItem.Name = "江岸";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 7;
			wItem.Name = "武昌南";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 8;
			wItem.Name = "成都";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 8;
			wItem.Name = "重庆";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 9;
			wItem.Name = "郑州";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 9;
			wItem.Name = "新乡";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 10;
			wItem.Name = "合肥";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 10;
			wItem.Name = "上海";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 10;
			wItem.Name = "杭州";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 11;
			wItem.Name = "兰州西";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 11;
			wItem.Name = "嘉峪关";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 12;
			wItem.Name = "西宁";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 13;
			wItem.Name = "呼和浩特";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 13;
			wItem.Name = "集宁";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 13;
			wItem.Name = "包头";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 14;
			wItem.Name = "乌鲁木齐";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 14;
			wItem.Name = "哈密";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 15;
			wItem.Name = "青岛";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 16;
			wItem.Name = "太原段";
			wList.add(wItem);

			wItem = new LFSSection();
			wItem.BureauID = 17;
			wItem.Name = "南宁南";
			wList.add(wItem);

			int wIndex = 1;
			for (LFSSection lfsSection : wList) {
				lfsSection.ID = wIndex++;
			}

			XmlTool.SaveXml("C:\\Users\\Shris\\Desktop\\LFSSection.xml", wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@SuppressWarnings("unused")
	private void WriteXml() {
		try {
			Map<String, String> wMap = new HashMap<String, String>();

			wMap.put("北京局", "北京");
			wMap.put("西安局", "延安,新丰镇");
			wMap.put("广州局", "广州,长沙,株洲,怀化,龙川");
			wMap.put("南昌局", "南昌,向塘,鹰潭,福州");
			wMap.put("南宁局", "南宁,柳州");
			wMap.put("昆明局", "昆明");
			wMap.put("武汉局", "武汉,江岸,武昌南,襄阳");
			wMap.put("成都局", "成都,重庆");
			wMap.put("郑州局", "郑州,新乡");
			wMap.put("上海局", "合肥,上海,杭州");
			wMap.put("兰州局", "兰州西,嘉峪关,");
			wMap.put("青藏局", "西宁,");
			wMap.put("呼和浩特局", "呼和浩特,集宁,包头");
			wMap.put("乌鲁木齐局", "乌鲁木齐,哈密");
			wMap.put("济南局", "青岛");
			wMap.put("大秦铁路", "太原段");
			wMap.put("沿海公司", "南宁南");

			List<LFSBureau> wList = new ArrayList<LFSBureau>();

			LFSBureau wItem = new LFSBureau();
			wItem.ID = 1;
			wItem.Name = "北京局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 2;
			wItem.Name = "西安局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 3;
			wItem.Name = "广州局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 4;
			wItem.Name = "南昌局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 5;
			wItem.Name = "南宁局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 6;
			wItem.Name = "昆明局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 7;
			wItem.Name = "武汉局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 8;
			wItem.Name = "成都局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 9;
			wItem.Name = "郑州局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 10;
			wItem.Name = "上海局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 11;
			wItem.Name = "兰州局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 12;
			wItem.Name = "青藏局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 13;
			wItem.Name = "呼和浩特局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 14;
			wItem.Name = "乌鲁木齐局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 15;
			wItem.Name = "济南局";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 16;
			wItem.Name = "大秦铁路";
			wList.add(wItem);

			wItem = new LFSBureau();
			wItem.ID = 17;
			wItem.Name = "沿海公司";
			wList.add(wItem);

			XmlTool.SaveXml("C:\\Users\\Shris\\Desktop\\LFSBureau.xml", wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
