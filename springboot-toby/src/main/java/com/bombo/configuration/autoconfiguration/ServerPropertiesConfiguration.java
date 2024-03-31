package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.MyAutoConfiguration;
import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@MyAutoConfiguration
public class ServerPropertiesConfiguration {

    @Bean
    public ServerProperties serverProperties(Environment env) {
        ServerProperties serverProperties = new ServerProperties();

        serverProperties.setContextPath(env.getProperty("contextPath"));
        serverProperties.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("port"))));

        return serverProperties;
    }
}
