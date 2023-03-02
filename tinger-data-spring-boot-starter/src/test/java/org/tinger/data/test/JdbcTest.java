package org.tinger.data.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tinger.data.test.serv.UserServer;

public class JdbcTest {

    private ApplicationContext context = new ClassPathXmlApplicationContext("DataTestApplication.xml");

    @Test
    public void test() {
        UserServer userServer = context.getBean(UserServer.class);
        userServer.add(2);
    }
}