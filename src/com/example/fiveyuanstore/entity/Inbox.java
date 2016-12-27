package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;



public class Inbox extends BaseEntity implements Serializable{

	String inboxContent;
	User send_user;
	User rec_user;
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


	
	
	public User getSend_user() {
		return send_user;
	}
	public void setSend_user(User send_user) {
		this.send_user = send_user;
	}
	public User getRec_user() {
		return rec_user;
	}
	public void setRec_user(User rec_user) {
		this.rec_user = rec_user;
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
