# 학습 목표
1. Java를 Kotlin으로 리팩토링하며 Kotlin 문법에 대한 이해도를 높인다.
2. Kotlin 사용 시 주의 해야 할 프레임워크 사용법에 대한 이해도를 높인다.
3. Kotlin Test를 Junit5로 작성해보고 이를, Kotest로 변경하여 Kotest 사용에 익숙해진다.

## 참고
https://www.inflearn.com/course/java-to-kotlin-2

## 생성자 프로퍼티 접근 제어
개발을 하다보면 private 접근제어를 사용하면서 getter는 열고 setter는 닫아주는 방식을 만들어보고 싶었다.
아무래도 자바를 쓰다보면 위 처럼 사용하는게 익숙한 것도 있다.

### 백킹 필드로 관리하기
코틀린의 컨벤션이라고 한다. 코드를 다음과 같이 작성 할 수 있다.

```kotlin
class Calculator(
    private var _number: Int
) {
    val number: Int
        get() = this._number
}
```

### 멤버변수를 클래스 내부에서 선언하기
생성자에서 바로 프로퍼티를 사용하지 않고 클래스 내부에서 초기화해주는 방식이다.
깔끔해보이지만, 사용하는 사람 입장에서 가독성이 떨어질 것 같다.
```kotlin
class Calculator(number: Int) {
    var number: Int = number
        private set
}
```

백킹 필드는 공식 컨벤션이라고 해도 후자보다 가독성이 더 떨어지는 측면이 있는 것 같다.
일단 후자의 방식을 택하고 회사의 컨벤션을 따라가자.

## 클래스 직접 명시
코틀린에서는 자바에 있는 클래스를 인자로 전달 할 때
`IllegalArgumentException.class` 처럼 전달해야 하는 것이 아니라, `IllegalArgumentException::class.java` 처럼 전달해주어야 한다.
따라서, 테스트 코드는 아래와 같이 된다.
```kotlin
assertThatThrownBy { calculator.divide(0) } // 매개변수가 없는 람다는 () 를 제거하여 표현 할 수 있다.
    .isInstanceOf(IllegalArgumentException::class.java)
    .hasMessage(message)
```
```kotlin
assertThrows<IllegalArgumentException> {
    calculator.divide(0)
}
```
위와 같이 표현도 가능하다.
