package com.example.fiveyuanstore.entity;

import java.io.Serializable;
import java.util.Date;

public class Goods implements Serializable{

	/* 商品名称，
	 * 商品编号
	 * 图片*/
	String title;
	User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	int id, payNumber,goods_count; 
	String goods_img, text;

	Date createDate;
	Date editDate;
	float price;
	
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
