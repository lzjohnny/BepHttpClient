package cn.mcdev.bephttputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Button buttonGet = (Button) findViewById(R.id.button_get);
//        Button buttonPost = (Button) findViewById(R.id.button_post);
    }

    public void get(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:5000/signin");
                    BepHttpUtils httpUtils = new BepHttpUtils(url);
                    httpUtils.get(new TextHandler() {
                        @Override
                        public void onStart(ContentHeaderField field) {
                            Log.d("MainActivity", field.getContentType());
                            Log.d("MainActivity", Integer.toString(field.getContentLength()));
                        }

                        @Override
                        public void onProcess() {

                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onSuccess(String responseText) {
                            Log.d("MainActivity", responseText);
                        }

                        @Override
                        public void onFailure(String failInfo) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void post(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:5000/signin");
                    BepHttpUtils httpUtils = new BepHttpUtils(url);
                    httpUtils.addHeaderParams("User-Agent", "Mozilla/5.0");
//                    httpUtils.addHeaderParams("Connection", "keep-alive");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", "admin");
                    map.put("password", "admin");

                    httpUtils.post(map, new TextHandler() {
                        @Override
                        public void onStart(ContentHeaderField field) {
                            Log.d("MainActivity", field.getContentType());
                            Log.d("MainActivity", Integer.toString(field.getContentLength()));
                        }

                        @Override
                        public void onProcess() {

                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onSuccess(String responseText) {
                            Log.d("MainActivity", responseText);
                        }

                        @Override
                        public void onFailure(String failInfo) {
                            Log.d("Failure", failInfo);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}