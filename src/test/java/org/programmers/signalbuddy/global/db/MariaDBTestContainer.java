package org.programmers.signalbuddy.global.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    MariaDBContainer<?> MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:11.5");

    @BeforeAll
    static void setup() {
        MARIADB_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        MARIADB_CONTAINER.stop();
    }
}
