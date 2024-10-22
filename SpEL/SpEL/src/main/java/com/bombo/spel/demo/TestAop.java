package com.bombo.spel.demo;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAop {

    @Around("@annotation(com.bombo.spel.demo.CustomAnnotation)")
    public Object getValue(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CustomAnnotation valueAnnotation = method.getAnnotation(CustomAnnotation.class);
        String value = valueAnnotation.value();

        String parseValue = CustomParser.getValue(signature.getParameterNames(), joinPoint.getArgs(), value).toString();
        System.out.println("=========== parseValue : " + parseValue + " ============");
        return parseValue;
    }
}
