package cn.mcdev.bephttputils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * headerParamsMap 保存请求头参数
 */
public class BepHttpUtils {
    private URL mUrl = null;
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
    public BepHttpUtils(URL url) {
        mUrl = url;
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
//            Log.d("EncodedPostParams", params);

            return params.getBytes("utf-8");
        }
    }

    public void get(BaseHandler baseHandler) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(connectTimeoutMillis);
            conn.setReadTimeout(readTimeoutMillis);

            setRequestHeaderParams(conn);
            conn.connect();

            baseHandler.handleConnection(conn);
        } catch (IOException e) {
            baseHandler.onFailure(e.toString());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    /**
     * @param postParamsMap 保存POST提交的参数信息
     * @param baseHandler
     */
    public void post(Map<String, String> postParamsMap, BaseHandler baseHandler) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(connectTimeoutMillis);
            conn.setReadTimeout(readTimeoutMillis);

            setRequestHeaderParams(conn);
            byte[] content = encodePostParams(postParamsMap);

            Log.d("EncodedPostParams", new String(content, "utf-8").toString());

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(content.length));
//            conn.setFixedLengthStreamingMode(content.length);
            conn.connect();

            OutputStream os = conn.getOutputStream();
            os.write(content);
            os.flush();
            os.close();

            baseHandler.handleConnection(conn);

        } catch (IOException e) {
            baseHandler.onFailure(e.toString());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }
}
