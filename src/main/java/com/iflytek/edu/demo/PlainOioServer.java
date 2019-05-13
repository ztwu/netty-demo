package com.iflytek.edu.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with Intellij IDEA.
 * User: ztwu2
 * Date: 2018/5/30
 * Time: 15:16
 * Description 阻塞式网络通信框架，
 * 一个线程只能处理一个请求的情况，而机器能支持的最大线程数是有限的
 */

public class PlainOioServer {

    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);     //1
        try {
            for (;;) {
                //accept()接受一个客户端的连接请求，并返回一个新的套接字。
                // 所谓“新的”就是说这个套接字与socket()返回的用于监听和接受客户端的连接请求的套接字不是同一个套接字。与本次接受的客户端的通信是通过在这个新的套接字上发送和接收数据来完成的。
                //再次调用accept()可以接受下一个客户端的连接请求，并再次返回一个新的套接字（与socket()返回的套接字、之前accept()返回的套接字都不同的新的套接字）。
                // 这个新的套接字用于与这次接受的客户端之间的通信。
                final Socket clientSocket = socket.accept();    //2
                System.out.println("Accepted connection from " + clientSocket);

                new Thread(new Runnable() {                        //3
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("test".getBytes());                            //4
                            out.flush();
                            clientSocket.close();                //5
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                // ignore on close
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}