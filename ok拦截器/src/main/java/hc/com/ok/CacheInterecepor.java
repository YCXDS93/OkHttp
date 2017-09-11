package hc.com.ok;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * function:自定义的缓存拦截器；如果服务器没有给文件在响应头中定义缓存标签，
 * 那么我们在拦截器中手动的给响应头加上标签
 * 1.自定义一个类，实现Interceptor
 * 2.在interceptor方法中写自己的逻辑
 * 数据返回过来有响应头，封装网络工具类，他是根据拿到响应头中的缓存标签，来决定这一次请求的数据是否
 * 要进行缓存
 * Created by YU on 2017/9/7.
 */

public class CacheInterecepor {
    public Response intercept(Interceptor.Chain chain)throws IOException{
        //得到Response对象
        Response response=chain.proceed(chain.request());
        return response.newBuilder()
                //设置缓存标签，及60秒的时长
                .header("Cache-Control","max-age=60")
                .build();
    }
}
