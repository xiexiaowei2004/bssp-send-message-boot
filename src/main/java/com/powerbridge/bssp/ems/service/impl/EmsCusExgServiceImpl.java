package com.powerbridge.bssp.ems.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.common.util.UUIDGenerator;
import com.powerbridge.bssp.ems.dao.EmsCusExgMapper;
import com.powerbridge.bssp.ems.entity.EmsCusExg;
import com.powerbridge.bssp.ems.service.IEmsCusExgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：EmsCusExgServiceImpl
 * 类描述：账册备案正式表成品服务实现
 * 创建人：willChen
 * 创建时间：2017/5/17 9:23
 * 修改人：jokylao
 * 修改时间：2017/6/4 11:23
 */
@Service("emsCusExgService")
public class EmsCusExgServiceImpl extends BaseServiceImpl<EmsCusExgMapper, EmsCusExg> implements IEmsCusExgService {

    @Autowired
    EmsCusExgMapper emsCusExgMapper;
    /**
     * 查询加工账册备案成品/分页
     *
     * @param page 分页
     * @param emsCusExg
     * @return Page<EmsCusExg>
     */
    @Override
    public Page<EmsCusExg> selectEmsCusExgList(Page<EmsCusExg> page, EmsCusExg emsCusExg){
        page.setRecords(emsCusExgMapper.selectEmsCusExgList(page, emsCusExg));
        return page;
    }
    /**
     * 获取最大的商品序号
     *
     * @return 账册编号
     */
    @Override
    public BigDecimal getMaxGdsSeqno(String emsNo) {
        BigDecimal maxGdsSeqno = emsCusExgMapper.getMaxGdsSeqno(emsNo);
        if (null == maxGdsSeqno) {
            maxGdsSeqno = BigDecimal.valueOf(0);
        }
        return maxGdsSeqno;
    }

    /**
     * 复制账册成品信息
     *
     * @param emsCusExgList   成品id
     * @return
     */
    @Override
    public List<EmsCusExg> copyGds(List<EmsCusExg> emsCusExgList) {
        List<EmsCusExg> newEmsCusExgList = new ArrayList<>();
        for (EmsCusExg emsCusExg:emsCusExgList) {
            String uId = UUIDGenerator.getUUID();
            emsCusExg.setUid(uId);
            BigDecimal gdsSeqno=getMaxGdsSeqno(emsCusExg.getSeqNo());
            gdsSeqno=gdsSeqno.add(new BigDecimal(1));//取最大值加1
            emsCusExg.setGdsSeqno(gdsSeqno);
            emsCusExg.setModfMarkcd("1");
            newEmsCusExgList.add(emsCusExg);
        }
        return newEmsCusExgList;
    }
    /**
     * 判断成品序号是否存在
     * @param emsNo
     * @param emsNo
     * @return
     */
    @Override
    public  Boolean isExistGdsSeqno(String emsNo,BigDecimal gdsSeqno){
        Boolean flag=false;
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("EMS_NO", emsNo);
        entityWrapper.eq("GDS_SEQNO", gdsSeqno);
        List<EmsCusExg> emsCusExgList = selectList(entityWrapper);
        if (emsCusExgList.size() > 0) {
            flag=true;
        }
        return flag;
    }
}
