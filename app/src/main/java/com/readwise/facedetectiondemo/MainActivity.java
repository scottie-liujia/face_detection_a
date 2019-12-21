/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readwise.facedetectiondemo.model.LoginResponse;
import com.readwise.facedetectiondemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Button mTrackBtn;
    private Button mAttrBtn;
    private Button mDetectBtn;
    private EditText mUserCodeET;
    private EditText mPasswordET;
    private Button mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addListener();
    }

    private void initView() {
        mTrackBtn = (Button) findViewById(R.id.track_btn);
        mAttrBtn = (Button) findViewById(R.id.attr_btn);
        mDetectBtn = (Button) findViewById(R.id.detect_btn);
        mUserCodeET = (EditText) findViewById(R.id.userCode_editText);
        mPasswordET = (EditText) findViewById(R.id.password_editText);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }

    private void addListener() {
        mTrackBtn.setOnClickListener(this);
        mAttrBtn.setOnClickListener(this);
        mDetectBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        switch (v.getId()) {
            case R.id.track_btn:
                Intent itTrack = new Intent(MainActivity.this, TrackActivity.class);
                startActivity(itTrack);
                break;
            case R.id.attr_btn:
                Intent itAttr = new Intent(MainActivity.this, AttrActivity.class);
                startActivity(itAttr);
                break;
            case R.id.detect_btn:
                // TODO 实时人脸检测
                Intent itDetect = new Intent(MainActivity.this, DetectActivity.class);
                startActivity(itDetect);
                break;
            case R.id.login_btn:
                login();
                break;
            default:
                break;
        }

    }
    private void login(){
        String url = "http://www.scottie-ai.com:8080/facedetection/Login";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("userCode", mUserCodeET.getText());
            json.put("password", mPasswordET.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //DialogUtils.showPopMsgInHandleThread(Release_Fragment.this.getContext(), mHandler, "数据获取失败，请重新尝试！");
                Log.d("scottie","onFailure:"+e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "通讯错误，请重试", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
//                Log.d("scottie",res);
                try {

                    JSONObject resJosn = new JSONObject(res);
                    String error_code = resJosn.getString("error_code");
                    LoginResponse loginResponse = new LoginResponse();
                    if (error_code.equals("0")){
                        Intent itTrack = new Intent(MainActivity.this, TrackActivity.class);
                        startActivity(itTrack);
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "用户密码错误", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return ;
    }
}
