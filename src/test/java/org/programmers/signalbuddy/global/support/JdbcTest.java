package org.programmers.signalbuddy.global.support;

import org.junit.jupiter.api.BeforeEach;
import org.programmers.signalbuddy.global.config.DataInitializer;
import org.programmers.signalbuddy.global.db.MariaDBTestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class JdbcTest implements MariaDBTestContainer {

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    void delete() {
        dataInitializer.deleteAll();
    }
}
