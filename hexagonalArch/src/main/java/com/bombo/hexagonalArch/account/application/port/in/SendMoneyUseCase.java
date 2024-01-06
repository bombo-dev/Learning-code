package com.bombo.hexagonalArch.account.application.port.in;

import com.bombo.hexagonalArch.account.application.service.dto.request.FindAccountServiceRequest;
import com.bombo.hexagonalArch.account.application.service.dto.request.SendMoneyServiceRequest;
import com.bombo.hexagonalArch.account.application.service.dto.response.AccountResponse;

public interface SendMoneyUseCase {
    AccountResponse sendMoney(SendMoneyServiceRequest request);
}
