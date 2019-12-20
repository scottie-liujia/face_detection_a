/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.readwise.facedetectiondemo;


import com.readwise.facedetectiondemo.model.AccessToken;
import com.readwise.facedetectiondemo.model.User;
import com.readwise.facedetectiondemo.utils.DeviceUuidFactory;
import com.readwise.facedetectiondemo.utils.HttpUtil;
import com.readwise.facedetectiondemo.utils.OnResultListener;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
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

public class APIService {

    private static final String BASE_URL = "https://aip.baidubce.com";

    private static final String ACCESS_TOEKN_URL = BASE_URL + "/oauth/2.0/token?";

    private String accessToken;

    private String groupId;

    private APIService() {

    }

    private static volatile APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();


                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        // 采用deviceId分组
        HttpUtil.getInstance().init();
        DeviceUuidFactory.init(context);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 设置accessToken 如何获取 accessToken 详情见:
     *
     * @param accessToken accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 明文aksk获取token
     *
     * @param listener
     * @param context
     * @param ak
     * @param sk
     */
    public void initAccessTokenWithAkSk(final OnResultListener<AccessToken> listener, Context context, String ak,
                                        String sk) {

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(ak);
        sb.append("&client_secret=").append(sk);
        sb.append("&grant_type=client_credentials");
        HttpUtil.getInstance().getAccessToken(listener, ACCESS_TOEKN_URL, sb.toString());

    }
    public String search(final OnResultListener<User> listener, String face){
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search?access_token="+accessToken;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("image", face);
            json.put("image_type", "BASE64");
            json.put("group_id_list", "Demo_01");
            json.put("quality_control", "LOW");
            json.put("liveness_control", "NONE");
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
//                Log.d("scottie",res);
                try {

                    JSONObject resJosn = new JSONObject(res);
                    long error_code = resJosn.getLong("error_code");
                    User u = new User();
                    if (error_code == 0){
                        JSONObject user = (JSONObject)resJosn.getJSONObject("result").getJSONArray("user_list").get(0);
                        Double score = user.getDouble("score");
                        if(score>=80)
                            u.setUser_id(user.getString("user_id"));
                        else
                            u.setUser_id("人员未登记");
                        u.setScore(user.getDouble("score"));
                        Log.d("scottie","user_id:"+u.getUser_id()+"---------"+"score:"+u.getScore());
                        listener.onResult(u);
                    }else{
                        u.setUser_id("人员未登记");
                        listener.onResult(u);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return "ok";


    }

}
