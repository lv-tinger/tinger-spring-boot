package org.tinger.data.test.repo;

import org.springframework.stereotype.Component;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.anno.TingerRepository;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Operation;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.core.tsql.TingerQueryable;
import org.tinger.data.core.value.TingerDriver;
import org.tinger.data.jdbc.repository.JdbcSingletRepository;
import org.tinger.data.test.pojo.User;

import java.util.List;

@Component
@TingerRepository(driver = TingerDriver.MYSQL)
@TingerDataSource("tinger")
@TingerDatabase("tinger")
@TingerDataTable("user")
public class UserRepository extends JdbcSingletRepository<User, Long> {
    private final Queryable queryable = new UserQueryable("test") {
        @Override
        public Criteria where() {
            return Criteria.where("id", Operation.LE, 0);
        }
    };

    public List<User> selectLeId(Long id) {
        return this.select(this.queryable, id);
    }

    private class UserQueryable extends TingerQueryable {
        public UserQueryable(String name) {
            super(name, metadata);
        }
    }
}