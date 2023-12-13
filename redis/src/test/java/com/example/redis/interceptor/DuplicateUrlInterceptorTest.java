package com.example.redis.interceptor;

import com.example.redis.RedisIntegrationTest;
import com.example.redis.exception.DuplicateUrlException;
import com.example.redis.exception.ExceedUrlRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
class DuplicateUrlInterceptorTest extends RedisIntegrationTest {

    @Autowired
    private DuplicateUrlInterceptor interceptor;

    @DisplayName("따닥 요청을 하면 예외가 발생한다.")
    @Test
    void duplicateRequestUrl() {
        // given
        String id = "A7811";
        String url = "/api/v1/users";
        interceptor.interceptDuplicateRequest(id, url);

        // when & then
        DuplicateUrlException exception = assertThrows(
                DuplicateUrlException.class,
                () -> interceptor.interceptDuplicateRequest(id, url)
        );
        assertEquals("반복 요청이 들어왔습니다. 현재 요청 수 : 2", exception.getMessage());
    }

    @DisplayName("다른 주소를 요청하면 예외가 발생하지 않는다.")
    @Test
    void RequestAnotherUrl() {
        // given
        String id = "A7812";
        String url = "/api/v1/users";
        String otherUrl = "/api/v1/users/1";
        interceptor.interceptDuplicateRequest(id, url);

        // when & then
        assertThatCode(() -> interceptor.interceptDuplicateRequest(id, otherUrl))
                .doesNotThrowAnyException();
    }

    @DisplayName("반복된 주소를 시간이 지난 뒤 요청하면 예외가 발생하지 않는다.")
    @Test
    void duplicateRequestUrlAfterLimitTime() throws InterruptedException {
        // given
        String id = "A7813";
        String url = "/api/v1/users";
        interceptor.interceptDuplicateRequest(id, url);
        Thread.sleep(3000);

        // when & then
        assertThatCode(() -> interceptor.interceptDuplicateRequest(id, url))
                .doesNotThrowAnyException();
    }

    @DisplayName("5회 이상 반복하여 요청하면 블랙리스트에 추가된다.")
    @Test
    void exceedLimitDuplicateRequest() {
        // given
        String id = "A7814";
        String url = "/api/v1/users";

        for (int i = 0; i < 5; i++) {
            try {
                interceptor.interceptDuplicateRequest(id, url);
            } catch (DuplicateUrlException e) {

            }
        }

        // when
        assertThatThrownBy(() -> interceptor.interceptDuplicateRequest(id, url))
                .isInstanceOf(ExceedUrlRequestException.class)
                .hasMessage("반복된 요청이 짧은 시간 내에 반복되었습니다.");
    }
}