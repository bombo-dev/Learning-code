package com.bombo.demo.open_feign_17.api;

import com.bombo.demo.open_feign_17.http.ExternalHttpClient;
import com.bombo.demo.open_feign_17.http.dto.request.ExternalSearchCondition;
import com.bombo.demo.open_feign_17.http.dto.response.ExternalModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealController {

    private final ExternalHttpClient externalHttpClient;

    public RealController(ExternalHttpClient externalHttpClient) {
        this.externalHttpClient = externalHttpClient;
    }

    @GetMapping("/api/v1/reals")
    public ExternalModel getReals(ExternalSearchCondition externalSearchCondition) {
        return externalHttpClient.getExternals(externalSearchCondition);
    }
}
