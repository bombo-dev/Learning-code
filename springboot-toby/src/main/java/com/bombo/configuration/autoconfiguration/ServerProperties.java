package com.bombo.configuration.autoconfiguration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServerProperties {

    private String contextPath;

    private int port;
}
