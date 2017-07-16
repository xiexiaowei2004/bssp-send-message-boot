package com.powerbridge.bssp.ems.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.ems.entity.EmsCusUcnsDt;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository("emsCusUcnsDtMapper")
public interface EmsCusUcnsDtMapper extends BaseMapper<EmsCusUcnsDt> {
    /**
     * 查询账册备案正式表单损耗列表
     * @param page
     * @param emsCusUcnsDt
     * @return EmsCusUcnsDt
     */
    List<EmsCusUcnsDt> selectEmsCusUcnsDtList(Page<EmsCusUcnsDt> page, EmsCusUcnsDt emsCusUcnsDt);

    /**
     * 根据账册编号，成品序号 查询单耗版本号
     * @param emsCusUcnsDt
     * @return
     */
    List<EmsCusUcnsDt> selectUcnsVerno(EmsCusUcnsDt emsCusUcnsDt);
    /**
     * 获取最大的序号
     *
     * @return 账册编号
     */
    BigDecimal getMaxUcnsSeqno(String emsNo);
}