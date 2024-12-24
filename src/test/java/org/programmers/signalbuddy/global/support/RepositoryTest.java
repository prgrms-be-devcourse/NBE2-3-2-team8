package org.programmers.signalbuddy.global.support;

import org.programmers.signalbuddy.global.config.TestQuerydslConfig;
import org.programmers.signalbuddy.global.db.MariaDBTestContainer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestQuerydslConfig.class)
public abstract class RepositoryTest implements MariaDBTestContainer {

}
