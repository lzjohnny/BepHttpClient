package cn.mcdev.bephttputils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by ShiningForever on 2016/6/22.
 * 按阶段分类：开始接收数据、进行、结束接收数据
 * 按结果分类：成功、失败
 */
public abstract class BaseHandler {
    public abstract void onStart(ContentHeaderField field);

    public abstract void onProcess();

    public abstract void onFinish();

    public abstract void onSuccess(byte[] response);

    public abstract void onFailure(String failInfo);

    public void handleConnection(HttpURLConnection conn) {
        try {
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                //http请求成功：
                String contentType = conn.getContentType();
                int contentLength = conn.getContentLength();
                ContentHeaderField field = new ContentHeaderField(contentType, contentLength);
                //开始：响应实体类型和长度
                onStart(field);

//                InputStream is = new BufferedInputStream(conn.getInputStream());
                InputStream is = conn.getInputStream();
                byte[] responseBytes = StreamToBytes(is);

                //成功：传入byte数组
                onSuccess(responseBytes);
//                handleOnSuccess(responseBytes);
            } else {
                //失败：响应码错误
                onFailure("Bad Response Code");
            }
        } catch (IOException e) {
            //失败：异常
            onFailure(e.toString());
        } finally {
            conn.disconnect();
            //结束：断开连接
            onFinish();
        }
    }

    protected byte[] StreamToBytes(InputStream is) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytesBuffer = new byte[2048];
        int lenRead;
        while ((lenRead = is.read(bytesBuffer, 0, bytesBuffer.length)) != -1) {
            bos.write(bytesBuffer, 0, lenRead);
        }
        bos.flush();
        bos.close();

        return bos.toByteArray();
    }

    //如果不借助这样一个中间方法，在BaseHandler中直接调用onSuccess，参数必须是byte数组。
    //那么TextHandler中onSuccess参数又必须是String，无法利用多态实现调用子类的不同onSuccess方法
    //onSuccess又是抽象方法，那么byte数组->String操作在handleOnSuccess中完成
    //子类重写handleOnSuccess即可
//    protected void handleOnSuccess(byte[] response) {
//        onSuccess(response);
//    }
}
