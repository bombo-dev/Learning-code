package com.bombo.springboottoby.learn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationTest {

    @Test
    void configurationWithManual() {

        MyConfig myConfig = new MyConfig();

        Assertions.assertNotSame(myConfig.bean1().common, myConfig.bean2().common);
    }

    @Test
    void configurationWithAuto() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(MyConfig.class);
        ac.refresh();

        Bean1 bean1 = ac.getBean(Bean1.class);
        Bean2 bean2 = ac.getBean(Bean2.class);

        // then
        Assertions.assertSame(bean1.common, bean2.common);
    }

    @Test
    void configurationWithManualProxy() {

        MyConfigProxy myConfigProxy = new MyConfigProxy();

        // then
        Assertions.assertSame(myConfigProxy.bean1().common, myConfigProxy.bean2().common);
    }

    static class MyConfigProxy extends MyConfig {
        private Common common;

        @Override
        Common common() {
            if (this.common == null) {
                this.common = super.common();
            }

            return this.common;
        }
    }

    @Configuration
    static class MyConfig {
        @Bean
        Common common() {
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }
    }

    static class Bean1 {
        private final Common common;

        public Bean1(Common common) {
            this.common = common;
        }
    }

    static class Bean2 {
        private final Common common;

        public Bean2(Common common) {
            this.common = common;
        }
    }

    // Bean1 <-- Common
    // Bean2 <-- Common

    static class Common {

    }
}
