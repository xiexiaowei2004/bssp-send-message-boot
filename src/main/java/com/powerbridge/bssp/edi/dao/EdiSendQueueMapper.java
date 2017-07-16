package com.powerbridge.bssp.edi.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.powerbridge.bssp.edi.entity.EdiSendQueue;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 单据队列表 Mapper 接口
 * </p>
 *
 * @author simon.xie
 * @since 2017-05-20
 */
@Repository
public interface EdiSendQueueMapper extends BaseMapper<EdiSendQueue> {
	/**
	 * 
	 * @Description 批量插入数据
	 * @param ediSendQueueList
	 * @return
	 */
	int insertCirclationInfoBatch(List<EdiSendQueue> ediSendQueueList);
}