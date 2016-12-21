package com.example.fiveyuanstore.entity;

import java.io.Serializable;

/* 
 * 商品数量
 * 付款人数
 * 商品价格
 * 产品*/
public class SaleItem extends Goods implements Serializable{
	
	Goods goods;
	User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	
}
