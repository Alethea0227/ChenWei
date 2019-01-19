package com.chenwei.java.study.socket;

import com.chenwei.java.study.socket.server.SocketServerStarter;

/**
 * @author chenwei
 * @date 2019-01-19 10:34
 **/
public class ServerTest {
    public static void main(String[] args) {
        new SocketServerStarter(8888).start();
    }
}
