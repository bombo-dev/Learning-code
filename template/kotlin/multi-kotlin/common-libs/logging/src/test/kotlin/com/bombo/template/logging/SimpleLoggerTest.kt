package com.bombo.template.logging

import org.junit.jupiter.api.Test

class SimpleLoggerTest {

    private val log = log()

    @Test
    fun givenMessageBlock_whenDebug_thenLogged() {
        // given & when & then
        log.debug { "hello debug block!" }
    }

    @Test
    fun givenMessageBlock_whenInfo_thenLogged() {
        // given & when & then
        System.getProperties().forEach { (key, value) -> log.info { "$key = $value" } }
        log.info { "hello info block!" }
    }

    @Test
    fun givenMessageBlock_whenWarn_thenLogged() {
        // given & when & then
        log.warn { "hello warn block!" }
    }

    @Test
    fun givenMessageBlock_whenError_thenLogged() {
        // given & when & then
        log.error { "hello error block!" }
    }
}
