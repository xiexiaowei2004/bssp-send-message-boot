package com.powerbridge.bssp.inv.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.common.util.toolbox.StringUtil;
import com.powerbridge.bssp.inv.dao.InvDtMapper;
import com.powerbridge.bssp.inv.entity.InvDt;
import com.powerbridge.bssp.inv.service.IInvDtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目名称：bssp Maven Webapp
 * 类名称：BondInvtImgServiceImplX
 * 类描述：InvDt 表业务逻辑层接口 实现类
 * 创建时间：2017年5月17日 下午10:51:17
 */
@Service("invDtService")
public class InvDtServiceImpl extends BaseServiceImpl<InvDtMapper, InvDt> implements IInvDtService {

    @Autowired
    private InvDtMapper invDtMapper;

    /**
     * @param
     * @param
     * @return List<InvExg>
     * @throws
     * @Description:
     */
    public List<InvDt> autoGetInvtImgList(String seqNo) {

        return invDtMapper.selectByBondInvtImgList(seqNo);
    }

    /**
     * @param
     * @param
     * @return List<InvExg>
     * @throws
     * @Description:
     */
    public List<InvDt> handlerGetInvtImgList(String seqNo) {

        return invDtMapper.selectByHandlerInvtImgList(seqNo);
    }

    /**
     * @param
     * @param
     * @return String
     * @throws
     * @Description: 报关数据检查
     */
    public String checkDeclareData(String seqNo){
        String tempStr = "";
        List<InvDt> invDtList = invDtMapper.selectSameEntryGdsSeqNoList(seqNo);//检索存在两条以上的相同报关单商品序号
        for (InvDt invDt:invDtList){
           // List<InvDt> invDtData = invDtMapper.selectSameGdSNoData(invDt.getSeqNo(),invDt.getEntryGdsSeqno());//查询相同报关单商品序号记录
            Map<String,Object> columnMap = new HashMap<String,Object>();
            columnMap.put("SEQ_NO",seqNo);
            columnMap.put("ENTRY_GDS_SEQNO",invDt.getEntryGdsSeqno());
            List<InvDt> invDtData = invDtMapper.selectByMap(columnMap);
            for (int i=0;i<invDtData.size()-1;i++){
               for (int j=i+1;j<invDtData.size();j++){
                   if(StringUtil.isNotEmpty(invDtData.get(i).getGdecd())){
                       if(StringUtil.isEmpty(invDtData.get(i).getDclUnitcd()) || StringUtil.isEmpty(invDtData.get(j).getDclUnitcd())|| StringUtil.isEmpty(invDtData.get(i).getNatcd()) || StringUtil.isEmpty(invDtData.get(j).getNatcd()) || StringUtil.isEmpty(invDtData.get(i).getDclCurrcd())|| StringUtil.isEmpty(invDtData.get(j).getDclCurrcd()) || StringUtil.isEmpty(invDtData.get(i).getLvyrlfModecd())|| StringUtil.isEmpty(invDtData.get(j).getLvyrlfModecd())){
                            continue;
                       }
                       if(!invDtData.get(i).getGdecd().equals(invDtData.get(j).getGdecd())||!invDtData.get(i).getDclUnitcd().equals(invDtData.get(j).getDclUnitcd())
                               || !invDtData.get(i).getNatcd().equals(invDtData.get(j).getNatcd())|| !invDtData.get(i).getDclCurrcd().equals(invDtData.get(j).getDclCurrcd()) || !invDtData.get(i).getLvyrlfModecd().equals(invDtData.get(j).getLvyrlfModecd()))
                       {
                           tempStr += invDtData.get(i).getGdsSeqno()+":"+invDtData.get(j).getGdsSeqno()+",";
                           tempStr = tempStr.substring(0,tempStr.length()-1);
                       }
                   }

               }
            }
        }
        return tempStr;
    }

    /**
     * 获取最大的序号
     *
     * @return
     */
   public  BigDecimal getMaxSeqno(String putrecNo,String iEFlag,String mtpckEndprdMarkcd){
       BigDecimal maxSeqno = invDtMapper.getMaxSeqno(putrecNo,iEFlag,mtpckEndprdMarkcd);
       if (null == maxSeqno) {
           maxSeqno = BigDecimal.valueOf(0);
       }
       maxSeqno=maxSeqno.add(new BigDecimal(1));//取最大值加1
       return maxSeqno;
    }

  public  Page<InvDt> selectByInvDt(Page<InvDt>page, InvDt invDt){
         page.setRecords(invDtMapper.selectByInvDt(page,invDt));
         return page;
    }


}
