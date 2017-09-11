package hc.com.ok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp底层网络请求是Socket,长连接
 */
public class MainActivity extends AppCompatActivity {
    private String Path = "http://publicobject.com/helloworld.txt";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView =  (TextView)findViewById(R.id.text);
    }
    /**
     * 根据点击事件使用Okhttp拦截器应用拦截
     */
    public void interceptor(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //在建立OkHttp对象时，传入拦截器对象
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
//                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor((Interceptor) new CacheInterecepor()).build();
                    Request request = new Request.Builder().url(Path).build();
                    Response response=client.newCall(request).execute();
                    String str=response.body().string();
                    Log.d("ysg",str);
//                    textView.setText(str);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    /**
     * 根据点击事件使用Okhttp，网络拦截器
     */
    public void interceptorNetWork(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //在建立OkHttplinet对象时，传入拦截器对象
                    //缓存拦截器：注意有两个，我们导包时用自己的
                    OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new LoggingInterceptor()).build();
//                    OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor((Interceptor) new CacheInterecepor()).build();
                    Request request = new Request.Builder().url(Path).build();
                    Response response=client.newCall(request).execute();
                    String str=response.body().string();
                    Log.d("ysg",str);
//                    textView.setText(str);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
