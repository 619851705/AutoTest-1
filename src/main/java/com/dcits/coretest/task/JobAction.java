package com.dcits.coretest.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.business.system.bean.AutoTask;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.service.UserService;
import com.dcits.constant.SystemConsts;
import com.dcits.coretest.message.test.MessageAutoTest;

public class JobAction implements Job {
	
	@Autowired
	private MessageAutoTest messageAutoTest;
	@Autowired
	private UserService userSerivce;
	

	@Override
	public void execute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		AutoTask task = (AutoTask)dataMap.get(SystemConsts.QUARTZ_TASK_NAME_PREFIX_KEY + context.getJobDetail().getKey().getGroup());
		User user = userSerivce.get(SystemConsts.ADMIN_USER_ID);
		
		int[] result = null;
		//判断任务类型
		if ("0".equals(task.getTaskType())) {
			//执行接口自动化
			result = messageAutoTest.batchTest(user, task.getRelatedId(), true);
		}
		
		if ("1".equals(task.getTaskType())) {
			//web自动化
		}
		
		context.setResult(result);				
	}

}
