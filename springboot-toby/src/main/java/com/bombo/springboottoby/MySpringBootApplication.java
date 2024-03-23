package com.bombo.springboottoby;

import com.bombo.configuration.DispatcherServletConfiguration;
import com.bombo.configuration.TomcatWebServerConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
@Import({DispatcherServletConfiguration.class, TomcatWebServerConfiguration.class})
public @interface MySpringBootApplication {
}
