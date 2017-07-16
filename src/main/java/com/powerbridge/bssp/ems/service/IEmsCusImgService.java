package com.powerbridge.bssp.ems.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.powerbridge.bssp.ems.entity.EmsCusImg;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：bssp-receipt
 * 类名称：IEmsCusImgService
 * 类描述：账册备案正式表料件服务
 * 创建人：willChen
 * 创建时间：2017/5/16 22:00
 * 修改人：jokylao
 * 修改时间：2017/6/4 11:23
 */
public interface IEmsCusImgService extends IService<EmsCusImg> {
    /**
     * 查询加工账册备案料件/分页
     *
     * @param page 分页
     * @param emsPutrecImg
     * @return List<EmsCusImg>
     */
    Page<EmsCusImg> selectEmsCusImgList(Page<EmsCusImg> page, EmsCusImg emsPutrecImg);
    /**
     * 获取最大的商品序号
     *
     * @return 账册编号
     */
    BigDecimal getMaxGdsSeqno(String emsNo);
    /**
     * 复制账册料件信息
     *
     * @param emsCusImgList
     * @return
     */
    List<EmsCusImg> copyGds(List<EmsCusImg> emsCusImgList);
    /**
     * 判断料件序号是否存在
     * @param emsNo
     * @param gdsSeqno
     * @return
     */
    Boolean isExistGdsSeqno(String emsNo,BigDecimal gdsSeqno);
}
