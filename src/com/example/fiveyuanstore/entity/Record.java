package com.example.fiveyuanstore.entity;

import java.io.Serializable;

public class Record extends DateRecord implements Serializable{

	int id;
	String state;
	Float money;
	int buyer_id;
	
	
	public int getBuy_id() {
		return buyer_id;
	}
	public void setBuy_id(int buy_id) {
		this.buyer_id = buy_id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}
	
	
}
