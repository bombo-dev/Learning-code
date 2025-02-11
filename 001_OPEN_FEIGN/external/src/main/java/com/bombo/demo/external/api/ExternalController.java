package com.bombo.demo.external.api;

import com.bombo.demo.external.model.External;
import com.bombo.demo.external.model.ExternalSearchCondition;
import com.bombo.demo.external.model.ExternalType;
import com.bombo.demo.external.store.ExternalStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalController {

    private final ExternalStore externalStore;

    public ExternalController(ExternalStore externalStore) {
        this.externalStore = externalStore;
    }

    @GetMapping("/api/v1/externals/find")
    public ExternalDto findExternal(ExternalSearchCondition condition) {
        return ExternalDto.from(externalStore.find(condition));
    }
}

record ExternalDto(
        Long id,
        String name,
        ExternalType type
) {

    public static ExternalDto from(External external) {
        return new ExternalDto(external.getId(), external.getName(), external.getType());
    }
}
