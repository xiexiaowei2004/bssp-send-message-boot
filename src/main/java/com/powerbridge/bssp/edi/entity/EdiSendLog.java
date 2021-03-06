package com.powerbridge.bssp.edi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 单据队列日志表
 * </p>
 *
 * @author simon.xie
 * @since 2017-05-20
 */
@TableName("edi_send_log")
public class EdiSendLog implements Serializable {
	@TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("UID")
	private String uid;
    /**
     * 监管场所
     */
	@TableField("AREA_CODE")
	private String areaCode;
    /**
     * 单据类型
     */
	@TableField("DOC_TYPE")
	private String docType;
    /**
     * 业务类型
     */
	@TableField("BIZ_TYPE")
	private String bizType;
    /**
     * 单据编号
     */
	@TableField("SEQ_NO")
	private String seqNo;
    /**
     * 处理标识
     */
	@TableField("STATUS")
	private String status;
    /**
     * 备注
     */
	@TableField("REMARKS")
	private String remarks;
    /**
     * 创建时间
     */
	@TableField("CREATE_TIME")
	private String createTime;
    /**
     * 修改时间
     */
	@TableField("UPDATE_TIME")
	private String updateTime;
	
	/**
     * 错误原因
     */
	@TableField("FAIL_NOTE")
	private byte[] failNote;
	
	@TableField(exist = false)
	private String failNoteStr;    
	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public byte[] getFailNote() {
		return failNote;
	}

	public void setFailNote(byte[] failNote) {
		this.failNote = failNote;
	}

	public String getFailNoteStr() {
		return failNoteStr;
	}

	public void setFailNoteStr(String failNoteStr) {
		this.failNoteStr = failNoteStr;
	}
}
