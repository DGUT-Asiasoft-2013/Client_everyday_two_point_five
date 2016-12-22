package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment extends BaseEntity  implements Serializable{
	/*id
	 *商品id
	 *订单id
	 *内容
	 *创建时间 */
	String content;
	Date createDate;
	int
	order_id;
	User author;
	Goods goods;
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
