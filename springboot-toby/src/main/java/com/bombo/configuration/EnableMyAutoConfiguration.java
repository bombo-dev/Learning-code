package com.bombo.configuration;

import com.bombo.configuration.autoconfiguration.EnableSelectImporterAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EnableSelectImporterAutoConfiguration.class)
public @interface EnableMyAutoConfiguration {
}
