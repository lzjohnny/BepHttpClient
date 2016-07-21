package cn.mcdev.bephttputils;

/**
 * Created by ShiningForever on 2016/7/1.
 *
 */
public class ContentHeaderField {
    private int contentLength = -1;
    private String contentType = null;

    public ContentHeaderField() {
    }

    public ContentHeaderField(String contentType, int contentLength) {
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
