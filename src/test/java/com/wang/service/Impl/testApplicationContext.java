package com.wang.service.Impl;

import com.wang.BaseJunitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class testApplicationContext extends BaseJunitTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void test() {
        System.out.println(applicationContext.getBean("infoService1"));
        System.out.println(applicationContext.getBean("infoService2"));
        System.out.println(applicationContext.getBean("infoService2"));
    }

}
