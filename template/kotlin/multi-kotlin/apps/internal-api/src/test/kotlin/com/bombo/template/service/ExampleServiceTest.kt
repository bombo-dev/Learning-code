package com.bombo.template.service

import com.bombo.template.domain.example.Example
import com.bombo.template.domain.example.ExampleRepository
import com.bombo.template.usecase.example.command.ExampleUpdateCommand
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExampleServiceTest {

    @Autowired
    private lateinit var exampleService: ExampleService

    @Autowired
    private lateinit var exampleRepository: ExampleRepository

    @Test
    fun update() {
        // given
        val savedId = exampleRepository.save(Example.newInstance("테스트 명"))
        val updateCommand = ExampleUpdateCommand(savedId, "수정된 테스트 명")

        // when
        exampleService.update(updateCommand)
        val sut = exampleRepository.findById(savedId)

        // then
        Assertions.assertThat(sut?.name).isEqualTo("수정된 테스트 명")
    }
}
