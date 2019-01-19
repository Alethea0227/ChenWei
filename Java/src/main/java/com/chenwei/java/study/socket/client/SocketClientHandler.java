package com.chenwei.java.study.socket.client;

import com.chenwei.java.study.socket.common.AbstractHandler;
import com.chenwei.java.study.socket.common.SocketUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * @author chenwei
 * @date 2019-01-19 10:36
 **/
public class SocketClientHandler extends AbstractHandler {
    private Socket connection;

    public SocketClientHandler(Socket connection) {
        this.connection = connection;
    }

    /**
     * 处理方法，需自行实现
     */
    @Override
    public void handle() {
        try {
            // 1.获取输出流并输出内容至服务端,内容可随意，我这里直接使用Test字符串
            SocketUtil.writeToSocket(connection, "Test");
            //2. 获取输入流并获取服务端返回内容
            String res = SocketUtil.readFromSocket(connection);
            //3. 解析返回内容并自定义进行处理,
            processRes(res);
            // 关闭资源
            SocketUtil.closeResource(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRes(String res) {
        System.out.println("处理返回值");
    }
}
