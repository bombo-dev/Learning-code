package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.ConditionalMyOnClass;
import com.bombo.configuration.EnableMyConfigurationProperties;
import com.bombo.configuration.MyAutoConfiguration;
import com.bombo.configuration.MyConfigurationPropertiesImportSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@MyAutoConfiguration
@EnableMyConfigurationProperties(clazz = ServerProperties.class)
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfiguration {

    @Bean("tomcatWebServerFactory")
    @ConditionalOnMissingBean
    public ServletWebServerFactory servletWebServerFactory(ServerProperties properties) {
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        serverFactory.setContextPath(properties.getContextPath());
        serverFactory.setPort(properties.getPort());
        return serverFactory;
    }
}
