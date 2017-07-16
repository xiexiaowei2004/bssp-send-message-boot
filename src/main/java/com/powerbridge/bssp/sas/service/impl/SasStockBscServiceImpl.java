package com.powerbridge.bssp.sas.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.sas.dao.SasDclDtMapper;
import com.powerbridge.bssp.sas.dao.SasStockBscMapper;
import com.powerbridge.bssp.sas.dao.SasStockDtMapper;
import com.powerbridge.bssp.sas.entity.SasStockBsc;
import com.powerbridge.bssp.sas.service.ISasStockBscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 出入库单表 服务实现类
 * </p>
 *
 * @author xuzuotao
 * @since 2017-06-01
 */
@Service("sasStockBscService")
public class SasStockBscServiceImpl extends BaseServiceImpl<SasStockBscMapper, SasStockBsc> implements ISasStockBscService {
    @Autowired
    private SasStockBscMapper sasStockBscMapper;
    @Autowired
    private SasStockDtMapper sasStockDtMapper;

    /**
     * 查询出入库单/分页

     * @param page  分页
     * @param sasStockBsc
     * @return Page<SasStockBsc>
     */
    public Page<SasStockBsc> selectBySasStockBsc(Page<SasStockBsc> page, SasStockBsc sasStockBsc) {
        page.setRecords(sasStockBscMapper.selectBySasStockBsc(page, sasStockBsc));
        return page;
    }

    /**
     * @param seqNo
     * @return boolean
     * @throws
     * @Description: 用于删除主表时，同时删除子表(单条删除)
     */
    @Transactional
    public boolean txDeleteSasStockBsc(String seqNo) {
        EntityWrapper entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("SEQ_NO", seqNo);
        sasStockDtMapper.delete(entityWrapper);
        boolean flag = retBool(sasStockBscMapper.delete(entityWrapper));
        return flag;
    }

    /**
     * @param seqNoByList
     * @return boolean
     * @throws
     * @Description: 用于删除主表时，同时删除子表(批量删除)
     */
    @Transactional
    public boolean txDeleteSasStockBscByList(List<String> seqNoByList) {
        EntityWrapper entityWrapper = new EntityWrapper<>();
        entityWrapper.in("SEQ_NO", seqNoByList);
        sasStockDtMapper.delete(entityWrapper);
        boolean flag = retBool(sasStockBscMapper.delete(entityWrapper));
        return flag;
    }
}
