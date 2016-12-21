package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	/*id
	 *商品id
	 *订单id
	 *内容
	 *创建时间 */
	int id,
	goods_id,
	order_id;
	User author;
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	String content;
	Date createDate;
	public int getId() {
		return id;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
