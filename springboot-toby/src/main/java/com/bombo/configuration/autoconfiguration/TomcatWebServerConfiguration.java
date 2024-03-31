package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.ConditionalMyOnClass;
import com.bombo.configuration.MyAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfiguration {

    @Value("${contextPath}")
    String contextPath;

    @Bean("tomcatWebServerFactory")
    @ConditionalOnMissingBean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        serverFactory.setContextPath(contextPath);
        return serverFactory;
    }
}
