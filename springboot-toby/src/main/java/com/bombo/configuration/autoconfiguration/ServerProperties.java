package com.bombo.configuration.autoconfiguration;

import com.bombo.configuration.MyConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MyConfigurationProperties(prefix = "server")
public class ServerProperties {

    private String contextPath;

    private int port;
}
