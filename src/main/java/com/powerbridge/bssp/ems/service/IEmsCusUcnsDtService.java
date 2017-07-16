package com.powerbridge.bssp.ems.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.powerbridge.bssp.ems.entity.EmsCusUcnsDt;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：IEmsCusUcnsDtService
 * 类描述：账册备案正式表损耗服务
 * 创建人：willChen
 * 创建时间：2017/5/16 22:00
 * 修改人：jokylao
 * 修改时间：2017/6/5 12:00
 */
public interface IEmsCusUcnsDtService extends IService<EmsCusUcnsDt> {
    /**
     * 查询账册备案正式表单损耗列表
     * @param page
     * @param emsCusUcnsDt
     * @return EmsCusUcnsDt
     */
    Page<EmsCusUcnsDt> selectEmsCusUcnsDtList(Page<EmsCusUcnsDt> page, EmsCusUcnsDt emsCusUcnsDt);

    /**
     *查询单耗版本号
     * @param emsCusUcnsDt
     * @return
     */
    List<EmsCusUcnsDt> selectUcnsVerno(EmsCusUcnsDt emsCusUcnsDt);
    /**
     * 判断序号是否存在
     * @param emsNo
     * @param ucnsSeqno
     * @return
     */
    Boolean isExistGdsSeqno(String emsNo,BigDecimal ucnsSeqno);
    /**
     * 获取最大的序号
     *
     * @return 账册编号
     */
    BigDecimal getMaxUcnsSeqno(String emsNo);
}
