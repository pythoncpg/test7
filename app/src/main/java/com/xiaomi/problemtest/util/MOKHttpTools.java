package com.xiaomi.problemtest.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MOKHttpTools {
    // 1、初始化okhttpClient对象
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private static final MediaType JSON =MediaType.parse("application/json;charset=utf-8");

    // 查询用户 没有就添加用户
    public String login(String loginUrl, String userAccount, String userPassword) {
        // 2、构建请求体requestBody
        try {
        JSONObject json = new JSONObject();
        json.put("user_count",userAccount);
        json.put("password",userPassword);
//        RequestBody requestBody = new FormBody.Builder()
//                .add("userAccount", userAccount)
//                .add("password", userPassword)
//                .build();
//        json.put("user_count",userAccount);
        // 3、发送请求，因为要传密码，所以用POST方式
        Request request = new Request.Builder()
                .url(loginUrl)
//                .post(requestBody)
                .post(RequestBody.create(JSON, String.valueOf(json)))
                .build();
        // 4、使用okhttpClient对象获取请求的回调方法，enqueue()方法代表异步执行

//                Response response = okHttpClient.newCall(request).execute();
            Response response =okHttpClient.newCall(request).execute();
            Log.d("TAG", "返回数据 :"+response);
//            Log.d("TAG", "返回数据body :"+response.body().string());
            Log.d("TAG", response.code()+"");
            //响应成功并返回响应内容
            if (response.code()==200) {
                return response.body().string();

                //此时代码执行在子线程，修改UI的操作使用handler跳转到UI线程
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //响应失败返回" "
        return " ";
    }

    // 根据用户名查询用户， 用户名不存在 用户存在就更新密码
    public String resetPassword(String loginUrl, String userAccount, String userPassword) {
        // 2、构建请求体requestBody
        RequestBody requestBody = new FormBody.Builder()
                .add("userAccount", userAccount)
                .add("password", userPassword)
                .build();
        // 3、发送请求，因为要传密码，所以用POST方式
        Request request = new Request.Builder()
                .url(loginUrl)
                .post(requestBody)
                .build();
        // 4、使用okhttpClient对象获取请求的回调方法，enqueue()方法代表异步执行
        try (Response response = okHttpClient.newCall(request).execute()) {
            //响应成功并返回响应内容
            if (response.isSuccessful()) {
                return response.body().string();
                //此时代码执行在子线程，修改UI的操作使用handler跳转到UI线程
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //响应失败返回" "
        return " ";
    }
    public static String uploadImage(String animalText,String animalSpeciesText,String wechatText,String adressText,String content, String url, String imagePath) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("imagePath", imagePath);
        File file = new File(imagePath);
        // 把文件封装进请求体
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        // MultipartBody 上传文件专用的请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("animal",animalText)
                .addFormDataPart("animalSpecies",animalSpeciesText)
                .addFormDataPart("wechat",wechatText)
                .addFormDataPart("adress",adressText)
                .addFormDataPart("content",content)
                .addFormDataPart("file", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
//        Response response = okHttpClient.newCall(request).execute();
//        JSONObject jsonObject = new JSONObject(response.body().string());
//        return jsonObject.optString("image");
        // 4、使用okhttpClient对象获取请求的回调方法，enqueue()方法代表异步执行
        try (Response response = okHttpClient.newCall(request).execute()) {
            //响应成功并返回响应内容
            if (response.isSuccessful()) {
                return response.body().string();
                //此时代码执行在子线程，修改UI的操作使用handler跳转到UI线程
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //响应失败返回" "
        return " ";
    }
}
