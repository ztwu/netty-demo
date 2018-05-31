package com.iflytek.edu.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with Intellij IDEA.
 * User: ztwu2
 * Date: 2018/5/30
 * Time: 15:19
 * Description 非阻塞式网络通信框架 基于jdk nio类库
 */

public class PlainNioServer {

    public void serve(int port) throws IOException {

        //Channel是一个通道，可以通过它读取和写入数据，它就像自来水管一样，网络数据通过Channel读取和写入。
        // 通道与流的不同之处在于通道是双向的，流只是在一个方向上移动（一个流必须是InputStream或者OutputStream的子类），而且通道可以用于读、写或者同时用于读写。
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);                                            //1
        //多路复用器Selector，它是Java NIO编程的基础，熟练地掌握Selector对于掌握NIO编程至关重要。多路复用器提供选择已经就绪的任务的能力。简单来讲，Selector会不断地轮询注册在其上的Channel，
        // 如果某个Channel上面有新的TCP连接接入、读和写事件，这个Channel就处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作。
        //一个多路复用器Selector可以同时轮询多个Channel，由于JDK使用了epoll()代替传统的select实现，所以它并没有最大连接句柄1024/2048的限制。这也就意味着只需要一个线程负责Selector的轮询，就可以接入成千上万的客户端，这确实是个非常巨大的进步。
        Selector selector = Selector.open();                        //2
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);    //3

        //缓冲区（Buffer）的概念，Buffer是一个对象，它包含一些要写入或者要读出的数据。
        // 在NIO类库中加入Buffer对象，体现了新库与原I/O的一个重要区别。
        // 在面向流的I/O中，可以将数据直接写入或者将数据直接读到Stream对象中。
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (;;) {
            try {
                selector.select();                                    //4
            } catch (IOException ex) {
                ex.printStackTrace();
                // handle exception
                break;
            }
            //通过SelectionKey可以获取就绪Channel的集合
            Set<SelectionKey> readyKeys = selector.selectedKeys();    //5
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {                //6
                        ServerSocketChannel server =
                                (ServerSocketChannel)key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE |
                                SelectionKey.OP_READ, msg.duplicate());    //7
                        System.out.println(
                                "Accepted connection from " + client);
                    }
                    if (key.isWritable()) {                //8
                        SocketChannel client =
                                (SocketChannel)key.channel();
                        ByteBuffer buffer =
                                (ByteBuffer)key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {        //9
                                break;
                            }
                        }
                        client.close();                    //10
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // 在关闭时忽略
                    }
                }
            }
        }
    }
}
