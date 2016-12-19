package com.example.fiveyuanstore.entity;

import java.io.Serializable;

/* 
 * 商品数量
 * 付款人数
 * 商品价格
 * 产品*/
public class SaleItem implements Serializable{
	int goods_count,payNumber;
	float price;
	Goods goods;
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public int getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}
	public int getPayNumber() {
		return payNumber;
	}
	public void setPayNumber(int payNumber) {
		this.payNumber = payNumber;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
