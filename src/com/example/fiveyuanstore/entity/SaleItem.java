package com.example.fiveyuanstore.entity;

import java.io.Serializable;

/* 
 * 商品数量
 * 付款人数
 * 商品价格
 * 产品*/
public class SaleItem implements Serializable{
	
	Goods goods;
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	
}
