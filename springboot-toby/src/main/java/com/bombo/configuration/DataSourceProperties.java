package com.bombo.configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MyConfigurationProperties(prefix = "data")
public class DataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
