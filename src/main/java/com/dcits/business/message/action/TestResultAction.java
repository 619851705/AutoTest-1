package com.dcits.business.message.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.service.TestResultService;

/**
 * 接口自动化<br>
 * 测试结果详情Action
 * @author Administrator
 *
 */

@Controller
@Scope("prototype")
public class TestResultAction extends BaseAction<TestResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private TestResultService testResultService;
	
	private Integer reportId;
	
	@Autowired
	public void setTestResultService(TestResultService testResultService) {
		super.setBaseService(testResultService);
		this.testResultService = testResultService;
	}
		
	@Override
	public String[] prepareList() {
		// TODO Auto-generated method stub
		if (reportId != null && model.getRunStatus() != null) {
			if ("all".equalsIgnoreCase(model.getRunStatus())) {
				this.filterCondition = new String[]{"testReport.reportId=" + reportId};
			} else {
				this.filterCondition = new String[]{"testReport.reportId=" + reportId, "runStatus='" + model.getRunStatus() + "'"};
			}			
		}
		return this.filterCondition;
	}

	


	/*****************************************************************************/
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	

}
