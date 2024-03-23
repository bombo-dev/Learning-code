package com.bombo.springboottoby.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hello(String message) {
        message = message == null ? "nullable Value" : message;
        return "Hello " + message;
    }
}
