package com.chenwei.java.study.socket.server;

import com.chenwei.java.study.socket.common.AbstractHandler;
import com.chenwei.java.study.socket.common.SocketUtil;

import java.net.Socket;

/**
 * @author chenwei
 * @date 2019-01-19 10:09
 **/
public class SocketServerHandler extends AbstractHandler implements Runnable {
    private Socket connection;

    public SocketServerHandler(Socket connection) {
        this.connection = connection;
    }

    /**
     * 处理方法，需自行实现
     */
    @Override
    public void handle() {
        try {
            //1. 获取输入流并拿到客户端传来的值
            String input = SocketUtil.readFromSocket(connection);
            //2. 解析收入值并自定义进行处理,
            processInput(input);
            //3. 获取输出流并想客户端返回内容,我这里直接把输入值返回
            SocketUtil.writeToSocket(connection, input);
            // 关闭资源
            SocketUtil.closeResource(connection);
        } catch (Exception e) {
            System.out.println("请求" + Thread.currentThread().getName() + "处理失败，原因：" + e.getMessage());
        }
    }

    /**
     * 处理Client端传来的值，需自定义处理方法
     */
    private void processInput(String input) {
        System.out.println("处理输入值");
    }

    @Override
    public void run() {
        this.handle();
        System.out.println("处理完成");
    }
}
