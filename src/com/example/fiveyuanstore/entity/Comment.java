package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment extends BaseEntity  implements Serializable{
	/*id
	 *商品id
	 *订单id
	 *内容
	 *创建时间 */
	String	 text, order_num;
	int
	order_id;
	User author;
	Goods goods;
	Date createDate, editDate;
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	
	
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
