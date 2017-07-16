package com.powerbridge.bssp.sas.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.sas.dao.SasStockDtMapper;
import com.powerbridge.bssp.sas.entity.SasStockDt;
import com.powerbridge.bssp.sas.service.ISasStockDtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出入库商品表 服务实现类
 * </p>
 *
 * @author xuzuotao
 * @since 2017-06-01
 */
@Service("sasStockDtService")
public class SasStockDtServiceImpl extends BaseServiceImpl<SasStockDtMapper, SasStockDt> implements ISasStockDtService {
    @Autowired
    private SasStockDtMapper sasStockDtMapper;

    /**
     * 查询 分页
     *
     * @param page  分页
     * @param seqNo 单据编号
     * @return Page<SasStockDt>
     */
    public Page<SasStockDt> selectByList(Page<SasStockDt> page, String seqNo) {
        page.setRecords(sasStockDtMapper.selectByList(page, seqNo));
        return page;
    }

}
