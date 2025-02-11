package com.bombo.demo.external.model;

public class External {
    private Long id;
    private String name;
    private ExternalType type;

    public External(Long id, String name, ExternalType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public ExternalType getType() {
        return type;
    }
}
