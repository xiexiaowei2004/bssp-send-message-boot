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
 * @ClassName BuildInvBscXML
 * @Description 加贸核注清单,保税清单
 * @author Simon.xie
 * @Date 2017年5月25日 下午9:53:09
 * @version 1.0.0
 */
@Component
public class BuildInvBscXML {
	@Autowired
	private EdiXmlInfoData ediXmlInfoData;
	static final Log logger = LogFactory.getLog(BuildSasVehicleBscXml.class);
	public void setBillData(HashMap<String,List> map, String docType,String bizType, Element dataInfoNode) {
		//创建BussinessData根节点
        Element bussinessDataNode= dataInfoNode.addElement("BussinessData");
        
        //创建EMS101根节点
        Element iNV101  = bussinessDataNode.addElement("INV101");
        for (Entry<String, List> entry: map.entrySet()) {
            String  key = entry.getKey();
            List list = entry.getValue();
            if(key.equalsIgnoreCase("INV_BSC")){
         	   List<InvBsc> invBsc = (List<InvBsc>) list;
         	   buildInvBscHead(invBsc, docType,bizType, iNV101);
            }else if(key.equalsIgnoreCase("INV_DT")){
         	   List<InvDt> invDt = (List<InvDt>) list;
         	   buildInvDt(invDt, docType,bizType, iNV101);
            /*} else if(key.equalsIgnoreCase("INV_DECLARE_BILL")){
         	   List<InvDecDt> invDeclareBill = (List<InvDecDt>) list;
         	   buildInvDecDt(invDeclareBill, docType,bizType, iNV101);*/
            }else if(key.equalsIgnoreCase("INV_ACMP_FORM_DT")){
         	   List<InvAcmpFormDt> invAcmpFormDt = (List<InvAcmpFormDt>) list;
         	   buildInvAcmpFromDt(invAcmpFormDt, docType,bizType, iNV101);
            }
         }
	}
    
    /**
     * 
     * @Description 构建报文体随附单证明细数据
     * @param docType
	 * @param bizType
     * @param iNV101
     */
	private void buildInvAcmpFromDt(List<InvAcmpFormDt> list, String docType,String bizType, Element iNV101) {
		//
		List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_ACMP_FORM_DT","1");
		if(list.size() > 0){
			Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
			//创建ET_ARCRP_ACMP_FORM_DT根节点
			String txt ="";
			String str ="";
			for(int  i= 0; i< list.size(); i ++){
				Element node = iNV101.addElement("BondInvtAcmpFormDt");
				for (int j = 0; j < ediXmlMapDbs.size(); j++) {
					logger.info(ediXmlMapDbs.get(j).getXmlName());
					String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
					//xmlNmae
					String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_ACMP_FORM_DT", xmlNmae);

					try {
						InvAcmpFormDt invAcmpFormDt = new InvAcmpFormDt();
						str = BeanUtil.getMethodReturnType(invAcmpFormDt,beanMap.get(dbColumn));
						logger.info(str);

					} catch (Exception e) {
						e.printStackTrace();
					}

					txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));
					if(txt.equals("null")){
						txt = BeanUtil.getNullTypeValue(str,txt);
					}
					if(txt.equals("")){
						node.addElement(xmlNmae);
					} else {
						node.addElement(xmlNmae).addText(txt);
					}
				}
			}
		}
	}
	/**
     * 
     * @Description 构建报关商品数据
     * @param list
	 * @param docType
	 * @param bizType
     * @param iNV101
     */
	private void buildInvDecDt(List<InvDecDt> list, String docType,String bizType, Element iNV101) {
		//
		List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_DEC_DT","1");
		if(list.size() > 0){
			Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
			//创建料件BondInvtDt根节点
			String txt ="";
			String str ="";
			for(int  i= 0; i< list.size(); i ++){
				Element node = iNV101.addElement("BondInvtDt");
				for (int j = 0; j < ediXmlMapDbs.size(); j++) {
					logger.info(ediXmlMapDbs.get(j).getXmlName());
					String xmlNmae = ediXmlMapDbs.get(j).getXmlName();
					//xmlNmae
					String dbColumn = XMLUtils.getDbName(ediXmlMapDbs, "INV_DEC_DT", xmlNmae);

					try {
						InvExg invExg = new InvExg();
						str = BeanUtil.getMethodReturnType(invExg,beanMap.get(dbColumn));
						logger.info(str);

					} catch (Exception e) {
						e.printStackTrace();
					}

					txt = String.valueOf(BeanKit.getProperty(list.get(i),beanMap.get(dbColumn)));
					if(txt.equals("null")){
						txt = BeanUtil.getNullTypeValue(str,txt);
					}
					if(txt.equals("")){
						node.addElement(xmlNmae);
					} else {
						node.addElement(xmlNmae).addText(txt);
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
     * @param iNV101
     */
	private void buildInvDt(List<InvDt> list, String docType,String bizType, Element iNV101) {
		//
		List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_DT","1");
		if(list.size() > 0){
			Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
			//创建料件ET_ARCRP_DT根节点
			String txt ="";
			String str ="";
			for(int  i= 0; i< list.size(); i ++){
				Element node = iNV101.addElement("BondInvtDt");
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
					if(txt.equals("null")){
						txt = BeanUtil.getNullTypeValue(str,txt);
					}
					if(txt.equals("")){
						node.addElement(xmlNmae);
					} else {
						node.addElement(xmlNmae).addText(txt);
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
     * @param iNV101
     */
	private void buildInvBscHead(List<InvBsc> list, String docType,String bizType, Element iNV101) {
		//
		List<EdiXmlMapDb> ediXmlMapDbs = ediXmlInfoData.getEdiXmlInfo(docType, bizType, "INV_BSC","1");
		if(list.size() > 0){
			Map<String,String> beanMap = BeanUtil.getBeanMap(list.get(0));
			//创建BondInvtBsc根节点
			Element node = iNV101.addElement("BondInvtBsc");
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
					if(txt.equals("null")){
						txt = BeanUtil.getNullTypeValue(str,txt);
					}
					if(txt.equals("")){
						node.addElement(xmlNmae);
					} else {
						node.addElement(xmlNmae).addText(txt);
					}
				}
			}
		}
	}

}
