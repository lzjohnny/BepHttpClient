package cn.mcdev.bephttpclient;

/**
 * Created by ShiningForever on 2016/9/30.
 * 请求状态：START：接收到响应（onStart参数为响应实体首部字段集）
 *          PROCESS：正在接收数据
 *          FINISH：请求完成结束
 */
public class Status {
    public static final int START = 0;
    public static final int PROCESS = 1;
    public static final int FINISH = 2;
    public static final int SUCCESS = 3;
    public static final int FAILURE = 4;
}
