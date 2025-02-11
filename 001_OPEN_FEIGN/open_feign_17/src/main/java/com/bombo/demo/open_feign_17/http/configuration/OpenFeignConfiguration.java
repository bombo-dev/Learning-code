package com.bombo.demo.open_feign_17.http.configuration;

import com.bombo.demo.open_feign_17.http.HttpModule;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = HttpModule.class)
@Configuration
public class OpenFeignConfiguration {

}
