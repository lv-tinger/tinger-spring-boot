package org.tinger.data.test.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinger.data.test.pojo.User;
import org.tinger.data.test.repo.UserRepository;

import java.util.Date;

@Component
public class UserServer {
    @Autowired
    private UserRepository userRepository;

    public User add(long userId) {
        User user = new User();
        user.setId(userId);
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        return userRepository.create(user) > 0 ? user : null;
    }
}
