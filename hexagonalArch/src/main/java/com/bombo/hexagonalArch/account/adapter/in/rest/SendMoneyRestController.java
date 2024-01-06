package com.bombo.hexagonalArch.account.adapter.in.rest;

import com.bombo.hexagonalArch.account.adapter.in.dto.SendMoneyRequest;
import com.bombo.hexagonalArch.account.application.port.in.SendMoneyUseCase;
import com.bombo.hexagonalArch.account.application.service.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@RestController
public class SendMoneyRestController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping("/send")
    public ResponseEntity<AccountResponse> sendMoney(@RequestBody @Validated SendMoneyRequest request) {
        return ResponseEntity.ok(sendMoneyUseCase.sendMoney(request.toService()));
    }
}
