package cn.mcdev.bephttpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.mcdev.bephttpclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonGet = (Button) findViewById(R.id.button_get);
        Button buttonPost = (Button) findViewById(R.id.button_post);
        final TextView textView = (TextView) findViewById(R.id.text_view);

        final TextProcessor processor = new TextProcessor() {
            @Override
            public void onSuccess(String responseText) {
                // 该方法运行在主线程，可以操作UI
                Log.d("MainActivity-onSuccess", Thread.currentThread().getName());
                textView.setText(responseText);
            }

            @Override
            public void onFailure(String failInfo) {
                // 该方法运行在主线程，可以操作UI
                Log.d("MainActivity-onFailure", Thread.currentThread().getName());
                textView.setText(failInfo);
            }
        };

        final Map<String, String> postData = new HashMap<>();
        postData.put("username", "admin");
        postData.put("password", "admin");

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncBepHttpClient httpClient = new AsyncBepHttpClient("http://10.0.2.2:5000/signin");
                httpClient.get(processor);
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncBepHttpClient httpClient = new AsyncBepHttpClient("http://10.0.2.2:5000/signin");
                httpClient.post(postData, processor);
            }
        });

    }

}