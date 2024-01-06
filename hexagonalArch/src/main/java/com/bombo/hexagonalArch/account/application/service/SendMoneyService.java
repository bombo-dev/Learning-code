package com.bombo.hexagonalArch.account.application.service;

import com.bombo.hexagonalArch.account.application.port.in.SendMoneyUseCase;
import com.bombo.hexagonalArch.account.application.port.out.AccountFindPort;
import com.bombo.hexagonalArch.account.application.service.dto.request.FindAccountServiceRequest;
import com.bombo.hexagonalArch.account.application.service.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendMoneyService implements SendMoneyUseCase {

    private final AccountFindPort accountFindPort;

    @Override
    public AccountResponse sendMoney(FindAccountServiceRequest request) {
        return null;
    }
}
