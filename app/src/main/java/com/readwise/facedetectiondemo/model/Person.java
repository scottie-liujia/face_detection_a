/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo.model;

public class Person {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    private String id;
    private String name;
    private String department;

}
