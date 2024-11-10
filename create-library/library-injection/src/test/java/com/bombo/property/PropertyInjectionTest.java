package com.bombo.property;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class PropertyInjectionTest {

    @Autowired
    private ApplicationContext applicationContext;

    @DisplayName("RedisProperties는 시작 시 속성 빈으로 등록한다.")
    @Test
    void readProperty() {
        // given
        // when
        RedisProperties property = applicationContext.getBean(RedisProperties.class);

        // then
        Assertions.assertThat(property).isNotNull();
    }
}
