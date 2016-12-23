package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;

public class Inbox extends BaseEntity implements Serializable{

	String inboxContent;
	String send_name;
	String rec_name;
	boolean isread;
	Date createDate;
	String sign;
	
	public String getInboxContent() {
		return inboxContent;
	}
	public void setInboxContent(String inboxContent) {
		this.inboxContent = inboxContent;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSend_name() {
		return send_name;
	}
	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}
	public String getRec_name() {
		return rec_name;
	}
	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}
	public boolean isIsread() {
		return isread;
	}
	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
