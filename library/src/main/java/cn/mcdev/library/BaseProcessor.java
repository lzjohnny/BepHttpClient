package cn.mcdev.library;

import android.os.Looper;
import android.os.Message;

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
public abstract class BaseProcessor {

    // 不可以直接调用以下5个回调方法，因为handleConnection运行在子线程中，而以下5个回调方法应当运行在主线程中
    // 需要回调时，应当先用handler发送消息，再由handler在主线程中调用相应的回调方法
    IHandler handler = new IHandler(Looper.getMainLooper(), this);
    public void onStart(ContentHeaderField field) {}
    public void onProcess() {}
    public void onFinish() {}
    public abstract void onSuccess(byte[] response);
    public abstract void onFailure(String failInfo);

    // 运行在子线程中
    public void handleConnection(HttpURLConnection conn) {
        try {
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                //http请求成功：
                String contentType = conn.getContentType();
                int contentLength = conn.getContentLength();
                ContentHeaderField field = new ContentHeaderField(contentType, contentLength);

                //开始：响应实体类型和长度
                //onStart(field);
                handler.sendMessage(Message.obtain(handler, Status.START, field));

                InputStream is = new BufferedInputStream(conn.getInputStream());
                byte[] responseBytes = StreamToBytes(is);

                //成功：传入byte数组
                //onSuccess(responseBytes);
                handler.sendMessage(Message.obtain(handler, Status.SUCCESS, responseBytes));

            } else {
                //失败：响应码错误
                //onFailure("Bad Response Code");
                handler.sendMessage(Message.obtain(handler, Status.FAILURE, "Bad Response Code"));

            }
        } catch (IOException e) {
            //失败：异常
            //onFailure(e.toString());
            handler.sendMessage(Message.obtain(handler, Status.FAILURE, e.toString()));

        } finally {
            conn.disconnect();
            //结束：断开连接
            //onFinish();
            handler.sendMessage(Message.obtain(handler, Status.FINISH));
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
}
