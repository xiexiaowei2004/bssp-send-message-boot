package com.powerbridge.bssp.ems.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.powerbridge.bssp.ems.entity.EmsPutrecExg;

import java.math.BigDecimal;

/**
 * 项目名称：bssp-receipt
 * 类名称：IEmsPutrecExgService
 * 类描述：账册备案申请成品服务
 * 创建人：willChen
 * 创建时间：2017/5/16 22:00
 * 修改人：willChen
 * 修改时间：2017/5/16 22:00
 */
public interface IEmsPutrecExgService extends IService<EmsPutrecExg> {
    /**
     * 查询加工账册备案成品/分页
     *
     * @param page 分页
     * @param emsPutrecExg
     * @return List<EmsPutrecExg>
     */
    Page<EmsPutrecExg> selectEmsPutrecExgList(Page<EmsPutrecExg> page, EmsPutrecExg emsPutrecExg);
    /**
     * 获取最大的商品序号
     *
     * @return 单据编号
     */
    BigDecimal getMaxGdsSeqno(String seqNo);
    /**
     * 复制商品
     * @param emsPutrecExg
     * @return
     */
    EmsPutrecExg copyGds(EmsPutrecExg emsPutrecExg);
    /**
     * 判断料件序号是否存在
     * @param seqNo
     * @param gdsSeqno
     * @param uid
     * @return Boolean
     */
    Boolean isExistGdsSeqno(String seqNo,BigDecimal gdsSeqno,String uid);
}
