package org.programmers.signalbuddy.global.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.programmers.signalbuddy.global.db.MariaDBTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public abstract class ServiceTest extends MariaDBTestContainer {

}
