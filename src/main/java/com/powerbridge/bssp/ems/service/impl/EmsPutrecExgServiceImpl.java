package com.powerbridge.bssp.ems.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.common.util.UUIDGenerator;
import com.powerbridge.bssp.ems.dao.EmsPutrecExgMapper;
import com.powerbridge.bssp.ems.entity.EmsPutrecExg;
import com.powerbridge.bssp.ems.service.IEmsPutrecExgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：EmsPutrecExgServiceImpl
 * 类描述：账册备案申请成品服务实现
 * 创建人：willChen
 * 创建时间：2017/5/17 9:23
 * 修改人：jokylao
 * 修改时间：2017/6/1 22:18
 */
@Service("emsPutrecExgService")
public class EmsPutrecExgServiceImpl extends BaseServiceImpl<EmsPutrecExgMapper, EmsPutrecExg> implements IEmsPutrecExgService {
    @Autowired
    private EmsPutrecExgMapper emsPutrecExgMapperMapper;
    /**
     * 查询加工账册备案成品/分页
     *
     * @param page 分页
     * @param emsPutrecExg
     * @return Page<SasVehicleBsc>
     */
    public Page<EmsPutrecExg> selectEmsPutrecExgList(Page<EmsPutrecExg> page, EmsPutrecExg emsPutrecExg){
        page.setRecords(emsPutrecExgMapperMapper.selectEmsPutrecExgList(page, emsPutrecExg));
        return page;
    }
    /**
     * 获取最大的商品序号
     *
     * @return 单据编号
     */
    @Override
    public BigDecimal getMaxGdsSeqno(String seqNo) {
        BigDecimal maxGdsSeqno = emsPutrecExgMapperMapper.getMaxGdsSeqno(seqNo);
        if (null == maxGdsSeqno) {
            maxGdsSeqno = BigDecimal.valueOf(0);
        }
        return maxGdsSeqno;
    }
    /**
     * 复制当前商品
     * @param emsPutrecExg
     * @return
     */
    @Override
    public EmsPutrecExg copyGds(EmsPutrecExg emsPutrecExg){
        EmsPutrecExg newEmsPutrecImg=new EmsPutrecExg();
        BeanUtils.copyProperties(emsPutrecExg,newEmsPutrecImg);
        String uId = UUIDGenerator.getUUID();
        newEmsPutrecImg.setUid(uId);//主键
        newEmsPutrecImg.setGdsMtno(null);//料号不复制
        BigDecimal gdsSeqno=getMaxGdsSeqno(emsPutrecExg.getSeqNo());
        gdsSeqno=gdsSeqno.add(new BigDecimal(1));//取最大值加1
        newEmsPutrecImg.setGdsSeqno(gdsSeqno);
        return  newEmsPutrecImg;
    }
    /**
     * 判断成品序号是否存在
     * @param seqNo
     * @param gdsSeqno
     * @param uid
     * @return
     */
    @Override
    public  Boolean isExistGdsSeqno(String seqNo,BigDecimal gdsSeqno,String uid){
        Boolean flag=false;
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("SEQ_NO", seqNo);
        entityWrapper.eq("GDS_SEQNO", gdsSeqno);
        entityWrapper.ne("uid",uid);
        List<EmsPutrecExg> emsPutrecExgList = selectList(entityWrapper);
        if (emsPutrecExgList.size() > 0) {
            flag=true;
        }
        return flag;
    }
}
