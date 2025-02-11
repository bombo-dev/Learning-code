package com.bombo.demo.open_feign_17.http.dto.request;

public class ExternalSearchCondition {
    private Long id;
    private String name;

    public ExternalSearchCondition(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
