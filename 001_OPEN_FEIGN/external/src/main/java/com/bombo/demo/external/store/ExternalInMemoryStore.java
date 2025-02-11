package com.bombo.demo.external.store;

import com.bombo.demo.external.model.External;
import com.bombo.demo.external.model.ExternalSearchCondition;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ExternalInMemoryStore implements ExternalStore {

    private final Map<Long, External> store = new HashMap<>();

    @Override
    public void save(External external) {
        store.put(external.getId(), external);
    }

    @Override
    public External find(ExternalSearchCondition externalSearchCondition) {
        if (externalSearchCondition.getId() != null) {
            return findById(externalSearchCondition.getId());
        } else if (externalSearchCondition.getName() != null) {
            return findByName(externalSearchCondition.getName());
        }
        return null;
    }

    @Override
    public External findById(Long id) {
        return store.get(id);
    }

    @Override
    public External findByName(String name) {
        return store.values().stream()
                .filter(external -> name.equals(external.getName()))
                .findFirst()
                .orElse(null);
    }
}
