package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class CalculatorTest {

    @Test
    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        assertThat(calculator.number).isEqualTo(15)
    }

    @Test
    fun divideWithZero() {
        // given
        val calculator = Calculator(5)

        // when & then
        assertThatThrownBy { calculator.divide(0) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("0으로 나눌 수 없습니다.")
    }

    @Test
    fun divideWithZero2() {
        // given
        val calculator = Calculator(5)

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.message

        assertThat(message).isEqualTo("0으로 나눌 수 없습니다.");
    }

    @Test
    fun divideWithNotZero() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(1)

        // then
        assertThat(calculator.number).isEqualTo(5)
    }
}