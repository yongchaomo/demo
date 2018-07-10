package com.dynamic.appliction.pojo.bean;

import java.io.Serializable;

public class MailBox implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer systemid;

	private String mailname;

	private String usdRange;

	private String title;

	private String content;

	private String country;

	private String sendingtime;

	private String creationtime;

	private String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSystemid() {
		return systemid;
	}

	public void setSystemid(Integer systemid) {
		this.systemid = systemid;
	}

	public String getMailname() {
		return mailname;
	}

	public void setMailname(String mailname) {
		this.mailname = mailname == null ? null : mailname.trim();
	}

	public String getUsdRange() {
		return usdRange;
	}

	public void setUsdRange(String usdRange) {
		this.usdRange = usdRange == null ? null : usdRange.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getSendingtime() {
		return sendingtime;
	}

	public void setSendingtime(String sendingtime) {
		this.sendingtime = sendingtime;
	}

	public String getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}