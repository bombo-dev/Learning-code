package com.bombo.configuration.autoconfiguration;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class EnableSelectImporterAutoConfiguration implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                "com.bombo.configuration.autoconfiguration.DispatcherServletConfiguration",
                "com.bombo.configuration.autoconfiguration.TomcatWebServerConfiguration",
        };
    }
}
