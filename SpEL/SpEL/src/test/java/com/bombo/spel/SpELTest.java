package com.bombo.spel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

class SpELTest {

    @DisplayName("작은 따옴표가 없으면 예외가 발생한다.")
    @Test
    void not_single_quotation_marks() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

        Assertions.assertThatThrownBy(() -> {
                    spelExpressionParser.parseExpression("Hello, World!");
                }
        ).isInstanceOf(SpelParseException.class);
    }

    @DisplayName("작은 따옴표가 있으면 정상적으로 처리된다.")
    @Test
    void single_quotation_marks() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = (String) spelExpressionParser.parseExpression("'Hello, World!'").getValue();
        Assertions.assertThat(result).isEqualTo("Hello, World!");
    }

    @DisplayName("어떠한 객체의 메서드를 표현식에서 사용 할 수 있다.")
    @Test
    void use_object_method() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        int length = (int) spelExpressionParser.parseExpression("'Hello, World!'.length()").getValue();
        Assertions.assertThat(length).isEqualTo(13);
    }

    @DisplayName("Dot notation으로 private field에 접근할 수 있다.")
    @Test
    void access_private_field() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = (String) spelExpressionParser.parseExpression("new com.bombo.spel.Foo('Hello').dummy").getValue();
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }

    @DisplayName("Dot Notation으로 객체 내의 객체에도 접근 할 수 있다.")
    @Test
    void access_inner_object() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = (String) spelExpressionParser.parseExpression("new com.bombo.spel.Foo2(new com.bombo.spel.Foo('Hello')).foo.dummy").getValue();
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }

    @DisplayName("Expression 객체의 getValue에 Class Type을 명시하면 캐스팅을 제거 할 수 있다.")
    @Test
    void expression_getValue_include_classType() {
        String expectedValue = "Hello";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = spelExpressionParser.parseExpression("new com.bombo.spel.Foo('Hello').dummy").getValue(String.class);
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
}
