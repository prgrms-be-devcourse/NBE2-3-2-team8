package org.programmers.signalbuddy.global.support;

import org.junit.jupiter.api.BeforeEach;
import org.programmers.signalbuddy.global.config.DataInitializer;
import org.programmers.signalbuddy.global.config.TestQuerydslConfig;
import org.programmers.signalbuddy.global.db.MariaDBTestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestQuerydslConfig.class, DataInitializer.class})
public abstract class RepositoryTest implements MariaDBTestContainer {

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    void delete() {
        dataInitializer.clear();
    }
}
