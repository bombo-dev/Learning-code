package com.bombo.spel.demo;

import org.springframework.stereotype.Service;

@Service
public class InboundService {

    @CustomAnnotation(value = "#inbound.getBoxId")
    public void createInbound(Inbound inbound) {

    }
}
