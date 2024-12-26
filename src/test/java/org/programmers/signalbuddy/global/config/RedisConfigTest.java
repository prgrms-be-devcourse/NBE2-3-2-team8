package org.programmers.signalbuddy.global.config;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.global.db.RedisTestContainer;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisConfigTest extends ServiceTest implements RedisTestContainer {

    private static final Logger log = LoggerFactory.getLogger(RedisConfigTest.class);
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @DisplayName("redis 작동 확인")
    @Test
    void testRedisConnection() {

        // given
        String key = "key";
        String value = "value";
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

        String storedValue = valueOperations.get(key);
        assertThat(storedValue).isEqualTo(value);

        log.info("stored value: {}", storedValue);
        redisTemplate.delete(key);
    }
}
