package com.iflytek.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/27
 * description
 */
@Service // <1>
public class EmailService implements ApplicationListener<UserRegisterEvent> { // <2>

    //观察者监听，事件处理器

    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        System.out.println("邮件服务接到通知，给 " + userRegisterEvent.getSource() + " 发送邮件...");// <3>
    }

}
