package com.bombo.spel;

import com.bombo.spel.demo.PlaceOfBirth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

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

    @DisplayName("Literal Expression에 홑 따옴표를 사용 할 수 있다.")
    @Test
    void expression_special_character() {
        String expectedValue = "Bombo's test code!";

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        String result = spelExpressionParser.parseExpression("'Bombo''s test code!'").getValue(String.class);
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }

    @DisplayName("SimpleEvaluationContext의 forReadOnlyDataBinding을 사용하면 데이터 바인딩을 읽기 전용으로 설정 할 수 있다.")
    @Test
    void expression_direct_read_only_access() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        PlaceOfBirth placeOfBirth = new PlaceOfBirth("Seoul", "Korea");

        Assertions.assertThatThrownBy(() -> {
            spelExpressionParser.parseExpression("city").setValue(context, placeOfBirth, "Busan");
        }).isInstanceOf(SpelEvaluationException.class);

        Assertions.assertThat(placeOfBirth.getCity()).isEqualTo("Seoul");
    }

    @DisplayName("SimpleEvaluationContext의 forReadWriteDataBinding을 사용하면 데이터 바인딩을 읽기와 쓰기 전용으로 설정 할 수 있다.")
    @Test
    void expression_direct_all_access() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();

        PlaceOfBirth placeOfBirth = new PlaceOfBirth("Seoul", "Korea");

        spelExpressionParser.parseExpression("city").setValue(context, placeOfBirth, "Busan");

        Assertions.assertThat(placeOfBirth.getCity()).isEqualTo("Busan");
    }
}
