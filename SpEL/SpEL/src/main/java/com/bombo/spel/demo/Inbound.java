package com.bombo.spel.demo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Inbound {

    private Long boxId;

    @Builder
    private Inbound(Long boxId) {
        this.boxId = boxId;
    }

    public static Inbound createInbound(Long boxId) {
        return Inbound.builder()
                .boxId(boxId)
                .build();
    }
}
