package com.iflytek.rpc.consumer;

import com.iflytek.rpc.consumer.RpcConsumer;
import com.iflytek.rpc.producer.HelloService;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/13
 * description
 */
public class ClientBootstrap {

    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {

        RpcConsumer consumer = new RpcConsumer();
        // 创建一个代理对象
        HelloService service = (HelloService) consumer
                .createProxy(HelloService.class, providerName);
        for (; ; ) {
            Thread.sleep(1000);
            System.out.println(service.hello("are you ok ?"));
        }
    }
}
