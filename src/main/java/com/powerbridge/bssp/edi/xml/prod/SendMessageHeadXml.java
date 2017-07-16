package com.powerbridge.bssp.edi.xml.prod;

import com.powerbridge.bssp.edi.entity.EdiRoutingInfo;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Simon.xie
 * @version 1.0.0
 * @ClassName SendMessageHeadXml
 * @Description 组装发送报文头xml
 * @Date 2017年6月4日 下午4:37:17
 */
public class SendMessageHeadXml {
    private static Logger logger = LoggerFactory.getLogger(SendMessageHeadXml.class);

    /**
     * @param messageType
     * @param root
     * @return
     * @Description 组装发送报文头xml-基础
     */
    public Element CreateSendBeasHeadXml(Element root, String messageType, String seqNo, String strBill, EdiRoutingInfo ediRoutingInfo) {
        // 创建SignedInfo根节点
        Element signedInfoNode = root.addElement("SignedInfo");
        Element canonicalizationMethod = signedInfoNode.addElement("CanonicalizationMethod");
        canonicalizationMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_SIGNATURE_ONE);
        //canonicalizationMethod.addAttribute("Algorithm", "");
        Element signatureMethod = signedInfoNode.addElement("SignatureMethod");
        signatureMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_TWO);
        //signatureMethod.addAttribute("Algorithm", "");
        Element reference = signedInfoNode.addElement("Reference");
        Element algorithmNode = reference.addAttribute("URI", "string");
        Element digestMethod = algorithmNode.addElement("DigestMethod");
        digestMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_THREE);
        //digestMethod.addAttribute("Algorithm", "");
        algorithmNode.addElement("DigestValue").addText("");

        //创建SignatureValue根节点
        root.addElement("SignatureValue").addText("");
        //创建SignatureValue根节点
        Element keyInfoNode = root.addElement("KeyInfo");
        //动态加载值
        keyInfoNode.addElement("KeyName").addText("aa");

        //创建Object根节点
        Element objectNode = root.addElement("Object");
        objectNode.addAttribute("Id","string");
        Element packageNode = objectNode.addElement("Package");
        Element envelopInfo = packageNode.addElement("EnvelopInfo");
        envelopInfo.addElement("version").addText(TransTypeEnum.CLASSVER);      //报文头
        envelopInfo.addElement("business_id").addText(seqNo);
        envelopInfo.addElement("message_id").addText(strBill);
        envelopInfo.addElement("file_name").addText(strBill + ".xml");
        envelopInfo.addElement("message_type").addText(messageType);
        envelopInfo.addElement("sender_id").addText(ediRoutingInfo.getSenderId());
        envelopInfo.addElement("receiver_id").addText(ediRoutingInfo.getReceiverId());
        envelopInfo.addElement("send_time").addText(LocalDateTime.now() + "");

        //创建DataInfo根节点
        Element dataInfoNode = packageNode.addElement("DataInfo");
        Element pocketInfoNode = dataInfoNode.addElement("PocketInfo");
        pocketInfoNode.addElement("pocket_id");
        pocketInfoNode.addElement("total_pocket_qty").addText("1");
        pocketInfoNode.addElement("cur_pocket_no").addText(1 + "");
        //pocketInfoNode.addElement("is_unstructured").addText("");
        return dataInfoNode;
    }

    /**
     * @param messageType
     * @param root
     * @return
     * @Description 组装发送报文头xml-基础
     */
    public Element CreateSendSpecialSupervisionHeadXml(Element root, String messageType, String seqNo, String strBill, EdiRoutingInfo ediRoutingInfo) {
        // 创建SignedInfo根节点
        Element signedInfoNode = root.addElement("SignedInfo");
        Element canonicalizationMethod = signedInfoNode.addElement("CanonicalizationMethod");
        canonicalizationMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_ONE);
        Element signatureMethod = signedInfoNode.addElement("SignatureMethod");
        signatureMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_TWO);
        Element reference = signedInfoNode.addElement("Reference");
        Element algorithmNode = reference.addAttribute("URI", "string");
        Element digestMethod = algorithmNode.addElement("DigestMethod");
        digestMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_THREE);
        algorithmNode.addElement("DigestValue").addText("");

        //创建SignatureValue根节点
        root.addElement("SignatureValue").addText("");
        //创建SignatureValue根节点
        Element keyInfoNode = root.addElement("KeyInfo");
        //动态加载值
        keyInfoNode.addElement("KeyName").addText("aa");

        //创建Object根节点
        Element objectNode = root.addElement("Object");
        objectNode.addAttribute("Id","string");
        Element packageNode = objectNode.addElement("Package");
        Element envelopInfo = packageNode.addElement("EnvelopInfo");
        envelopInfo.addElement("version").addText(TransTypeEnum.CLASSVER);      //报文头
        envelopInfo.addElement("business_id").addText(seqNo);
        envelopInfo.addElement("message_id").addText(strBill);
        envelopInfo.addElement("file_name").addText(strBill + ".xml");
        envelopInfo.addElement("message_type").addText(messageType);
        envelopInfo.addElement("sender_id").addText(ediRoutingInfo.getSenderId());
        envelopInfo.addElement("receiver_id").addText(ediRoutingInfo.getReceiverId());
        envelopInfo.addElement("send_time").addText(LocalDateTime.now() + "");

        //创建DataInfo根节点
        Element dataInfoNode = packageNode.addElement("DataInfo");
        Element pocketInfoNode = dataInfoNode.addElement("PocketInfo");
        pocketInfoNode.addElement("pocket_id");
        pocketInfoNode.addElement("total_pocket_qty").addText(1 + "");
        pocketInfoNode.addElement("cur_pocket_no").addText(1 + "");
        return dataInfoNode;
    }

    /**
     * @param messageType
     * @param root
     * @return
     * @Description 组装发送报文头xml-基础(含：is_unstructured节点)
     */
    public Element CreateSendSpecialSupervisionHeadXmlByUnstructured(Element root, String messageType, String seqNo, String strBill, EdiRoutingInfo ediRoutingInfo) {
        // 创建SignedInfo根节点
        Element signedInfoNode = root.addElement("SignedInfo");
        Element canonicalizationMethod = signedInfoNode.addElement("CanonicalizationMethod");
        canonicalizationMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_ONE);
        Element signatureMethod = signedInfoNode.addElement("SignatureMethod");
        signatureMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_TWO);
        Element reference = signedInfoNode.addElement("Reference");
        Element algorithmNode = reference.addAttribute("URI", "String");
        Element digestMethod = algorithmNode.addElement("DigestMethod");
        digestMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_THREE);
        algorithmNode.addElement("DigestValue").addText("");

        //创建SignatureValue根节点
        root.addElement("SignatureValue").addText("");
        //创建SignatureValue根节点
        Element keyInfoNode = root.addElement("KeyInfo");
        //动态加载值
        keyInfoNode.addElement("KeyName").addText("aa");

        //创建Object根节点
        Element objectNode = root.addElement("Object");
        Element packageNode = objectNode.addElement("Package");
        Element envelopInfo = packageNode.addElement("EnvelopInfo");
        envelopInfo.addElement("version").addText(TransTypeEnum.CLASSVER);      //报文头
        envelopInfo.addElement("business_id").addText(seqNo);
        envelopInfo.addElement("message_id").addText(strBill);
        envelopInfo.addElement("file_name").addText(strBill + ".xml");
        envelopInfo.addElement("message_type").addText(messageType);
        envelopInfo.addElement("sender_id").addText(ediRoutingInfo.getSenderId());
        envelopInfo.addElement("receiver_id").addText(ediRoutingInfo.getReceiverId());
        envelopInfo.addElement("send_time").addText(LocalDateTime.now() + "");

        //创建DataInfo根节点
        Element dataInfoNode = packageNode.addElement("DataInfo");
        Element pocketInfoNode = dataInfoNode.addElement("PocketInfo");
        pocketInfoNode.addElement("pocket_id");
        pocketInfoNode.addElement("total_pocket_qty").addText(1 + "");
        pocketInfoNode.addElement("cur_pocket_no").addText(1 + "");
        pocketInfoNode.addElement("is_unstructured").addText(1 + "");
        return dataInfoNode;
    }

    /**
     * @param messageType
     * @param root
     * @return
     * @Description 组装发送报文头xml-基础(外网)
     */
    public Element CreateSendSpecialExtranetHeadXml(Element root, String messageType, String seqNo, String strBill, EdiRoutingInfo ediRoutingInfo) {
        // 创建SignedInfo根节点
        Element signedInfoNode = root.addElement("SignedInfo");
        Element canonicalizationMethod = signedInfoNode.addElement("CanonicalizationMethod");
        canonicalizationMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_ONE);
        Element signatureMethod = signedInfoNode.addElement("SignatureMethod");
        signatureMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_TWO);
        Element reference = signedInfoNode.addElement("Reference");
        Element algorithmNode = reference.addAttribute("URI", "String");
        Element digestMethod = algorithmNode.addElement("DigestMethod");
        digestMethod.addAttribute("Algorithm", SendMessageXmlEnum.SEND_MESSAGE_XSI_ALGORITHM_THREE);
        algorithmNode.addElement("DigestValue").addText("");

        //创建SignatureValue根节点
        root.addElement("SignatureValue").addText("");
        //创建SignatureValue根节点
        Element keyInfoNode = root.addElement("KeyInfo");
        //动态加载值
        keyInfoNode.addElement("KeyName").addText("");

        //创建Object根节点
        Element objectNode = root.addElement("Object");
        objectNode.addAttribute("Id", "String");
        Element packageNode = objectNode.addElement("Package");
        Element envelopInfo = packageNode.addElement("EnvelopInfo");
        envelopInfo.addElement("version").addText(TransTypeEnum.CLASSVER_EXTRANET);      //报文头
        envelopInfo.addElement("business_id").addText(seqNo);
        envelopInfo.addElement("message_id").addText(strBill);
        envelopInfo.addElement("file_name").addText(strBill + ".xml");
        envelopInfo.addElement("message_type").addText(messageType);
        envelopInfo.addElement("sender_id").addText(ediRoutingInfo.getSenderId());
        envelopInfo.addElement("receiver_id").addText(ediRoutingInfo.getReceiverId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        envelopInfo.addElement("send_time").addText(sdf.format(new Date()));

        //创建DataInfo根节点
        Element dataInfoNode  = packageNode.addElement("DataInfo");
        dataInfoNode.addAttribute("xsi:noNamespaceSchemaLocation", ediRoutingInfo.getBizType()+".xsd");
        dataInfoNode.addAttribute("xmlns:xsi", SendMessageXmlEnum.SEND_MESSAGE_XSI_SIGNATURE_TWO);
        Element pocketInfoNode= dataInfoNode.addElement("PocketInfo");
        pocketInfoNode.addElement("pocket_id");
        pocketInfoNode.addElement("total_pocket_qty").addText(1+"");
        pocketInfoNode.addElement("cur_pocket_no").addText(1+"");
        pocketInfoNode.addElement("is_unstructured").addText("");
        return dataInfoNode;
    }

    public static void main(String args[]) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        logger.info(time);
    }
}
