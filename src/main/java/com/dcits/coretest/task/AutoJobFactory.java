package com.dcits.coretest.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class AutoJobFactory extends AdaptableJobFactory implements ApplicationContextAware {

	ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext; 
		
	}
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		// 调用父类的方法
		Object jobInstance = super.createJobInstance(bundle);

		AutowireCapableBeanFactory aaa = applicationContext.getAutowireCapableBeanFactory();
		aaa.autowireBeanProperties(jobInstance, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		return jobInstance;
	} 
	
}
