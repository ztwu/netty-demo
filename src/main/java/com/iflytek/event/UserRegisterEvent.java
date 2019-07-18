package com.iflytek.event;

import org.springframework.context.ApplicationEvent;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/27
 * description
 */
public class UserRegisterEvent extends ApplicationEvent {

    public UserRegisterEvent(String name) { //name即source
        super(name);
    }

}
