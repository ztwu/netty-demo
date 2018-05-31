package com.iflytek.edu.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created with Intellij IDEA.
 * User: ztwu2
 * Date: 2018/5/30
 * Time: 15:22
 * Description
 *
 Channel，表示一个连接，可以理解为每一个请求，就是一个Channel。
 ChannelHandler，核心处理业务就在这里，用于处理业务请求。
 ChannelHandlerContext，用于传输业务数据。
 ChannelPipeline，用于保存处理过程需要用到的ChannelHandler和ChannelHandlerContext。

 ByteBuf是一个存储字节的容器，最大特点就是使用方便，它既有自己的读索引和写索引，
 方便你对整段字节缓存进行读写，也支持get/set，方便你对其中每一个字节进行读写
 */

public class NettyOioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        //EventLoop 这个相当于一个处理线程，是Netty接收请求和处理IO请求的线程。
        //EventLoopGroup 可以理解为将多个EventLoop进行分组管理的一个类，是EventLoop的一个组。
        ///EventLoopGroup是一个线程组，它包含了一组nio线程，专门用于网络事件的处理，实际上他们就是Reactor线程组
        //这里创建2个的原因是一个用于服务端接受客户的连接，另一个用于SockentChannel的网络读写。
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();        //1

            b.group(group)                                    //2
                    //不指定channelFactory,就需要指定channel,channel初始化时会指定默认的channelFactory。
                    .channel(OioServerSocketChannel.class)
                    //指定channelFactory,就不需要指定channel了。
                    //.channelFactory(new ReflectiveChannelFactory<Channel>(NioSocketChannel.class))
                    .localAddress(new InetSocketAddress(port))
                    ////指定ChannelHandler
                    .childHandler(new ChannelInitializer<SocketChannel>() {//3
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            //ChannelPipeline 这是Netty处理请求的责任链，这是一个ChannelHandler的链表
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {            //4
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);//5
                                }
                            });
                        }
                    });
            //通过Bootstrap的connect方法来连接服务器端。该方法返回的也是ChannelFuture，
            //通过这个我们可以判断客户端是否连接成功,以便我们在连接成功之后,做一些其他的事情
            //在Netty中的所有的I/O操作都是异步执行的，这就意味着任何一个I/O操作会立刻返回，不保证在调用结束的时候操作会执行完成。
            // 因此，会返回一个ChannelFuture的实例，通过这个实例可以获取当前I/O操作的状态。
            ChannelFuture f = b.bind().sync();  //6
            //等待服务端监听端口关闭;
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();        //7
        }
    }
}