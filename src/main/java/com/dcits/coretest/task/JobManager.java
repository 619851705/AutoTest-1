package com.dcits.coretest.task;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dcits.business.system.bean.AutoTask;
import com.dcits.business.system.service.AutoTaskService;
import com.dcits.constant.SystemConsts;

public class JobManager {
	
	private static final Logger LOGGER = Logger.getLogger(JobManager.class);
	
	@Resource  
    private Scheduler scheduler ;
	@Resource
	private TaskJobListener jobListener;
	@Resource
	private AutoTaskService taskService;
      
    public Scheduler getScheduler() {  
        return scheduler;  
    }  
  
    public void setScheduler(Scheduler scheduler) {  
        this.scheduler = scheduler;  
    }
    
    /**
     * 添加一个定时任务
     * @param task   任务详情
     * @param classz 执行任务的详细job
     * @throws SchedulerException 
     */
	public boolean addTask(AutoTask task) {
    	boolean flag = false;
    	//获取任务信息数据
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	//表达式调度构建器  
    	CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCronExpression()); 
    	try {
    		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        	//判断添加的任务触发器是否已经存在？
        	if(trigger == null){
        		//不存在,创建一个
        		 JobDetail jobDetail = JobBuilder.newJob(JobAction.class).withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).build();
        		 jobDetail.getJobDataMap().put(SystemConsts.QUARTZ_TASK_NAME_PREFIX_KEY + task.getTaskId(), task);
       		                 
                 //按新的cronExpression表达式构建一个新的trigger  
                 trigger = TriggerBuilder.newTrigger().withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).withSchedule(scheduleBuilder).build();  
                 
                 scheduler.scheduleJob(jobDetail, trigger);                 
                 
        	} else {
        		//存在就更新表达式
        		//表达式调度构建器  
                //按新的cronExpression表达式重新构建trigger  
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();  
                //按新的trigger重新设置job执行  
                scheduler.rescheduleJob(triggerKey, trigger);
        	}
        	flag = true;
        	
        	//更新task状态
        	task.setStatus("0");
        	task.setLastFinishTime(null);
        	task.setRunCount(0);
        	taskService.edit(task);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("创建quartz定时任务失败:\n[任务名称]-" + task.getTaskName() + ",[任务ID]-" + task.getTaskId(), e);
		}
    	return flag;   
    }
    
    /**
     * 停止任务
     * @param task
     * @throws SchedulerException 
     */
    public boolean stopTask(AutoTask task) {
    	boolean flag = false;   	
    	//获取任务信息数据
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	
    	try {
    		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        	//任务存在就删除(停止)
        	if(trigger!=null){
        		scheduler.unscheduleJob(triggerKey);
        	}
        	flag = true;
        	//更新task状态
        	taskService.updateStatus(task.getTaskId(), "1");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("停止quartz定时任务失败:\n[任务名称]-" + task.getTaskName() + ",[任务ID]-" + task.getTaskId(), e);
		}
    	
    	return flag;
    }
    
    
    /** 
     * 启动所有定时任务 
     * 
     */  
    public void startTasks() {  
        try {     
        	WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
            ServletContext servletContext = webApplicationContext.getServletContext(); 
            if (SystemConsts.QUARTZ_SCHEDULER_IS_STOP.equals(servletContext.getAttribute(SystemConsts.QUARTZ_SCHEDULER_START_FLAG))) {
            	LOGGER.info("恢复所有定时任务...");
            	scheduler.resumeAll();
            	return;
            }
            
            LOGGER.info("启动Quartz定时任务管理器...");
            scheduler.start();  
            scheduler.getListenerManager().addJobListener(jobListener);
            LOGGER.info("启动Quartz可运行定时任务...");
            //启动当前所有可运行的定时任务
            List<AutoTask> tasks = taskService.findRunTasks();
            LOGGER.info("当前可运行定时任务为" + tasks.size());
            for (AutoTask t:tasks) {
            	addTask(t);
            }
                      
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**
     * 停止所有任务
     */
    public void stopTasks(){
    	try {
    		//shutdown之后无法重启,所以使用pauseAll通过暂停
			//scheduler.shutdown();
    		LOGGER.info("暂停所有定时任务...");
			scheduler.pauseAll();			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
}
