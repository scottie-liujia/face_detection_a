/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo.utils;

import com.readwise.facedetectiondemo.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
