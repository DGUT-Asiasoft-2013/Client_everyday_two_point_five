package com.example.fiveyuanstore.entity;

import java.io.Serializable;

public class Goods implements Serializable{

	/* 商品名称，
	 * 商品编号
	 * 图片*/
	String title;

	int id,user_id; 
	
	String goods_img, text;
	


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}




	
}
