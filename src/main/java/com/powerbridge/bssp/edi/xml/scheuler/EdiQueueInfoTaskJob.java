package com.powerbridge.bssp.edi.xml.scheuler;

import com.powerbridge.bssp.edi.xml.prod.EdiSendTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 *
 * @ClassName EdiSendQueueTaskJob
 * @Description 单据队列任务调度器
 * @author Simon.xie
 * @Date 2017年5月23日 上午11:31:34
 * @version 1.0.0
 */
@Component
@Configurable
@EnableScheduling
public class EdiQueueInfoTaskJob {

	static final Log logger = LogFactory.getLog(EdiQueueInfoTaskJob.class);

	@Autowired
	private EdiSendTask ediSendTask;

	@Scheduled(cron="0 */1 * * * ?")
	@Autowired
	public void execute() {
		logger.info("-----------------报文组装任务调度器EdiSendQueueTaskJob任务启动----------");
		try {
		//EdiSendTask ediSendTask  = new EdiSendTask();
		ediSendTask.job();
		logger.info("-----------------报文组装任务调度器EdiSendQueueTaskJob任务结束----------");
		} catch (Exception e) {
			System.out.println("报文组装处理异常-----------------------------" + e.getMessage());
			e.printStackTrace();
		}
	}
}
