package bwie.com.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String path="http://publicobject.com/helloworld.tex";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void okHttp(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //注意采用的链式调用法，只有OkHttp3.0方可使用
                //创建client
                OkHttpClient client=new OkHttpClient.Builder().build();
                //创建请求对象
                Request request=new Request.Builder().url(path).build();
                //同步
                try {
                    Response response=client.newCall(request).execute();
                    String string = response.body().string();
                    Log.d("ysg",string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    //异步请求
    public void OkOnclick(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //缓存大小
                int cacheSize =10*1024*1024;//10m
                //创建Cache对象，文件存放到私有目录
                Cache cache = new Cache(getCacheDir(), cacheSize);
                //创建Cache对象，进行本地缓存的设置
                OkHttpClient okHttpClient=new OkHttpClient.Builder().cache(cache).build();
                Request build = new Request.Builder().url(path).build();
                //创建Call对象
                Call call=okHttpClient.newCall(build);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        
                    }
                });
            }
        }.start();
    }
}
