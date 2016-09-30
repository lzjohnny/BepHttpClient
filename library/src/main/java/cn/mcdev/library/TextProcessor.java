package cn.mcdev.library;

import java.io.UnsupportedEncodingException;

/**
 * Created by lzjohnny on 2016/6/22.
 *
 */
public abstract class TextProcessor extends BaseProcessor {
    private String mCharsetName = null;

    // 默认使用"utf-8"编码解析
    public TextProcessor() {
        this("utf-8");
    }

    public TextProcessor(String charsetName) {
        mCharsetName = charsetName;
    }

    //onSuccess参数为String
    public abstract void onSuccess(String responseText);

    @Override
    public void onSuccess(byte[] response) {
        try {
            String responseText = new String(response, mCharsetName);
            onSuccess(responseText);
        } catch (UnsupportedEncodingException e) {
            onFailure(e.toString());
        }
    }
}
