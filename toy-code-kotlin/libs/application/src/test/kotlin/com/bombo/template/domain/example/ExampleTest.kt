package com.bombo.template.domain.example

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ExampleTest {

    @DisplayName("run 은 람다로 전달 된 행위를 하고 람다의 결과 값을 반환한다.")
    @Test
    fun run() {
        val example = Example.newInstance("test")

        val block = example.run {
            println(this.name) // this로 접근 가능하다. 인자가 T.() 확장 함수로 정의 되어 있다.
            this.name = "x" // run 또한 객체의 수정이 가능하지만, 반환 값이 람다의 결과 값이다.
            anyMethod("test")
        }

        val text = example.run {
            typeMethod("test")
        }

        // 반환 값 block()
        Assertions.assertThat(block.javaClass).isEqualTo(Unit::class.java)
        Assertions.assertThat(text.javaClass).isEqualTo(String::class.java)
    }

    @DisplayName("let은 람다로 전달 된 행위를 하고 람다의 결과 값을 반환한다.")
    @Test
    fun let() {
        val example = Example.newInstance("test")

        val block = example.let {
            println(it.name) // 인자로 받아서 it으로 접근 가능하다. (T) 인자를 받는다. 인자에 값이 있기에 it 접근.
            it.anyMethod("test")
        }

        // null Safe 하게 사용하는 경우 주로 사용.
        // 정말 non-null 이 보장이 되는 경우에만 run을 쓸 듯.
        val text = example.let {
            it.typeMethod("test")
        }

        // 반환 값 block(this) -> this는 전달된 T

        Assertions.assertThat(block.javaClass).isEqualTo(Unit::class.java)
        Assertions.assertThat(text.javaClass).isEqualTo(String::class.java)
    }

    @DisplayName("apply는 람다로 전달 된 행위를 하고 객체 자신을 반환한다.")
    @Test
    fun apply() {
        val example = Example.newInstance("test")

        val invokedExample = example.apply {
            println(this.name) // this로 접근 가능하다. 인자가 T.() 확장 함수로 정의 되어 있다.
            anyMethod("test")
        }

        val updatedExample = example.apply {
            typeMethod("test")
            this.name = "x" // apply 또한 객체의 수정이 가능하고, 반환 값이 객체 자신이다.
        }

        // 반환 값 this
        Assertions.assertThat(invokedExample).isEqualTo(example)
        Assertions.assertThat(updatedExample.name).isEqualTo("x")
    }

    @DisplayName("also는 람다로 전달 된 행위를 하고 객체 자신을 반환한다.")
    @Test
    fun also() {
        val example = Example.newInstance("test")

        val invokedExample = example.also {
            println(it.name) // 인자로 받아서 it으로 접근 가능하다. (T) 인자를 받는다. 인자에 값이 있기에 it 접근.
            it.anyMethod("test")
        }

        val updatedExample = example.also {
            it.typeMethod("test") // 반환 값 Unit인 람다를 필요로 하여 반환 값이 필요 없다는 의미이지만, 실제로 반환 값이 있더라도 무시한다.
            it.name = "x" // also 또한 객체의 수정이 가능하고, 반환 값이 객체 자신이다.
        }

        // 반환 값 this
        Assertions.assertThat(invokedExample).isEqualTo(example)
        Assertions.assertThat(updatedExample.name).isEqualTo("x")
    }

    @DisplayName("with는 람다로 전달 된 행위를 하고 람다의 결과 값을 반환한다.")
    @Test
    fun with() {
        val example = Example.newInstance("test")

        val block = with(example) {
            println(this.name) // this로 접근 가능하다. 인자가 T.() 확장 함수로 정의 되어 있다.
            this.name = "x" // with 또한 객체의 수정이 가능하지만, 반환 값이 람다의 결과 값이다.
            anyMethod("test")
        }

        val text = with(example) {
            typeMethod("test")
        }

        // run 과의 차이는 객체 자체의 확장 함수로 정의되어있는가 vs 인자로 받아서 접근하는가의 차이가 있다.
        // 반환 값 block(this) -> this는 전달된 T
        Assertions.assertThat(block.javaClass).isEqualTo(Unit::class.java)
        Assertions.assertThat(text.javaClass).isEqualTo(String::class.java)
    }
}
