package com.powerbridge.bssp.saspass.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.saspass.dao.SasPassportDtMapper;
import com.powerbridge.bssp.saspass.entity.SasPassportDt;
import com.powerbridge.bssp.saspass.service.ISasPassportDtService;
@Service("sasPassportDtService")
public class SasPassportDtServiceImpl extends BaseServiceImpl<SasPassportDtMapper, SasPassportDt> implements ISasPassportDtService {

	@Autowired
    private SasPassportDtMapper sasPassportDtMapper;

	@Override
	public SasPassportDt selectByUid(String id){
        return sasPassportDtMapper.selectByUid(id);
    }

    @Override
    public Page<SasPassportDt> selectBySasPassportDt(Page<SasPassportDt> page, SasPassportDt sasPassportDt){
        page.setRecords(sasPassportDtMapper.selectBySasPassportDt(page, sasPassportDt));
        return page;
    }
}
