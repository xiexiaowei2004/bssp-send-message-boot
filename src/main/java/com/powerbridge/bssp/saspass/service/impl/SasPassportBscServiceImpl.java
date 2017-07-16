package com.powerbridge.bssp.saspass.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.saspass.dao.SasPassportDtMapper;
import com.powerbridge.bssp.saspass.entity.SasPassportDt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.saspass.dao.SasPassportBscMapper;
import com.powerbridge.bssp.saspass.entity.SasPassportBsc;
import com.powerbridge.bssp.saspass.service.ISasPassportBscService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("sasPassportBscService")
public class SasPassportBscServiceImpl extends BaseServiceImpl<SasPassportBscMapper, SasPassportBsc> implements ISasPassportBscService {

    @Autowired
    private SasPassportBscMapper sasPassportBscMapper;

    @Autowired
    private SasPassportDtMapper sasPassportDtMapper;
    @Override
    public SasPassportBsc selectByUid(String uid) {
        return sasPassportBscMapper.selectByUid(uid);
    }

    @Override
    public Page<SasPassportBsc> selectBySasPassportBsc(Page<SasPassportBsc> page, SasPassportBsc sasPassportBsc){
        page.setRecords(sasPassportBscMapper.selectBySasPassportBsc(page, sasPassportBsc));
        return page;
    }

    @Override
    @Transactional
    public boolean insert(SasPassportBsc entity) {
        SasPassportDt sasPassportDt = new SasPassportDt();
        sasPassportDt.setUid(String.valueOf(UUID.randomUUID().hashCode()));
        sasPassportDtMapper.insert(sasPassportDt);
        entity.setUid(String.valueOf(UUID.randomUUID().hashCode()));
        return sasPassportBscMapper.insert(entity)>0;
    }
}
