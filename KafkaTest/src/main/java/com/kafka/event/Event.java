package com.kafka.event;

import java.time.LocalDateTime;

public class Event {


	private Integer id;
	private String aggregateId;
	private String eventType;
	private LocalDateTime createdAt;
	private BankTransaction data;
	
	public Event() {
		
	}
	public Event(Integer id, String aggregateId, String eventType, LocalDateTime createdAt, BankTransaction data) {
		super();
		this.id = id;
		this.aggregateId = aggregateId;
		this.eventType = eventType;
		this.createdAt = createdAt;
		this.data = data;
	}
	
	public Event(String aggregateId, String eventType, LocalDateTime createdAt, BankTransaction data) {
		super();
		this.aggregateId = aggregateId;
		this.eventType = eventType;
		this.createdAt = createdAt;
		this.data = data;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAggregateId() {
		return aggregateId;
	}
	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public BankTransaction getData() {
		return data;
	}
	public void setData(BankTransaction data) {
		this.data = data;
	}
}
