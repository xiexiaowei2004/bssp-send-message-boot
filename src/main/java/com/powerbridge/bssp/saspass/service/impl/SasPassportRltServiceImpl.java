package com.powerbridge.bssp.saspass.service.impl;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.common.util.UUIDGenerator;
import com.powerbridge.bssp.common.util.toolbox.StringUtil;
import com.powerbridge.bssp.saspass.dao.SasPassportBscMapper;
import com.powerbridge.bssp.saspass.dao.SasPassportDtMapper;
import com.powerbridge.bssp.saspass.entity.SasPassportBsc;
import com.powerbridge.bssp.saspass.entity.SasPassportDt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.saspass.dao.SasPassportRltMapper;
import com.powerbridge.bssp.saspass.entity.SasPassportRlt;
import com.powerbridge.bssp.saspass.service.ISasPassportRltService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("sasPassportRltService")
@Transactional(readOnly = true)
public class SasPassportRltServiceImpl extends BaseServiceImpl<SasPassportRltMapper, SasPassportRlt> implements ISasPassportRltService {

    @Autowired
    private SasPassportRltMapper sasPassportRltMapper;
    @Autowired
    private SasPassportBscMapper sasPassportBscMapper;
    @Autowired
    private SasPassportDtMapper sasPassportDtMapper;

    @Override
    public Page<SasPassportRlt> selectBySasPassportRlt(Page<SasPassportRlt> page, SasPassportRlt sasPassportRlt){
        page.setRecords(sasPassportRltMapper.selectBySasPassportRlt(page, sasPassportRlt));
        return page;
    }

    @Override
    @Transactional
    public Boolean batchInsert(List<SasPassportRlt> entitys) {

        SasPassportBsc sasPassportBsc = sasPassportBscMapper.selectOne(new SasPassportBsc(entitys.get(0).getSeqNo()));

        StringBuffer rltNo = new StringBuffer();
        if (StringUtil.isNotEmpty(sasPassportBsc.getRltNo())){
            rltNo.append(sasPassportBsc.getRltNo() + ",");
        }
        for (SasPassportRlt entity:entitys){
            entity.setUid(UUIDGenerator.getUUID());
            entity.setChgTmsCnt(0);
            rltNo.append(entity.getRltNo() + ",");

            sasPassportRltMapper.insert(entity);
        }
        //更新核放单表的关联单号
        sasPassportBsc.setRltNo(rltNo.substring(0,rltNo.length()-1));
        sasPassportBscMapper.updateById(sasPassportBsc);
        //新增核放单货物表
        SasPassportDt SasPassportDt = new SasPassportDt();

        sasPassportDtMapper.insert(SasPassportDt);
        return true;
    }

    @Override
    @Transactional
    public Boolean batchAndCascadeInsert(SasPassportBsc sasPass){
        //获取关联单表批量信息
        List<SasPassportRlt> sasPassportRlts = sasPass.getSasPassportRlts();
        //获取货物表批量信息
        List<SasPassportDt> sasPassportDts = sasPass.getSasPassportDts();
        //获取原核放单信息
        SasPassportBsc sasPassportBsc = null;

        if (StringUtil.isNotEmpty(sasPass.getUid())){
            sasPassportBsc = sasPassportBscMapper.selectById(sasPass.getUid());
        }else{
            return false;
        }

        StringBuffer sb = new StringBuffer();
        //原核放单中存在关联单编号，在其尾部追加分隔符
        if (StringUtil.isNotEmpty(sasPassportBsc.getRltNo())){
            sb.append(sasPassportBsc.getRltNo() + "\\");
        }


        if (sasPassportRlts.size() > 0 && sasPassportDts.size() > 0){
            for (SasPassportRlt entity:sasPassportRlts){
                sb.append(entity.getRltNo() + "\\");
                entity.setUid(UUIDGenerator.getUUID());
                entity.setChgTmsCnt(0);
            }
            for (SasPassportDt entity : sasPassportDts) {
                entity.setUid(UUIDGenerator.getUUID());
                entity.setChgTmsCnt(0);
            }
            //批量插入关联单表
            sasPassportRltMapper.insertByBatch(sasPassportRlts);
            //批量插入货物表
            sasPassportDtMapper.insertByBatch(sasPassportDts);
        }else{
            return false;
        }
        //更新核放单关联单编号
        sasPassportBsc.setRltNo(sb.substring(0,sb.length()-1));
        sasPassportBsc.setMasterCuscd(sasPass.getMasterCuscd());//设置主管关区
        //更新核放单
        sasPassportBscMapper.updateById(sasPassportBsc);
        return true;
    }

    @Override
    public List<SasPassportRlt> selectByRltNos(List<String> rltNos){
        Map<String, Object> rltNosParam = new HashMap<String, Object>();
        rltNosParam.put("rltNos", rltNos);
        return sasPassportRltMapper.selectByRltNos(rltNosParam);
    }

    @Override
    @Transactional
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        for (Serializable serializable : idList) {
            //获取当前删除表-->关联表的对应数据
            SasPassportRlt sasPassportRlt = sasPassportRltMapper.selectById(serializable);
            //根据关联表的seqNo获取主表-->核放单表的对应数据
            SasPassportBsc sasPassportBsc = sasPassportBscMapper.selectOne(new SasPassportBsc(sasPassportRlt.getSeqNo()));
            //从核放单表获取RltNo
            String rltNos = sasPassportBsc.getRltNo();
            //获取核放单中RltNo字段转化数组
            String[] rltNoArray = sasPassportBsc.getRltNo().split("\\\\");

            for (int i = 0; i < rltNoArray.length; i++) {
                //匹配关联表的RltNo在核放单中是否存在
                if(StringUtil.equals(rltNoArray[i],sasPassportRlt.getRltNo())){
                    rltNoArray[i] = rltNoArray[rltNoArray.length-1];
                    //数组缩容
                    rltNoArray = Arrays.copyOf(rltNoArray, rltNoArray.length-1);
                    break;
                }
            }
            String newRltNo = Arrays.toString(rltNoArray);
            newRltNo = newRltNo.replace(",","\\")
                    .replace("[","")
                    .replace("]","")
                    .replace(" ","");
            sasPassportBsc.setRltNo(newRltNo);
            sasPassportBscMapper.updateById(sasPassportBsc);
        }
        return super.deleteBatchIds(idList);
    }

}
