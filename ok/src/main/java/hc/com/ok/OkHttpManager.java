package hc.com.ok;

import android.os.Handler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static okhttp3.internal.Internal.instance;

/**
 * function: okhttp的中级封装，实现两个功能，从客户端下载数据；从客户端提交数据
 * 封装优秀的okhttp:OkhttpUtils,Okgo(更深入的封装，研究OKGO)
 * 1节约内存，使所有的网络请求都是用一个Okhttpclient和handler对象
 * 2解决OkHttp，网络请求成功，代码在子线程的问题，把请求成功后的逻辑代码放到主线程中执行
 * 3 简化代码
 * 这次封装用到哪些知识点？
 * 1.单例模式 2.handler 3.接口  4.Okhttp
 * 我们在使用单利模式时，构造方法一般权限私有，这样保证了对象的唯一性（EventBus，如果看源码的话，他的构造方法是public  所以一方面可以通过）
 * Created by YU on 2017/9/6.
 */

public class OkHttpManager {
    //1定义成员变量
    private OkHttpClient mclient;
    private static Handler mHandler;
    private volatile static OkHttpManager sManager;//防止多个线程同时访问，Volatile

    //2使用构造方法，完成初始化
    private OkHttpManager() {
        mclient = new OkHttpClient();
        mHandler = new Handler();
    }


    //3使用单例模式，通过获取的方式拿到对象

    public static OkHttpManager getsManager() {

        if (sManager == null) {
            synchronized (OkHttpManager.class) {
                if (instance == null) {
                    sManager = new OkHttpManager();
                }
            }
        }
        return sManager;
    }
    //4定义接口
    interface Func1 {
        void onResponse(String result);
    }

    interface Func2 {
        void onResponse(byte[] result);
    }

    interface Func3 {
        void onResponse(JSONObject jsonObject);
    }

    //5使用handler，接口，保证处理数据的逻辑在主线程、
    //处理请求网络成功的方法，返回结果是json字符串
    private static void onSuccessJsonStringMethod(final String jsonValue, final Func1 callBack) {
        //这里用的是mHandler.post方法，把数据放到主线程中，以后可以用EventBus或RxJava的线程调度器去完成
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {

                    try {
                        callBack.onResponse(jsonValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private static void OnSuccessJsonStringMethod(final byte[] bytes, final Func2 callBack) {
        //这里用的是mHandler.post方法，把数据放到主线程中，以后可以用EventBus或RxJava的线程调度器去完成
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {

                    try {
                        callBack.onResponse(bytes);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    //6暴露提供给外界调用的的方法

    /**
     * 请求制定的URL返回的结果是Json字符串
     */
    public void asyncJsonStringByURL(String url, final Func1 callBack) {
        Request request = new Request.Builder().url(url).build();
        mclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断response是否有对象，成功
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 提交表单
     */
    public void sendComplexForm(String url, Map<String, String> params, final Func1 callBack) {
        //表单对象
        FormBody.Builder builder = new FormBody.Builder();
        //键值非空判断
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody request_body = builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();
        mclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断response是否有对象，成功
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 图片下载
     */
    public void loadPicture(String url, final Func2 callBack) {
        Request request = new Request.Builder().url(url).build();
        mclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断response是否有对象，成功
                if (response != null && response.isSuccessful()) {
                    OnSuccessJsonStringMethod(response.body().bytes(), callBack);
                }
            }
        });
    }
}

