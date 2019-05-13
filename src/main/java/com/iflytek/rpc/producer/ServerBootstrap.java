package com.iflytek.rpc.producer;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/13
 * description
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("localhost", 8088);
    }
}
