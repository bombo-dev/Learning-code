package com.bombo.demo.external.configuration;

import com.bombo.demo.external.model.External;
import com.bombo.demo.external.model.ExternalType;
import com.bombo.demo.external.store.ExternalInMemoryStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ExternalInMemoryInitializer {

    private final ExternalInMemoryStore inMemoryStore;

    public ExternalInMemoryInitializer(ExternalInMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    @PostConstruct
    public void init() {
        inMemoryStore.save(new External(1L, "name1", ExternalType.REAL));
        inMemoryStore.save(new External(2L, "name2", ExternalType.FAKE));
        inMemoryStore.save(new External(3L, "name3", ExternalType.REAL));
        System.out.println("ExternalInMemoryInitializer initialized");
    }
}
