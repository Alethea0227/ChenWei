package com.chenwei.java.study.socket;

import com.chenwei.java.study.socket.client.SocketClientStarter;

/**
 * @author chenwei
 * @date 2019-01-19 10:43
 **/
public class ClientTest {
    public static void main(String[] args) {
        new SocketClientStarter("127.0.0.1", 8888).start();
    }
}
