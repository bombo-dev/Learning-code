package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.MyAutoConfiguration;
import com.bombo.configuration.MyConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

@MyAutoConfiguration
public class PropertyPostProcessorConfiguration {

    @Bean
    BeanPostProcessor propertyPostProcess(Environment env) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                MyConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(), MyConfigurationProperties.class);
                if (annotation == null) {
                    return bean;
                }

                return Binder.get(env).bindOrCreate("", bean.getClass());
            }
        };
    }
}
