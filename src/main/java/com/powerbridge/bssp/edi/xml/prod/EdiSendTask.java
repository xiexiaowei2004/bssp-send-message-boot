package com.powerbridge.bssp.edi.xml.prod;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.powerbridge.bssp.common.util.UUIDGenerator;
import com.powerbridge.bssp.common.util.toolbox.DateUtil;
import com.powerbridge.bssp.edi.entity.EdiMessageLog;
import com.powerbridge.bssp.edi.entity.EdiSendQueue;
import com.powerbridge.bssp.edi.service.IEdiMessageLogService;
import com.powerbridge.bssp.edi.service.IEdiSendQueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @ClassName EdiSendTask
 * @Description 发送报文
 * @author Simon.xie
 * @Date 2017年5月23日 上午11:31:34
 * @version 1.0.0
 */
@Component
public class EdiSendTask{
    static final Log logger = LogFactory.getLog(EdiSendTask.class);
    //报文队列服务
    @Autowired
    private IEdiSendQueueService ediSendQueueService;
    //报文日志服务
    @Autowired
    private IEdiMessageLogService ediMessageLogService;

    @Autowired
    private EtArcrpBscGroupProcesser etArcrpBscGroupProcesser;
    @Autowired
    private EmsPutrecBscGroupProcesser emsPutrecBscGroupProcesser;
    @Autowired
    private EmsBwsBscGroupProcesser emsBwsBscGroupProcesser;
    @Autowired
    private SasPassportBscGroupProcesser sasPassportBscGroupProcesser;
    @Autowired
    private InvBscGroupProcesser invBscGroupProcesser;
    @Autowired
    private DcrChgoffBscGroupProcesser dcrChgoffBscGroupProcesser;
    @Autowired
    private SasVehicleBscGroupProcesser sasVehicleBscGroupProcesser;
    @Autowired
    private SasDclBscGroupProcesser sasDclBscGroupProcesser;
    @Autowired
    private SasStockBscGroupProcesser sasStockBscGroupProcesser;

    public void job(){
       /* HttpClientUtil httpClientUtil = new HttpClientUtil();
        String Str = httpClientUtil.executeByGET("http://localhost:8090/bssp-admin/edi/ediSendQueue/emsList");
        JSONObject jsonObject=(JSONObject) JSONObject.parse(Str);
        String data = jsonObject.get("data").toString();
        Gson gson = new Gson();
        List<EdiSendQueue> ediSendQueueList = gson.fromJson(data.toString(), new TypeToken<List<EdiSendQueue>>() {
        }.getType());*/

        //1.查询所有Status为“1”要处理的队列表数据
        EntityWrapper entityWrapper = new EntityWrapper<EdiSendQueue>();
        entityWrapper.eq("Status", "N");//处理标识
        logger.info("ediSendQueueService："+ediSendQueueService);
        List<EdiSendQueue> ediSendQueueList = ediSendQueueService.selectList(entityWrapper);

        int ediSendQueueListSize = ediSendQueueList.size();
        for (int i = 0; i < ediSendQueueListSize; i++) {
            String areaCode = ediSendQueueList.get(i).getAreaCode();  //监管场所
            String docType = ediSendQueueList.get(i).getDocType();  //单据类型
            String bizType = ediSendQueueList.get(i).getBizType();  //业务类型
            String seqNo = ediSendQueueList.get(i).getSeqNo();      //单据编号
            BigDecimal chgTmsCnt = ediSendQueueList.get(i).getChgTmsCnt();  //变更次数

            //final IGroupProcesser processer = SendMessageFactory.createGroupProcesser(docType,bizType);
            switch (bizType) {
                case SendMessageGroupKindEnum.ET_ARCRP_BSC_BIZTYPE:  //EMS101
                    logger.info("----------------------企业档案库--------------------");
                    // 企业档案库报文处理方式
                    etArcrpBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.EMS_PUTREC_BSC_BIZTYPE://EMS111
                    logger.info("----------------------加工帐册备案--------------------");
                    // 加工帐册备案,加贸账册
                    emsPutrecBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.EMS_BWS_BSC_BIZTYPE: //BWS101
                    logger.info("----------------------物流账册--------------------");
                    // 物流账册
                    emsBwsBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.SAS_PASSPORT_BSC_BIZTYPE: //SAS121
                    logger.info("----------------------核放单--------------------");
                    sasPassportBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.INV_BSC_BS_BIZTYPE:   //inv101
                    logger.info("----------------------保税清单--------------------");
                    //保税清单
                    invBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.DCR_CHGOFF_BSC_BIZTYPE:  //EMS121
                    logger.info("----------------------账册报核--------------------");
                    // 加贸账册报核,加工账册报核,物流账册报核
                    dcrChgoffBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.SAS_VEHICLE_BSC_BIZTYPE:   ////SAS131
                    logger.info("----------------------车辆备案--------------------");
                    // 车辆备案
                    sasVehicleBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.SAS_DCL_BSC_BIZTYPE:   ////SAS101
                    logger.info("----------------------加工业务申请表,物流业务申请表--------------------");
                    sasDclBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
                case SendMessageGroupKindEnum.SAS_STOCK_BSC_BIZTYPE:   ////SAS111
                    logger.info("----------------------加工出入库单,物流出入库单--------------------");
                    sasStockBscGroupProcesser.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);
                    break;
            }



           /* if(processer==null){
                inserEdiMessageLog(docType,bizType,areaCode,"",null,"请检查单据类型和业务类型是否正确!",TransTypeEnum.EDI_ASSEMBLE_ERROR,"");
                UpdateEdiSendQueue(docType,bizType,seqNo,areaCode);
                continue;
            }
            processer.execute(docType,bizType,areaCode,seqNo,chgTmsCnt);*/
        }
    }
    /**
     * @Description 更新队列表
     * @param seqNo
     */
    public void UpdateEdiSendQueue(String docType,String bizType,String seqNo,String areaCode) {
        EntityWrapper entityWrapper = new EntityWrapper<EdiSendQueue>();
        entityWrapper.eq("AREA_CODE", areaCode);//处理标识
        entityWrapper.eq("DOC_TYPE", docType);//处理标识
        entityWrapper.eq("BIZ_TYPE", bizType);//处理标识
        entityWrapper.eq("SEQ_NO", seqNo);//处理标识
        EdiSendQueue ediSendQueue  = new EdiSendQueue();
        ediSendQueue.setStatus(TransTypeEnum.ONE);
        ediSendQueue.setUpdateTime(DateUtil.now());
        ediSendQueueService.update(ediSendQueue,entityWrapper);
    }

    public void inserEdiMessageLog(String docType, String bizType, String areaCode,String fileName,byte[] msg,String checkInfo,String status,String messageDataUid) {
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
