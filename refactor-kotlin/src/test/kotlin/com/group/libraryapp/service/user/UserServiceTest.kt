package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService,
) {

    @DisplayName("유저를 저장한다.")
    @Test
    fun saveUser() {
        // given
        val request = UserCreateRequest("이름", null)

        // when
        userService.saveUser(request);

        // then
        val results = userRepository.findAll();
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("이름")
        assertThat(results[0].age).isNull()

        // Java 코드에 Annotation 이 없어서 오류 발생.
        // 코틀린에서는 명백한 Annotation 이 없는 Getter에 대해서는 NotNull 이라고 판단
        // 이런 타입을 플랫폼 타입이라 칭함. 해결을 하기 위해서는 Java 코드에 애노테이션을 붙여주거나
        // 자바 코드를 가져와서 사용하는 매퍼 타입을 따로 만들어서 사용을 해주어야 한다.
    }
}