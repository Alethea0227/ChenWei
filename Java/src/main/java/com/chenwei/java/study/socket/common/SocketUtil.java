package com.chenwei.java.study.socket.common;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author chenwei
 * @date 2019-01-15 16:48
 **/
public class SocketUtil {

    public static String readFromSocket(@NotNull Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public static String readFromSocket(@NotNull Socket socket, String type) throws IOException {
        InputStream inputStream = socket.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new byte[1024];
        while (inputStream.read(bytes) != -1) {
            stringBuilder.append(bytes);
            bytes = new byte[1024];
        }
        return stringBuilder.toString();
    }

    public static void writeToSocket(@NotNull Socket socket, @NotNull String data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();
    }

    public static void closeResource(@NotNull Socket socket) throws IOException {
        if (!socket.isInputShutdown()) {
            System.out.println("开始关闭输入流");
            socket.getInputStream().close();
        }
        if (!socket.isOutputShutdown()) {
            System.out.println("开始关闭输出流");
            socket.getOutputStream().close();
        }
        if (!socket.isClosed()) {
            System.out.println("开始关闭Socket");
            socket.close();
        }
    }
}
