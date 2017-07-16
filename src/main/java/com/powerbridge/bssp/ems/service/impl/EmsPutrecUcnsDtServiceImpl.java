package com.powerbridge.bssp.ems.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.ems.dao.EmsPutrecUcnsDtMapper;
import com.powerbridge.bssp.ems.entity.EmsPutrecUcnsDt;
import com.powerbridge.bssp.ems.service.IEmsPutrecUcnsDtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：EmsPutrecUcnsDtServiceImpl
 * 类描述：账册备案申请损耗服务实现
 * 创建人：willChen
 * 创建时间：2017/5/17 9:23
 * 修改人：jokylao
 * 修改时间：2017/6/1 22:18
 */
@Service("emsPutrecUcnsDtService")
public class EmsPutrecUcnsDtServiceImpl extends BaseServiceImpl<EmsPutrecUcnsDtMapper, EmsPutrecUcnsDt> implements IEmsPutrecUcnsDtService {
    @Autowired
    private EmsPutrecUcnsDtMapper emsPutrecUcnsDtMapper;
    /**
     * 查询加工账册备案单损耗/分页
     *
     * @param page 分页
     * @param emsPutrecUcnsDt
     * @return Page<SasVehicleBsc>
     */
    public Page<EmsPutrecUcnsDt> selectEmsPutrecUcnsDtList(Page<EmsPutrecUcnsDt> page, EmsPutrecUcnsDt emsPutrecUcnsDt){
        page.setRecords(emsPutrecUcnsDtMapper.selectEmsPutrecUcnsDtList(page, emsPutrecUcnsDt));
        return page;
    }
    /**
     * 判断序号是否存在
     * @param seqNo
     * @param ucnsSeqno
     * @param uid
     * @return
     */
    @Override
    public  Boolean isExistGdsSeqno(String seqNo, BigDecimal ucnsSeqno, String uid){
        Boolean flag=false;
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("SEQ_NO", seqNo);
        entityWrapper.eq("UCNS_SEQNO", ucnsSeqno);
        entityWrapper.ne("uid",uid);
        List<EmsPutrecUcnsDt> emsPutrecUcnsDtList = selectList(entityWrapper);
        if (emsPutrecUcnsDtList.size() > 0) {
            flag=true;
        }
        return flag;
    }
    /**
     * 获取最大的商品序号
     *
     * @return 单据编号
     */
    @Override
    public BigDecimal getMaxUcnsSeqno(String seqNo) {
        BigDecimal maxGdsSeqno = emsPutrecUcnsDtMapper.getMaxUcnsSeqno(seqNo);
        if (null == maxGdsSeqno) {
            maxGdsSeqno = BigDecimal.valueOf(0);
        }
        return maxGdsSeqno;
    }
}
