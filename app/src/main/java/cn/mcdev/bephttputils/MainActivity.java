package cn.mcdev.bephttputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.tv);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2/updata.json");
                    BepHttpUtils httpUtils = new BepHttpUtils(url);
                    BaseHandler handler = new TextHandler() {
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
                                tv.setText(responseText);
                        }

                        @Override
                        public void onFailure(String failInfo) {

                        }
                    };
                    httpUtils.get(handler);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
