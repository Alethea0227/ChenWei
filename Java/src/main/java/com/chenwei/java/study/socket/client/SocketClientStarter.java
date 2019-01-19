package com.chenwei.java.study.socket.client;

import com.chenwei.java.study.socket.common.AbstractStarter;

import java.io.IOException;
import java.net.Socket;

/**
 * @author chenwei
 * @date 2019-01-19 10:36
 **/
public class SocketClientStarter extends AbstractStarter {
    private String host;
    private int port;

    public SocketClientStarter(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 启动方法，需自行实现
     */
    @Override
    public void start() {
        try {
            Socket connection = new Socket(host, port);
            new SocketClientHandler(connection).handle();
        } catch (IOException e) {
            System.out.println("启动客户端失败，原因：" + e.getMessage());
        }
    }
}
