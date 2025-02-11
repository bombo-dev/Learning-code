package com.bombo.demo.external.store;

import com.bombo.demo.external.model.External;
import com.bombo.demo.external.model.ExternalSearchCondition;

public interface ExternalStore {

    void save(External external);

    External find(ExternalSearchCondition externalSearchCondition);

    External findById(Long id);

    External findByName(String name);
}
