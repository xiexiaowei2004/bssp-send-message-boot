package com.powerbridge.bssp.edi.xml.prod;

import com.powerbridge.bssp.common.util.BeanKit;
import com.powerbridge.bssp.common.util.toolbox.DateUtil;
import com.powerbridge.bssp.edi.entity.EdiXmlMapDb;
import com.powerbridge.bssp.edi.xml.util.BeanUtil;
import com.powerbridge.bssp.edi.xml.util.XMLUtils;
import com.powerbridge.bssp.inv.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @ClassName BuildEmsPutrecBscXML
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author Simon.xie
 * @Date 2017年5月25日 下午9:53:09
 * @version 1.0.0
 */
@Component
public class BuildInvBscExtranetXML {
    static final Log logger = LogFactory.getLog(BuildSasVehicleBscXml.class);

    @Autowired
    private EdiXmlInfoData ediXmlInfoData;

    public void setBillData(HashMap<String,List> map, String docType,String bizType, Element dataInfoNode) {
        //创建BussinessData根节点
        Element bussinessDataNode= dataInfoNode.addElement("BussinessData");

        //创建InvtMessage根节点
        String dclcusFlag = "";
        Element InvtMessage  = bussinessDataNode.addElement("InvtMessage");
        for (Entry<String, List> entry: map.entrySet()) {
            String  key = entry.getKey();
            List list = entry.getValue();
            if(key.equalsIgnoreCase("INV_BSC")){
                List<InvBsc> invBsc = (List<InvBsc>) list;
                dclcusFlag = buildInvBscHead(invBsc, docType,bizType, InvtMessage);
            }else if (key.equalsIgnoreCase("INV_DT")){
                List<InvDt> invDt = (List<InvDt>) list;
                buildInvDt(invDt, docType,bizType, InvtMessage);
            }

            // 报关才显示 报关商品节点
            if (SendMessageDeclareTypeEnum.DCLCUS_FLAG_1.equals(dclcusFlag)){
                if (key.equalsIgnoreCase("INV_DEC_BSC")){
                    List<InvDecBsc> invDecBsc = (List<InvDecBsc>) list;
                    buildInvDecBsc(invDecBsc, docType,bizType, InvtMessage);
                }else if(key.equalsIgnoreCase("INV_DEC_DT")){
                    List<InvDecDt> invDeclareBill = (List<InvDecDt>) list;
                    buildInvDecDt(invDeclareBill, docType,bizType, InvtMessage);
                }
            }
        }
//        <ListStat/>
//        <SysId/>
//        <OperCusRegCode/>  保留，后续也许会填
        bussinessDataNode.addElement("DelcareFlag").addText("0");
    }

    /**
     *
     * @Description 构建报文体随附单证明细数据
     * @param docType
     * @param bizType
     * @param iNV101
     */
//    private void buildInvAcmpFromDt(List<InvAcmpFormDt> list, String docType,String bizType, Element iNV101) {
//
//        List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_ACMP_FORM_DT","1");
//        if(list.size() > 0){
//            Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
//            //创建ET_ARCRP_ACMP_FORM_DT根节点
//            String txt ="";
//            String str ="";
//            for(int  i= 0; i< list.size(); i ++){
//                Element node = iNV101.addElement("BondInvtAcmpFormDt");
//                for (int j = 0; j < ediXmlMapDbs.size(); j++) {
//                    logger.info(ediXmlMapDbs.get(j).getXmlName());
//                    String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
//                    //xmlNmae
//                    String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_ACMP_FORM_DT", xmlNmae);
//
//                    try {
//                        InvAcmpFormDt invAcmpFormDt = new InvAcmpFormDt();
//                        str = BeanUtil.getMethodReturnType(invAcmpFormDt,beanMap.get(dbColumn));
//                        logger.info(str);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));
//                    if(txt.equals("null")){
//                        txt = BeanUtil.getNullTypeValue(str,txt);
//                    }
//                    if(dbColumn.equals("PARAM4")){
//                        //txt = DateUtil.now();
//                        txt ="2117-06-13 01:14:41";
//                    }
//                    if(txt.equals("")){
//                        node.addElement(xmlNmae);
//                    } else {
//                        node.addElement(xmlNmae).addText(txt);
//                    }
//                }
//            }
//        }
//    }
    /**
     *
     * @Description 构建报关商品数据
     * @param list
     * @param docType
     * @param bizType
     * @param InvtMessage
     */
    private void buildInvDecDt(List<InvDecDt> list, String docType,String bizType, Element InvtMessage) {

        List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_DEC_DT","2");
        if(list.size() > 0){
            Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
            //创建料件BondInvtDt根节点
            String txt ="";
            String str ="";
            for(int  i= 0; i< list.size(); i ++){
                Element node = InvtMessage.addElement("InvtDecListType");
                for (int j = 0; j < ediXmlMapDbs.size(); j++) {
                    logger.info(ediXmlMapDbs.get(j).getXmlName());
                    String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
                    //xmlNmae
                    String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_DEC_DT", xmlNmae);

                    try {
                        InvDecDt invDeclareBill = new InvDecDt();
                        str = BeanUtil.getMethodReturnType(invDeclareBill,beanMap.get(dbColumn));
                        logger.info(str);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));
                    try {
                        if(!txt.equals("") && !txt.equals("null")){
                            Boolean dateTime = DateUtil.StringCheckDateTime(txt);
                            Boolean date = DateUtil.StringCheckDate(txt);
                            if(dateTime){
                                txt = DateUtil.strToDateFormatDateTime(txt);
                            }
                            if(date){
                                txt = DateUtil.strToDateFormatDate(txt);
                            }
                            node.addElement(xmlNmae).addText(txt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     *
     * @Description 构建报关商品商品表头
     * @param list
     * @param docType
     * @param bizType
     * @param InvtMessage
     */
    private void buildInvDecBsc(List<InvDecBsc> list, String docType,String bizType, Element InvtMessage) {

        List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_DEC_BSC","2");
        if(list.size() > 0){
            Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
            //创建料件ET_ARCRP_DT根节点
            String txt ="";
            String str ="";
            for(int  i= 0; i< list.size(); i ++){
                Element node = InvtMessage.addElement("InvtDecHeadType");
                for (int j = 0; j < ediXmlMapDbs.size(); j++) {
                    logger.info(ediXmlMapDbs.get(j).getXmlName());
                    String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
                    //xmlNmae
                    String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_DEC_BSC", xmlNmae);

                    try {
                        InvDecBsc invDecBsc = new InvDecBsc();
                        str = BeanUtil.getMethodReturnType(invDecBsc,beanMap.get(dbColumn));
                        logger.info(str);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));

                    try {
                        if(!txt.equals("") && !txt.equals("null")){
                            Boolean dateTime = DateUtil.StringCheckDateTime(txt);
                            Boolean date = DateUtil.StringCheckDate(txt);
                            if(dateTime){
                                txt = DateUtil.strToDateFormatDateTime(txt);
                            }
                            if(date){
                                txt = DateUtil.strToDateFormatDate(txt);
                            }
                            node.addElement(xmlNmae).addText(txt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    /**
     *
     * @Description 构建报文体料件数据
     * @param list
     * @param docType
     * @param bizType
     * @param InvtMessage
     */
    private void buildInvDt(List<InvDt> list, String docType,String bizType, Element InvtMessage) {

        List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_DT","2");
        if(list.size() > 0){
            Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
            //创建料件ET_ARCRP_DT根节点
            String txt ="";
            String str ="";
            for(int  i= 0; i< list.size(); i ++){
                Element node = InvtMessage.addElement("InvtListType");
                for (int j = 0; j < ediXmlMapDbs.size(); j++) {
                    logger.info(ediXmlMapDbs.get(j).getXmlName());
                    String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
                    //xmlNmae
                    String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_DT", xmlNmae);

                    try {
                        InvDt invDt = new InvDt();
                        str = BeanUtil.getMethodReturnType(invDt,beanMap.get(dbColumn));
                        logger.info(str);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));

                    try {
                        if(!txt.equals("") && !txt.equals("null")){
                            Boolean dateTime = DateUtil.StringCheckDateTime(txt);
                            Boolean date = DateUtil.StringCheckDate(txt);
                            if(dateTime){
                                txt = DateUtil.strToDateFormatDateTime(txt);
                            }
                            if(date){
                                txt = DateUtil.strToDateFormatDate(txt);
                            }
                            node.addElement(xmlNmae).addText(txt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     *
     * @Description 构建表头数据
     * @param list
     * @param docType
     * @param bizType
     * @param InvtMessage
     */
    private String buildInvBscHead(List<InvBsc> list, String docType,String bizType, Element InvtMessage) {

        List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_BSC","2");
        String dclcusFlag = "";
        if(list.size() > 0){
            Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
            //创建BondInvtBsc根节点
            Element node = InvtMessage.addElement("InvtHeadType");
            String txt ="";
            String str ="";
            for(int  i= 0; i< list.size(); i ++){
                for (int j = 0; j < ediXmlMapDbs.size(); j++) {
                    //logger.info(ediXmlMapDbs.get(j).getXmlName());
                    String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
                    //xmlNmae
                    String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_BSC", xmlNmae);

                    try {
                        InvBsc invBsc = new InvBsc();
                        str = BeanUtil.getMethodReturnType(invBsc,beanMap.get(dbColumn));
                        //logger.info(str);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));
                    try {
                        if(!txt.equals("") && !txt.equals("null")){
                            Boolean dateTime = DateUtil.StringCheckDateTime(txt);
                            Boolean date = DateUtil.StringCheckDate(txt);
                            if(dateTime){
                                txt = DateUtil.strToDateFormatDateTime(txt);
                            }
                            if(date){
                                txt = DateUtil.strToDateFormatDate(txt);
                            }
                            node.addElement(xmlNmae).addText(txt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(xmlNmae.equalsIgnoreCase("dclcusFlag")){
                        dclcusFlag = txt;
                    }
                }
            }
        }
        return dclcusFlag;
    }

}
