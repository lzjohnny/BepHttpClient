package cn.mcdev.bephttpclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by ShiningForever on 2016/9/30.
 * IHandler与相应的Looper和MessagQueue均绑定在主线程中
 * 这样才可以在主线程中回调onSuccess、onFailure等方法
 */
public class IHandler extends Handler {
    BaseProcessor processor;
    public IHandler(Looper looper, BaseProcessor processor) {
        super(looper);
        this.processor = processor;
    }

    // handleMessage方法运行在主线程中
    // processor.onStart()等回调方法也运行在主线程中
    // 这样就将子线程BaseProcessor.handleConnection中的数据传递到主线程中
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Status.START:
                processor.onStart((ContentHeaderField) msg.obj);
                break;
            case Status.PROCESS:
                processor.onProcess();
                break;
            case Status.FINISH:
                processor.onFinish();
                break;
            case Status.SUCCESS:
                processor.onSuccess((byte[]) msg.obj);
                break;
            case Status.FAILURE:
                processor.onFailure((String) msg.obj);
                break;
        }
    }
}
