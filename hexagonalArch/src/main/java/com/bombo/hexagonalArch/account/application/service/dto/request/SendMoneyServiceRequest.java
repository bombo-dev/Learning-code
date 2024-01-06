package com.bombo.hexagonalArch.account.application.service.dto.request;

import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import com.bombo.hexagonalArch.account.domain.vo.Money;

public record SendMoneyServiceRequest(
        AccountId senderId,
        AccountId receiverId,
        Money amount
) {
}
