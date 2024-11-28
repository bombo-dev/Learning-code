package com.bombo.template

import com.bombo.template.domain.example.ExampleRepository
import com.bombo.template.service.ExampleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
abstract class IntegrationTestContainer {

    @Autowired
    protected lateinit var exampleService: ExampleService

    @Autowired
    protected lateinit var exampleRepository: ExampleRepository
}
