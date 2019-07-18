package com.iflytek.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/27
 * description
 */
@Service // <1>
public class UserService { // <2>

    public void register(String name) {
        System.out.println("用户：" + name + " 已注册！");
        applicationEventPublisher.publishEvent(new UserRegisterEvent(name));// <3>
    }

    //被观察者==发布者 观察者==订阅者
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher; // <2>

}