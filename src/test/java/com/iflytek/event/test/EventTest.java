package com.iflytek.event.test;

import com.iflytek.event.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created with idea
 * user:ztwu
 * date:2019/5/27
 * description
 */
public class EventTest extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    public void test(){
        userService.register("ztwu");
    }

}
