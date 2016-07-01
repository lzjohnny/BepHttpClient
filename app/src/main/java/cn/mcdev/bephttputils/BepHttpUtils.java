package cn.mcdev.bephttputils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BepHttpUtils {
    private URL mUrl = null;
//    private HttpURLConnection mConnection;

    private int connectTimeoutMillis = 8000;
    private int readTimeoutMillis = 8000;

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
    }

    public void get(BaseHandler baseHandler) {
        try {
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(connectTimeoutMillis);
            conn.setReadTimeout(readTimeoutMillis);
            conn.connect();

            baseHandler.handleConnection(conn);
        } catch (IOException e) {
            baseHandler.onFailure(e.toString());
        }
    }

    public void post() {

    }
}
