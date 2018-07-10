package com.dynamic.appliction.pojo.bean;

import java.io.Serializable;
import java.util.List;

public class TransactionType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer userId;

	private String type;

	private String describe;

	private String creationTime;

	private List<TransactionRecord> transactionRecord;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe == null ? null : describe.trim();
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public List<TransactionRecord> getTransactionRecord() {
		return transactionRecord;
	}

	public void setTransactionRecord(List<TransactionRecord> transactionRecord) {
		this.transactionRecord = transactionRecord;
	}

}