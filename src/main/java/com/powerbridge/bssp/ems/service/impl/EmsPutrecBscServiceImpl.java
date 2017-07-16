package com.powerbridge.bssp.ems.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.ems.dao.*;
import com.powerbridge.bssp.ems.entity.EmsPutrecAcmpFormDt;
import com.powerbridge.bssp.ems.entity.EmsPutrecBsc;
import com.powerbridge.bssp.ems.entity.EmsPutrecUcnsDt;
import com.powerbridge.bssp.ems.service.IEmsPutrecBscService;
import com.powerbridge.bssp.ems.dao.EmsPutrecBscMapper;
import com.powerbridge.bssp.ems.entity.EmsPutrecBsc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：EmsPutrecBscServiceImpl
 * 类描述：账册备案申请表服务实现
 * 创建人：willChen
 * 创建时间：2017/5/17 9:23
 * 修改人：jokylao
 * 修改时间：2017/6/1 22:18
 */
@Service("emsPutrecBscService")
public class EmsPutrecBscServiceImpl extends BaseServiceImpl<EmsPutrecBscMapper, EmsPutrecBsc> implements IEmsPutrecBscService {

    @Autowired
    private EmsPutrecBscMapper emsPutrecBscMapper;
    @Autowired
    private EmsPutrecImgMapper emsPutrecImgMapper;
    @Autowired
    private EmsPutrecExgMapper emsPutrecExgMapper;
    @Autowired
    private EmsPutrecUcnsDtMapper emsPutrecUcnsDtMapper;
    @Autowired
    private EmsPutrecAcmpFormDtMapper emsPutrecAcmpFormDtMapper;
    /**
     * 查询车辆备案信息申请表/分页
     *
     * @param page 分页
     * @param emsPutrecBsc
     * @return Page<SasVehicleBsc>
     */
    public Page<EmsPutrecBsc> selectEmsPutrecBscList(Page<EmsPutrecBsc> page, EmsPutrecBsc emsPutrecBsc){
        page.setRecords(emsPutrecBscMapper.selectEmsPutrecBscList(page, emsPutrecBsc));
        return page;
    }
	@Override
	public EmsPutrecBsc getBusinessData(String str) {
		return emsPutrecBscMapper.getBusinessData(str);
	}
    /**
     * 批量删除
     * @param seqNoByList 主键集合
     * @return
     */
    @Transactional
    public Boolean deleteBySeqNoList(List<String> seqNoByList){
        EntityWrapper entityWrapper = new EntityWrapper<>();
        entityWrapper.in("SEQ_NO", seqNoByList);
        emsPutrecImgMapper.delete(entityWrapper);//删除料件
        emsPutrecExgMapper.delete(entityWrapper);//删除成品
        emsPutrecUcnsDtMapper.delete(entityWrapper);//删除单耗
        emsPutrecAcmpFormDtMapper.delete(entityWrapper);//删除随单附证
        boolean flag = retBool(emsPutrecBscMapper.delete(entityWrapper));
        return flag;
    }
}
