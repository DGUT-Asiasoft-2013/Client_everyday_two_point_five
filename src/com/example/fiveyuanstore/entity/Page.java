package com.example.fiveyuanstore.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page <T>{
	List <T> content;
	Integer number;
	
	public List <T> getContent() {
		return content;
	}
	public void setContent(List <T> content) {
		this.content = content;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Goods get(int position) {
		return (Goods) content.get(position);
	}
	
	public Inbox getInbox(int position) {
		return (Inbox) content.get(position);
	}
	
	public int size() {
		return content.size();
	}
	public void addAll(List<T> content2) {
		setContent(content2);
		
	}
	
}
