package com.powerbridge.bssp.edi.xml.prod;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.powerbridge.bssp.common.util.RandomUtils;
import com.powerbridge.bssp.common.util.toolbox.DateUtil;
import com.powerbridge.bssp.common.util.toolbox.StringUtil;
import com.powerbridge.bssp.cop_et.entity.EtArcrpBsc;
import com.powerbridge.bssp.cop_et.service.IEtArcrpBscService;
import com.powerbridge.bssp.edi.entity.EdiRoutingInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author Simon.xie
 * @version 1.0.0
 * @ClassName etGroupProcesser
 * @Description 联网企业档案报文发送处理
 * @Date 2017年5月24日 下午9:32:30
 */
@Component
public class EtArcrpBscGroupProcesser extends GroupProcesser {
    //报文头表服务
    @Autowired
    private IEtArcrpBscService etArcrpBscService;
    @Autowired
    private BuildEtArcrpBscExtranetXML buildEtArcrpBscExtranetXML;
    @Autowired
    private BuildEtArcrpBscXML buildEtArcrpBscXML;

    @Override
    public void execute(String docType, String bizType, String areaCode, String seqNo, BigDecimal chgTmsCnt) {
        //1.查询路由配置表
        EdiRoutingInfo ediRoutingInfo = getEdiRoutingInfo(docType, bizType, areaCode, seqNo);
        if (ediRoutingInfo != null) {
            //2.产生报文生成规则：bizType+申报类型代码+单据编号+日期+3位随机数
            EtArcrpBsc etArcrpBsc = getdclTypecd(seqNo, chgTmsCnt);
            if (etArcrpBsc != null) {
                StringBuffer strBill = new StringBuffer();
                String proName = ediRoutingInfo.getProName();
                if (StringUtil.isEmpty(proName)) {
                    proName = "";
                } else {
                    proName += "-";
                }
                strBill.append(proName).append("EMS-").append(docType).append("-").append(etArcrpBsc.getDclTypecd()).append("-")
                        .append(seqNo).append("-").append(DateUtil.getStringDate()).append("-")
                        .append(RandomUtils.getFixLenthString(3));
                //3.组装sql
                HashMap<String, List> map = getAssembleSql(docType, bizType, seqNo, areaCode, chgTmsCnt);
                if (map.size() > 0) {
                    //4.组装xml文件
                    org.dom4j.Document document = createSendMessageXml(map, docType, bizType, seqNo, strBill.toString(), ediRoutingInfo);
                    //File fileName = new File(System.getProperty("user.dir")+ File.separator + strBill.toString()+ ".xml");
                    //DomWrite.writeDocument(fileName.toString(),document);
                    //6.校验xml
                    File xsdFileName = new File(getXsdUrl() + File.separator + "EMS101.xsd");
                    //5.校验xml

                    boolean flat = true;
                    //validateXML.validateXMLByXSD(document,xsdFileName.toString(),docType, bizType,areaCode,seqNo,strBill.toString());
                    if (flat) {
                        //6.报文数据EDI_MESSAGE_DATA
                        boolean ediMessageLog = savaEdiMessageData(docType, bizType, areaCode, seqNo, ediRoutingInfo, strBill, document);
                        if (ediMessageLog) {
                            //7.删除单据队列表数据
                            delEdiSendQueueById(docType, bizType, areaCode, seqNo);
                            //8.保存单据队列表日志数据
                            insetEdiSendLog(docType, bizType, areaCode, seqNo, TransTypeEnum.Y);
                        }
                    } else {
                        UpdateEdiSendQueue(docType, bizType, seqNo, areaCode);
                    }
                }
            } else {
                UpdateEdiSendQueue(docType, bizType, seqNo, areaCode);
                String msg = "请检查单据号是否正确！";
                inserEdiMessageLog(docType, bizType, areaCode, "", null, msg, TransTypeEnum.EDI_ASSEMBLE_ERROR, "");
            }
        }
    }

    /**
     * @param seqNo
     * @return
     * @Description 获取申报类型代码字段
     */
    private EtArcrpBsc getdclTypecd(String seqNo, BigDecimal chgTmsCnt) {
        EntityWrapper entityWrapper = new EntityWrapper<EtArcrpBsc>();
        entityWrapper.eq("SEQ_NO", seqNo);//单据编号
//		entityWrapper.eq("CHG_TMS_CNT", chgTmsCnt);//变更次数
        return etArcrpBscService.selectOne(entityWrapper);
    }


    /**
     * @param map     联网企业档案库
     * @param docType 单据类型
     * @param bizType 业务类型
     * @return
     * @Description 创建联网企业档案 XMl的文件
     */
    public Document createSendMessageXml(HashMap<String, List> map, String docType, String bizType, String seqNo, String strBill, EdiRoutingInfo ediRoutingInfo) {
        //List<EdiXmlMapDb> ediXmlInfos  = getEdiXmlInfo(docType,bizType);
        Document document = DocumentHelper.createDocument();//
        Element root = null;
        //外网
        if (ediRoutingInfo.getPassageway().equals("2")) {
            // 创建根节点
            root = document.addElement("Signature");
            root.addAttribute("xmlns:xsi", SendMessageXmlEnum.SEND_MESSAGE_XSI_SIGNATURE_TWO);
            root.addAttribute("xsi:noNamespaceSchemaLocation", "EMS101.xsd");
            SendMessageHeadXml SendMessageHeadXml = new SendMessageHeadXml();
            Element dataInfoNode = SendMessageHeadXml.CreateSendSpecialExtranetHeadXml(root, SendMessageGroupKindEnum.ET_ARCRP_BSC_BIZTYPE, seqNo, strBill, ediRoutingInfo);
            //BuildEtArcrpBscExtranetXML buildEtArcrpBscExtranetXML = new BuildEtArcrpBscExtranetXML();
            buildEtArcrpBscExtranetXML.setBillData(map, docType, bizType, dataInfoNode);
        } else {
            // 创建根节点
            root = document.addElement("Signature", SendMessageXmlEnum.SEND_MESSAGE_XSI_SIGNATURE_ONE);
            SendMessageHeadXml SendMessageHeadXml = new SendMessageHeadXml();
            Element dataInfoNode = SendMessageHeadXml.CreateSendSpecialSupervisionHeadXml(root, SendMessageGroupKindEnum.ET_ARCRP_BSC_BIZTYPE, seqNo, strBill, ediRoutingInfo);
            //BuildEtArcrpBscXML buildEtArcrpBscXML = new BuildEtArcrpBscXML();
            buildEtArcrpBscXML.setBillData(map, docType, bizType, dataInfoNode);
        }
        document.setRootElement(root);
        return document;
    }
}
