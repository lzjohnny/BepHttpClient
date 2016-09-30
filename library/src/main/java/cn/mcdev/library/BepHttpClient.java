package cn.mcdev.library;

import android.os.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * headerParamsMap 保存请求头参数
 */
public class BepHttpClient {
    private String stringUrl = null;
    private URL realUrl = null;
    private Map<String, String> headerParamsMap = null;

    private int connectTimeoutMillis = 8000;
    private int readTimeoutMillis = 8000;

    //设置连接超时和读取超时
    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    //构造方法
    public BepHttpClient(String stringUrl) {
        this.stringUrl = stringUrl;
        headerParamsMap = new HashMap<>();
    }

    //增删首部信息
    public void addHeaderParams(String name, String value) {
        headerParamsMap.put(name, value);
    }

    public void removeHeaderParams(String name) {
        headerParamsMap.remove(name);
    }

    //将首部信息设置到HTTP请求中
    private HttpURLConnection setRequestHeaderParams(HttpURLConnection conn) {
        for (Map.Entry<String, String> entry : headerParamsMap.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return conn;
    }

    // POST提交的数据必须放在消息主体（entity-body)中，且必须经过application/x-www-form-urlencoded编码
    //格式：username=admin&password=admin，“=”和“&”不进行URL编码
    private byte[] encodePostParams(Map<String, String> postParamsMap) throws UnsupportedEncodingException {
        if (postParamsMap == null) {
            return new byte[0];
        } else {
            String params = null;
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : postParamsMap.entrySet()) {
                builder.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                builder.append("&");
            }
            //去掉末尾的'&'号
            if (builder.length() > 0) {
                params = builder.substring(0, builder.length() - 1);
            } else {
                params = builder.toString();
            }
            return params.getBytes("utf-8");
        }
    }

    public void get(BaseProcessor baseProcessor) {
        request(Method.GET, null, baseProcessor);
    }

    public void post(Map<String, String> postParamsMap, BaseProcessor baseProcessor) {
        request(Method.POST, postParamsMap, baseProcessor);
    }

    /**
     * request应当运行在子线程中，相应的，由request调用的方法也会运行在子线程中
     *
     * @param method
     * @param postParamsMap 保存POST提交的参数信息，GET方式下参数为NULL
     * @param baseProcessor
     */
    public void request(Method method, Map<String, String> postParamsMap, BaseProcessor baseProcessor) {
//        Log.d("BepHttpClient-request", Thread.currentThread().getName());
        HttpURLConnection conn = null;
        try {
            realUrl = new URL(stringUrl);
            // 不区分GET和POST的通用设置：连接超时、读取超时、请求头参数
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(connectTimeoutMillis);
            conn.setReadTimeout(readTimeoutMillis);
            setRequestHeaderParams(conn);

            if (method == Method.GET)
                conn.setRequestMethod("GET");
            else {
                // POST方式：为post数据编码，并写入connection输出流中
                conn.setRequestMethod("POST");
                byte[] content = encodePostParams(postParamsMap);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", Integer.toString(content.length));

                OutputStream os = conn.getOutputStream();
                os.write(content);
                os.flush();
                os.close();
            }

            baseProcessor.handleConnection(conn);
        } catch (IOException e) {
            //baseProcessor.onFailure(e.toString());
            baseProcessor.handler.sendMessage(Message.obtain(baseProcessor.handler, Status.FAILURE, e.toString()));
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }
}
