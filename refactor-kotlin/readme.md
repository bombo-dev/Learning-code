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

```kotlin
val message = assertThrows<IllegalArgumentException> {
    calculator.divide(0)
}.message

assertThat(message).isEqualTo("0으로 나눌 수 없습니다.");
```

위 처럼, 기존에 사용하던 Assertions 의 유사한 구문처럼 사용 할 수 있다.

## 자바의 플랫폼 타입

자바의 플랫폼 타입을 가져와서 테스트를 작성하는 경우를 주의하자.
예를 들어서, 다음과 같은 상황이 가능하다.

```kotlin
// given
val request = UserCreateRequest("이름", null)

// when
userService.saveUser(request);

// then
val results = userRepository.findAll();
assertThat(results).hasSize(1)
assertThat(results[0].name).isEqualTo("이름")
assertThat(results[0].age).isNull()
```
실제 User의 코드에는 각 필드에 대한 Getter 가 있는 상태이다.
코틀린은 프로퍼티를 호출하는 경우 자바 클래스에 대한 Getter를 보고 확인하는데, 이때 분명한 애노테이션이 없으면
코틀린은 NotNull로 판단하고 실행한다. 따라서, 명백한 애노테이션이 없다면 `age must be not null` 예외가 발생한다.

- 해결을 위해서는 플랫폼 타입을 매핑해주는 Mapper 클래스를 만들거나, 애노테이션을 명시하도록 하자.

## tuple
객체의 다중 컬럼을 조회할 때 extracting을 활용하곤 한다. 이때 tuple은 여러 결과를 조회하는데 용이하게 사용 될 수 있다.  
단일 결과가 나오는 것으로 예상되는 테스트에 대해서 tuple을 사용하면 예외가 발생한다.

# Kotlin Entity 생성
- Jpa Entity는 프록시로 생성이 되어진다. 그리고 기본 생성자를 요구한다.
- primary 생성자를 만들고 secondary 생성자가 primary 생성자를 오버라이딩 하는 방식을 택해야 한다. (하지만 너무 더럽..)
- 이를 해결해주기 위해 다음과 같은 플러그인을 추가해주어야 한다. `id 'org.jetbrains.kotlin.plugin.jpa' version '1.6.21'`
- 다음으로 리플렉션을 해주기 위한 의존성을 추가해주어야 한다. `implementation 'org.jetbrains.kotlin:kotlin-reflect:1.6.21'`