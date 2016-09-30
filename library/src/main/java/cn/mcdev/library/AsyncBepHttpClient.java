package cn.mcdev.library;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ShiningForever on 2016/9/28.
 */
public class AsyncBepHttpClient extends BepHttpClient {
    ExecutorService threadPool;
    public AsyncBepHttpClient(String stringUrl) {
        super(stringUrl);
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void get(final BaseProcessor baseProcessor) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                request(Method.GET, null, baseProcessor);
            }
        });
    }

    @Override
    public void post(final Map<String, String> postParamsMap, final BaseProcessor baseProcessor) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                request(Method.POST, postParamsMap, baseProcessor);
            }
        });
    }
}
