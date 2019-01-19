package com.chenwei.java.study.socket.server;

import com.chenwei.java.study.socket.common.AbstractStarter;
import com.chenwei.java.study.socket.common.ThreadPoolExecutorUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author chenwei
 * @date 2019-01-19 10:05
 **/
public class SocketServerStarter extends AbstractStarter {
    private int port;

    public SocketServerStarter(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server端已启动，端口：" + port);
            while (true) {
                try {
                    Socket connection = serverSocket.accept();
                    ThreadPoolExecutorUtil.submit(new SocketServerHandler(connection));
                    System.out.println("收到新请求，开始处理");
                } catch (IOException e) {
                    System.out.println("请求处理失败");
                }
            }
        } catch (Exception e) {
            System.out.println("启动Socket服务端失败，原因：" + e.getMessage());
        }
    }
}
