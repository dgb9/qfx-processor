package com.ispnote.qfxprocessor;

import java.math.BigDecimal;
import java.util.Date;

public class QfxData {
	private String id; 
	private String name; 
	
	private String bankId; 
	private String accountId; 
	
	private BigDecimal amount; 
	
	private Date postedDate; 
	private String transactionType;
	
	public QfxData(){}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("QfxData [bankId=");
		builder.append(bankId);
		builder.append(", accountId=");
		builder.append(accountId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", postedDate=");
		builder.append(postedDate);
		builder.append(", transactionType=");
		builder.append(transactionType);
		builder.append("]");
		
		return builder.toString();
	}
	
}
