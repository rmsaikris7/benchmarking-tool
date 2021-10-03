package com.kafka.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class  BankTransaction {
	private Integer id;
	private Integer accountId;
	private String type;
	private BigDecimal ammount;
	private LocalDateTime transactionTime;
	
	public BankTransaction() {

	}
	
	public BankTransaction(Integer id, Integer accountId, String type, BigDecimal ammount, LocalDateTime transactionTime) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.type = type;
		this.ammount = ammount;
		this.transactionTime = transactionTime;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getAmmount() {
		return ammount;
	}
	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
}
