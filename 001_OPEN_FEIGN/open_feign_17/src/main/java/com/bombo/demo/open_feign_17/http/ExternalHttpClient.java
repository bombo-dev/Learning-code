package com.bombo.demo.open_feign_17.http;

import com.bombo.demo.open_feign_17.http.dto.request.ExternalSearchCondition;
import com.bombo.demo.open_feign_17.http.dto.response.ExternalModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "externalApi", url = "${apis.endpoint.external}")
public interface ExternalHttpClient {

    @GetMapping("/api/v1/externals/find")
    ExternalModel getExternals(@SpringQueryMap ExternalSearchCondition externalSearchCondition);
}
