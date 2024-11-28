package com.bombo.template.controller.v1

import com.bombo.template.usecase.example.ExampleUseCase
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController(
    private val exampleUseCase: ExampleUseCase
) {
}
