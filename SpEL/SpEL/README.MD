# SpEL (Spring Expression Language)

## 1. SpEL
SpEL은 런타임에 객체 그래프의 쿼리 및 조작을 지원하는 표현 언어입니다.
org.springframework.expression package 가 제공해준다.

ExpressionParser 인터페이스는 표현식 문자열을 구문 분석하는 역할을 합니다.
표현식 문자열은 작은 따옴표로 표시된 문자열 리터럴로 작성해야 한다.

```java
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'.concat('!')"); 
String message = (String) exp.getValue(); // 'Hello World!'
```

```java
    @DisplayName("작은 따옴표가 없으면 예외가 발생한다.")
    @Test
    public void not_single_quotation_marks() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

        Assertions.assertThatThrownBy(() -> {
                    spelExpressionParser.parseExpression("Hello, World!");
                }
        ).isInstanceOf(SpelParseException.class);
    }

    @DisplayName("작은 따옴표가 있으면 정상적으로 처리된다.")
    @Test
    public void single_quotation_marks() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

        Assertions.assertThat(spelExpressionParser.parseExpression("'Hello, World!'").getValue()).isEqualTo("Hello, World!");
    }

    @DisplayName("어떠한 객체의 메서드를 표현식에서 사용 할 수 있다.")
    @Test
    void use_object_method() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        int length = (int) spelExpressionParser.parseExpression("'Hello, World!'.length()").getValue();
        Assertions.assertThat(length).isEqualTo(13);
    }
```
![img.png](static/img/img.png)
SpEL은 dot notation (prop1.prop2.prop3)을 사용하여 중첩된 속성과 해당 속성 값 설정을 지원합니다.

해당 내용을 보고 객체를 직접 생성해봤으나 다음과 같은 오류를 마주하였습니다.
A problem occurred whilst attempting to construct an object of type 'Foo' using arguments '(java.lang.String)'

```java
    @DisplayName("Dot notation으로 private field에 접근할 수 있나?")
    @Test
    void access_private_field() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = (String) spelExpressionParser.parseExpression("new Foo('Hello').dummy").getValue(String.class);
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
```

getValue의 사용방식을 달리하여 객체를 생성 할 수 있었습니다.

```java
    @DisplayName("Dot notation으로 private field에 접근할 수 있나?")
    @Test
    void access_private_field() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Foo foo = new Foo("Hello");
        String result = (String) spelExpressionParser.parseExpression("dummy").getValue(foo);
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
```
위 처럼 작성하면 테스트가 정상적으로 통과하지만 동적으로 클래스를 생성하여 판단하는 등의 작업은 불가능합니다.

```java
    @DisplayName("Dot notation으로 private field에 접근할 수 있나?")
    @Test
    void access_private_field() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = (String) spelExpressionParser.parseExpression("new com.bombo.spel.Foo('Hello').dummy").getValue();
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
```
다음과 같이 풀 패키지명을 명시하니 해당 문제를 해결 할 수 있었습니다. 현재 위의 객체의 상태는 다음과 같습니다.
```java
public class Foo {
    private String dummy;

    public Foo(String dummy) {
        this.dummy = dummy;
    }
}
```
Property or field 'value' cannot be found on object of type 'com.bombo.spel.Foo' - maybe not public or not valid?
위와 같은 예외를 마주하였고, public 필드로 바꿔보겠습니다.

```java
public class Foo {
    public String dummy;

    public Foo(String dummy) {
        this.dummy = dummy;
    }
}
```
![img.png](static/img/img2.png)
테스트가 정상적으로 통과합니다.

하지만, 필드를 public으로 노출시키는 것은 좋지 않아 보입니다.
Java Bean 패턴에 따라 필드의 접근 제어자를 private로 수정하고, Getter를 만들어보겠습니다.

```java
    public class Foo {
        private String dummy;

        public Foo(String dummy) {
            this.dummy = dummy;
        }
    
        public String getDummy() {
            return dummy;
        }
    }
```
![img.png](static/img/img3.png)
SpEL은 Java Bean 표준을 준수하며, getter를 통해 필드에 접근할 수 있습니다.

추가적으로, 클래스 타입을 명시하여 캐스팅을 제거 할 수 있다고 하여 시도해봤습니다.
```java
    @DisplayName("Expression 객체의 getValue에 Class Type을 명시하면 캐스팅을 제거 할 수 있다.")
    @Test
    void expression_getValue_include_classType() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = spelExpressionParser.parseExpression("new com.bombo.spel.Foo('Hello').dummy").getValue(String.class);
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
```
![img.png](static/img/img4.png)
테스트가 통과되었습니다. 상황에 따라서 적절하게 형변환을 제거 할 수 있을 것 같습니다.
