package com.bombo.demo.external.model;

public enum ExternalType {
    REAL("진짜"),
    FAKE("가짜");

    private final String description;

    ExternalType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ExternalType fromName(String name) {
        for (ExternalType type : ExternalType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return ExternalType.FAKE;
    }
}
