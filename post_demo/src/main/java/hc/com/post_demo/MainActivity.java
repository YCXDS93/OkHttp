package hc.com.post_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String path = "http://169.254.53.96:8080/web/LoginServlet";
    private EditText mEt_qq;
    private EditText mEt_pwd;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //控件初始化
        initView();
    }

    private void initView() {
        mEt_qq = (EditText) findViewById(R.id.et_qq);
        mEt_pwd = (EditText) findViewById(R.id.et_qq);
        textView = (TextView) findViewById(R.id.tv_status);

    }

    //使用Post进行表单上传，完成登录
    public void login(View view) {
        //得到用户输入信息，进行非空判断
        final String qq = mEt_qq.getText().toString().trim();
        final String pwd = mEt_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(qq) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(MainActivity.this, "不能输入为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建okhttpClinet对象
        new Thread() {
            @Override
            public void run() {
                super.run();
                //设置连接超时时间
                //创建okhttpClinet对象
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS) //设置连接超时时间
                        .readTimeout(10, TimeUnit.SECONDS)//读取超出时间
                        .writeTimeout(10, TimeUnit.SECONDS)//写入超出时间
                        .build();

                //表单对象
               FormBody formBody= new FormBody.Builder()
                        .add("qq", qq)
                        .add("pwd", pwd)
                        .build();
                //创建请求对象
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(path)
                        .build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String s=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(s);
                            }
                        });
                    }
                });
            }
        }.start();
    }
}
