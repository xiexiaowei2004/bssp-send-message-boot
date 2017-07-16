package com.powerbridge.bssp.saspass.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.powerbridge.bssp.saspass.entity.SasPassportBsc;

public interface ISasPassportBscService extends IService<SasPassportBsc> {

    /**
     * 根据ID查询
     * @param uid
     * @return
     */
    public SasPassportBsc selectByUid(String uid);

    /**
     * 分页查询
     * @param page
     * @param sasPassportBsc
     * @return
     */
    public Page<SasPassportBsc> selectBySasPassportBsc(Page<SasPassportBsc> page, SasPassportBsc sasPassportBsc);
}
