package com.powerbridge.bssp.edi.xml.prod;


import com.powerbridge.bssp.common.util.UUIDGenerator;
import com.powerbridge.bssp.common.util.toolbox.DateUtil;
import com.powerbridge.bssp.edi.entity.EdiMessageLog;
import com.powerbridge.bssp.edi.entity.EdiSendLog;
import com.powerbridge.bssp.edi.service.IEdiMessageLogService;
import com.powerbridge.bssp.edi.service.IEdiSendLogService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class ValidateXML {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    //    private WebApplicationContext wac = WebContextListener.getCurrentWebApplicationContext();   // 获取容器（上下文环境）
    //单据队列表日志服务
    @Autowired
    private IEdiSendLogService ediSendLogService;

    //报文日志服务
    @Autowired
    private IEdiMessageLogService ediMessageLogService;

    //    public ValidateXML(){
//        // 通过容器 获取service
//        ediSendLogService = (IEdiSendLogService) wac.getBean("ediSendLogService");
//        ediMessageLogService = (IEdiMessageLogService)wac.getBean("ediMessageLogService");
//    }
    public boolean validateXMLByXSD(Document xmlDocument, String xsdFileName, String docType, String bizType, String areaCode, String seqNo, String fileName) {
        boolean flat = false;
        try {
            //创建默认的XML错误处理器
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            //获取基于 SAX 的解析器的实例
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //解析器在解析时验证 XML 内容。
            factory.setValidating(true);
            //指定由此代码生成的解析器将提供对 XML 名称空间的支持。
            factory.setNamespaceAware(true);
            //使用当前配置的工厂参数创建 SAXParser 的一个新实例。
            SAXParser parser = factory.newSAXParser();
            //创建一个读取工具
            SAXReader xmlReader = new SAXReader();
            //获取要校验xml文档实例
            //Document xmlDocument =  xmlReader.read(new File(xmlFileName));
            //设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    "file:" + xsdFileName);
            //创建一个SAXValidator校验工具，并设置校验工具的属性
            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            //设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
            validator.setErrorHandler(errorHandler);
            //校验
            //xmlDocument.setXMLEncoding("utf-8");
            validator.validate(xmlDocument);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            XMLWriter xmlWriter = null;
            try {
                xmlWriter = new XMLWriter(byteOut);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                xmlWriter.write(xmlDocument);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = byteOut.toByteArray();
            //如果错误信息不为空，说明校验失败，打印错误信息
            if (errorHandler.getErrors().hasContent()) {
                logger.info("XML文件通过XSD文件校验失败！");
                Element element = errorHandler.getErrors();
                logger.info(element.asXML());
                //insetEdiSendLog(docType, bizType, areaCode, seqNo,element.asXML().getBytes(),TransTypeEnum.ONE);
                inserEdiMessageLog(docType, bizType, areaCode, fileName, data, element.asXML(), TransTypeEnum.EDI_XML_ERROR, "");

            } else {
                logger.info("XML文件通过XSD文件校验成功！");
                flat = true;
                String msg = "XML文件通过XSD文件校验成功！";
                inserEdiMessageLog(docType, bizType, areaCode, fileName, msg.getBytes(), "", TransTypeEnum.EDI_XML_SUCCESS, "");
            }
        } catch (Exception ex) {
            inserEdiMessageLog(docType, bizType, areaCode, fileName, null, ex.getMessage(), "XML文件: " + xmlDocument + " 通过XSD文件:" + xsdFileName + "检验过程发生错误", "");
            ex.printStackTrace();
        }
        return flat;
    }

    /**
     * @param docType  监管场所
     * @param bizType  单据类型
     * @param areaCode 业务类型
     * @param seqNo    单据编号
     * @Description 保存单据队列表日志数据
     */
    public void insetEdiSendLog(final String docType, final String bizType, final String areaCode,
                                final String seqNo, byte[] bytes, String Status) {
        EdiSendLog ediSendLog = new EdiSendLog();
        ediSendLog.setUid(UUIDGenerator.getUUID());
        ediSendLog.setAreaCode(areaCode);
        ediSendLog.setBizType(bizType);
        ediSendLog.setDocType(docType);
        ediSendLog.setSeqNo(seqNo);
        ediSendLog.setStatus(Status);
        ediSendLog.setFailNote(bytes);
        ediSendLogService.insert(ediSendLog);
    }


    public void inserEdiMessageLog(String docType, String bizType, String areaCode, String fileName, byte[] msg, String checkInfo, String status, String messageDataUid) {
        EdiMessageLog ediMessageLog = new EdiMessageLog();
        ediMessageLog.setUid(UUIDGenerator.getUUID());
        ediMessageLog.setAreaCode(areaCode);
        ediMessageLog.setBizType(bizType);
        ediMessageLog.setDocType(docType);
        ediMessageLog.setFileName(fileName);
        ediMessageLog.setProcessingTime(DateUtil.now());
        ediMessageLog.setProcessingLog(msg);
        ediMessageLog.setCheckInfo(checkInfo.getBytes());
        ediMessageLog.setStatus(status);
        ediMessageLog.setTransType(TransTypeEnum.S);
        ediMessageLog.setMessageUid(messageDataUid);
        ediMessageLogService.insert(ediMessageLog);
    }

}
