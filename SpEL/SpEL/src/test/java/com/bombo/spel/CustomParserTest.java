package com.bombo.spel;

import com.bombo.spel.demo.Inbound;
import com.bombo.spel.demo.InboundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomParserTest {

    @Autowired
    private InboundService inboundService;

    @Test
    void testCreateInbound() {
        inboundService.createInbound(Inbound.createInbound(100L));
    }
}
