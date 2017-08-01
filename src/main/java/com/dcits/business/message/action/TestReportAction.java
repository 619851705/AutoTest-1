package com.dcits.business.message.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.service.TestReportService;
import com.dcits.business.message.service.TestSetService;
import com.dcits.constant.ReturnCodeConsts;

/**
 * 接口自动化<br>
 * 测试报告和测试结果Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.07.10
 *
 */


@Controller
@Scope("prototype")
public class TestReportAction extends BaseAction<TestReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TestReportService testReportService;
	@Autowired
	private TestSetService testSetService;
	
	@Autowired
	public void setTestReportService(TestReportService testReportService) {
		super.setBaseService(testReportService);
		this.testReportService = testReportService;
	}

	@Override
	public String get() {
		// TODO Auto-generated method stub
		model = testReportService.get(model.getReportId());
		model.setSceneNum();
		
		jsonMap.put("report", model);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object processListData(Object o) {
		// TODO Auto-generated method stub
		List<TestReport> reports = (List<TestReport>) o;
		
		for (TestReport r:reports) {
			r.setSceneNum();
			r.setSetName("全量测试");
			
			if (!"0".equals(r.getTestMode())) {
				TestSet set = testSetService.get(Integer.valueOf(r.getTestMode()));
				
				if (set != null) {
					r.setSetName(set.getSetName());
				} else {
					r.setSetName("测试集已删除");
				}
			}
		}		
		return reports;
	}
	
	/**
	 * 获取生成完整测试报告所需数据
	 * @return
	 */
	public String getReportDetail() {
		TestReport report = testReportService.get(model.getReportId());
		report.setSceneNum();
		
		TestSet set = testSetService.get(Integer.valueOf(report.getTestMode()));
		String title = "神州数码接口自动化测试报告";
		
		jsonMap.put("title", "全量测试  " + report.getStartTime() + " - " + title);
		
		if (!"0".equals(report.getTestMode())) {
			if (set == null) {
				jsonMap.put("title", "接口测试  " + report.getStartTime() + " - " + title);
			} else {
				jsonMap.put("title", set.getSetName() + " - " + title);
			}
		}
		
		Map<String, Object> desc = new HashMap<>();
		
		desc.put("sceneNum", report.getSceneNum());
		desc.put("testDate", report.getStartTime());
		desc.put("successNum", report.getSuccessNum());
		desc.put("failNum", report.getFailNum());
		desc.put("stopNum", report.getStopNum());
		desc.put("successRate",  String.format("%.2f", Double.valueOf(String.valueOf((report.getSuccessNum()/report.getSceneNum()*100)))));
		
		jsonMap.put("desc", desc);
		jsonMap.put("data", report.getTrs());		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
}
