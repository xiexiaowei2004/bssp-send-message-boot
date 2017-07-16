package com.powerbridge.bssp.edi.xml.prod;

import com.powerbridge.bssp.edi.entity.EdiXmlMapDb;
import com.powerbridge.bssp.edi.service.IEdiXmlMapDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon.xie
 * @version 1.0.0
 * @ClassName EdiXmlInfoData
 * @Description 字段关系数据
 * @Date 2017年6月1日 下午10:39:16
 */
@Component
public class EdiXmlInfoData {

    //字段关系服务
    @Autowired
    private IEdiXmlMapDbService ediXmlMapDbService;

    /**
     * @param docType 单据类型
     * @param bizType 业务类型
     * @return
     * @Description 根据单据类型，业务类型，表名，字段名查询对应报文字段名
     */
    public List<EdiXmlMapDb> getEdiXmlInfo(final String docType, final String bizType, String dbTable, String passageway) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("docType", docType);
        map.put("bizType", bizType);
        map.put("dbTable", dbTable);
        map.put("passageway", passageway);
        return ediXmlMapDbService.getEdiXmlMapDb(map);
    }
}
