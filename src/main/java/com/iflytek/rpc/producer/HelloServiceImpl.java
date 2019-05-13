package com.iflytek.rpc.producer;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/13
 * description
 */
/**
 * 实现类
 */
public class HelloServiceImpl implements HelloService {
    public String hello(String msg) {
        return msg != null ? msg + " -----> I am fine." : "I am fine.";
    }
}