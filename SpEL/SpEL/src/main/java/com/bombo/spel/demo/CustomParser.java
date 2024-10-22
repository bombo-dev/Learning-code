package com.bombo.spel.demo;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomParser {

    private CustomParser() {

    }

    public static Object getValue(String[] parameterNames, Object[] args, String value) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            standardEvaluationContext.setVariable(parameterNames[i], args[i]);
        }

        return spelExpressionParser.parseExpression(value).getValue(standardEvaluationContext);
    }
}
