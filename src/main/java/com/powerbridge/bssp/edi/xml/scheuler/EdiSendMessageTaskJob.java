package com.powerbridge.bssp.edi.xml.scheuler;

import com.powerbridge.bssp.edi.xml.prod.EdiAssembleTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @ClassName EdiSendMessageTaskJob
 * @Description 发送报文任务器
 * @author Simon.xie
 * @Date 2017年5月23日 上午11:31:34
 * @version 1.0.0
 */
@Component
public class EdiSendMessageTaskJob{
    @Autowired
    private EdiAssembleTask ediAssembleTask;

    static final Log logger = LogFactory.getLog(EdiSendMessageTaskJob.class);
    @Scheduled(cron="0 */2 * * * ?")
    @Autowired
    public void execute() {
        try {
            logger.info("-----------------发送报文任务器EdiSendMessageTaskJob任务启动----------");
            //EdiAssembleTask ediAssembleTask = new EdiAssembleTask();
            ediAssembleTask.job();
            logger.info("-----------------发送报文任务器EdiSendMessageTaskJob任务结束----------");
        } catch (Exception e) {
            System.out.println("发送报文处理异常-----------------------------" + e.getMessage());
            e.printStackTrace();
        }

    }
}
