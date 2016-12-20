package com.example.fiveyuanstore.entity;

import java.io.Serializable;

public class User implements Serializable  {
	/*
	 * 用户名
	 * 账号，
	 * 邮箱
	 * 图片*/
	String user_name,
		account,
		email,
		avatar;
	int id;
	float money;
	




	
	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public int getId() {
		return id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String name) {
		this.user_name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
	
}
