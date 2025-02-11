package com.bombo.demo.external.model;

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
