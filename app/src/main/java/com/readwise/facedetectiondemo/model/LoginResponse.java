/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangtianfei01 on 17/4/13.
 */

public class LoginResponse {

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String userCode;
    private String password;
}
