package hc.com.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String path = "https://10.url.cn/eth/ajNVdqHZLLAxibwnrOxXSzIxA76ichutwMCcOpA45xjiapneMZsib7eY4wUxF6XDmL2FmZEVYsf86iaw/ ";
    private ImageView imageView_okhttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView_okhttp = (ImageView) findViewById(R.id.imageView_okhttp);

    }

    public void Picture_okhttp_bt(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //采用链式调用发，只有okhttp3能用
                //创建client
                OkHttpClient client = new OkHttpClient.Builder().build();
                //创建请求对象，设置URL
                Request request = new Request.Builder().url(path).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] bytes = response.body().bytes();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView_okhttp.setImageBitmap(bitmap);
                            }
                        });
                    }
                });
            }
        }.start();
    }

    /**
     * 当按钮点击时，执行使用OkHttp上传图片到服务器的
     * 注意：有时候上传图片失败，是服务器规定还要上传一个key,如果开发中关于网络这一块处理问题，就多和web哥们交流
     */
    public void uploading(View view) {
        //图片上传的接口地址
        String url = "http://123.206.14.104:8080/FileUploadDemo/FileUploadServlet";
        //创建上传文件的对象
        File file = new File(Environment.getExternalStorageDirectory(), "big.jpg");
        //创建RequestBody封装参数
        RequestBody fileBody=RequestBody.create(MediaType.parse("application/0ctet-stream"),file);
        //创建MultipartBody,给requestBody进行设置
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "big.jpg", fileBody)
                .build();
        //创建Okhttp对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //创建request对象
        Request request = new Request.Builder().url(url).post(multipartBody).build();
        //上传图片，到服务器反馈数据
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("ysg",string);
            }
        });

    }
}
