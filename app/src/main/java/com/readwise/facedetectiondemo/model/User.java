/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo.model;

public class User {

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    private String user_id;
    private double score;

}
