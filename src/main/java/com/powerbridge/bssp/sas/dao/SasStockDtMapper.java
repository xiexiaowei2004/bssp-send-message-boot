package com.powerbridge.bssp.sas.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.sas.entity.SasStockDt;

import java.util.List;

/**
 * <p>
 * 出入库商品表 Mapper 接口
 * </p>
 *
 * @author xuzuotao
 * @since 2017-06-01
 */
public interface SasStockDtMapper extends BaseMapper<SasStockDt> {
    /**
     * 查询 分页
     *
     * @param page  分页
     * @param seqNo 单据编号
     * @return List<SasDclDt>
     */
    List<SasStockDt> selectByList(Page<SasStockDt> page, String seqNo);
}