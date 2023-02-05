package org.tinger.data.test.repo;

import org.springframework.stereotype.Component;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.anno.TingerRepository;
import org.tinger.data.core.value.TingerDriver;
import org.tinger.data.jdbc.repository.JdbcSingletRepository;
import org.tinger.data.test.pojo.User;

@Component
@TingerRepository(driver = TingerDriver.MYSQL)
@TingerDataSource("tinger")
@TingerDatabase("tinger")
@TingerDataTable("user")
public class UserRepository extends JdbcSingletRepository<User, Long> {

}
