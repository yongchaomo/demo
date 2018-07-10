package com.dynamic.appliction.pojo.bean;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String email;

    private String password;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String birthDate;

    private String country;

    private String usdRange;

    private Boolean notus;

    private String code;

    private String recommend;

    private String sharingToken;

    private String state;// 用户状态

    private String confirmationToken;// 验证用户注册

    private String language;

    private String purseAddress;

    private String creationTime;

    private List<Logins> logins;// 登录历史

    private List<Auths> auths;// 第三方授权

    private List<Share> share;// 推广

    private List<TransactionType> transactionType;// 交易类型

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getUsdRange() {
        return usdRange;
    }

    public void setUsdRange(String usdRange) {
        this.usdRange = usdRange == null ? null : usdRange.trim();
    }

    public Boolean getNotus() {
        return notus;
    }

    public void setNotus(Boolean notus) {
        this.notus = notus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getSharingToken() {
        return sharingToken;
    }

    public void setSharingToken(String sharingToken) {
        this.sharingToken = sharingToken;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Logins> getLogins() {
        return logins;
    }

    public void setLogins(List<Logins> logins) {
        this.logins = logins;
    }

    public List<Auths> getAuths() {
        return auths;
    }

    public void setAuths(List<Auths> auths) {
        this.auths = auths;
    }

    public List<Share> getShare() {
        return share;
    }

    public void setShare(List<Share> share) {
        this.share = share;
    }

    public List<TransactionType> getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(List<TransactionType> transactionType) {
        this.transactionType = transactionType;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getPurseAddress() {
        return purseAddress;
    }

    public void setPurseAddress(String purseAddress) {
        this.purseAddress = purseAddress;
    }

}