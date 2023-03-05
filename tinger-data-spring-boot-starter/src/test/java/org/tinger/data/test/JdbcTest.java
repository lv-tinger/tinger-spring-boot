package org.tinger.data.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tinger.data.test.pojo.User;
import org.tinger.data.test.repo.UserRepository;

import java.util.List;

public class JdbcTest {

    private final ApplicationContext context = new ClassPathXmlApplicationContext("DataTestApplication.xml");

    @Test
    public void test() {
        UserRepository repository = context.getBean(UserRepository.class);
        List<User> users = repository.selectLeId(3L);
        System.out.println(users.toString());
    }
}