package com.dynamic.appliction.pojo.bean;

import java.io.Serializable;

public class Share implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer referralid;

	private Integer beintroducedid;

	private String creationTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferralid() {
		return referralid;
	}

	public void setReferralid(Integer referralid) {
		this.referralid = referralid;
	}

	public Integer getBeintroducedid() {
		return beintroducedid;
	}

	public void setBeintroducedid(Integer beintroducedid) {
		this.beintroducedid = beintroducedid;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
}