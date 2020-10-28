package com.common.bean;


import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4227244327808705724L;
    private Integer userId;
    private String userName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
