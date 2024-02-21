package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {
    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
    }

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

    @DisplayName("저장된 유저들을 조회한다.")
    @Test
    fun getUsers() {
        // given
        userRepository.saveAll(listOf(
            User("이름", 20),
            User("이름2", null)))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name", "age")
            .containsExactlyInAnyOrder(
                tuple("이름", 20),
                tuple("이름2", null)
            )
    }

    @DisplayName("유저의 이름을 업데이트 한다.")
    @Test
    fun updateUserName() {
        // given
        val savedUser = userRepository.save(User("이름", 20))
        val userUpdateRequest = UserUpdateRequest(savedUser.id, "이름 변경!")

        // when
        userService.updateUserName(userUpdateRequest)
        val findUser = userRepository.findById(savedUser.id).get()

        // then
        assertThat(findUser).extracting("id", "name", "age")
            .containsExactlyInAnyOrder(savedUser.id, userUpdateRequest.name, savedUser.age)
    }

    @DisplayName("이름이 일치하는 유저를 삭제한다.")
    @Test
    fun deleteUser() {
        // given
        val savedUser = userRepository.save(User("이름", 20))

        // when
        userService.deleteUser(savedUser.name)

        val findUsers = userRepository.findAll()

        // then
        assertThat(findUsers).isEmpty()
    }
}