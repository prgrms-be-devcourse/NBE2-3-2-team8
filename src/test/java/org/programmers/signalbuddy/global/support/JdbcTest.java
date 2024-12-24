package org.programmers.signalbuddy.global.support;

import org.programmers.signalbuddy.global.db.MariaDBTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class JdbcTest implements MariaDBTestContainer {

}
