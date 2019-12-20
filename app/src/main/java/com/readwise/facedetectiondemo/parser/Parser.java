/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo.parser;

import com.readwise.facedetectiondemo.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError;
}
