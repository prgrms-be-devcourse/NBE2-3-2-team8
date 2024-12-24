package org.programmers.signalbuddy.global.db;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public interface MariaDBTestContainer {

    @Container
    MariaDBContainer<?> MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:10.11")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");
}
