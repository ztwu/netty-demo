package com.iflytek.edu.servers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created with Intellij IDEA.
 * User: ztwu2
 * Date: 2018/5/31
 * Time: 9:29
 * Description 基于netty的nio实现http服务器
 */

public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 9999;
        new HttpServer(port).start();
    }

    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        System.out.println("initChannel ch:" + ch);
                        ch.pipeline()
                                //自定义编码器和解码器
                                //分别用于在数据流进来的时候将字节码转换为消息对象和数据流出去的时候将消息对象转换为字节码。
                                .addLast("decoder", new HttpRequestDecoder())   // 1
                                .addLast("encoder", new HttpResponseEncoder())  // 2
                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))    // 3
                                .addLast("handler", new HttpHandler());        // 4
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        b.bind(port).sync();
    }

}
