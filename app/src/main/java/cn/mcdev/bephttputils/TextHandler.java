package cn.mcdev.bephttputils;

import android.util.Log;
import android.widget.BaseAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * Created by lzjohnny on 2016/6/22.
 *
 */
public abstract class TextHandler extends BaseHandler {
    private String mCharsetName = "utf-8";

    public TextHandler() {
    }

    public TextHandler(String charsetName) {
        mCharsetName = charsetName;
    }

    @Override
    public abstract void onStart(ContentHeaderField field);

    @Override
    public abstract void onProcess();

    @Override
    public abstract void onFinish();

    //onSuccess参数为String
    public abstract void onSuccess(String responseText);

    @Override
    public abstract void onFailure(String failInfo);

    @Override
    public void onSuccess(byte[] response) {
        try {
            String responseText = new String(response, mCharsetName);
            onSuccess(responseText);
        } catch (UnsupportedEncodingException e) {
            onFailure(e.toString());
        }
//        onSuccess(Integer.toString(response.length));
    }

//    @Override
//    protected void handleOnSuccess(byte[] response) {
//        String responseText = response.toString();
//        onSuccess(responseText);
//    }
}
