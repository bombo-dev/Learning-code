package com.bombo.hexagonalArch.account.adapter.in.dto;

import com.bombo.hexagonalArch.account.application.service.dto.request.SendMoneyServiceRequest;
import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import com.bombo.hexagonalArch.account.domain.vo.Money;
import jakarta.validation.constraints.NotNull;

public record SendMoneyRequest(
        @NotNull
        Long senderId,

        @NotNull
        Long receiverId,

        @NotNull
        Long amount
) {

    public SendMoneyServiceRequest toService() {
        return new SendMoneyServiceRequest(new AccountId(senderId), new AccountId(receiverId), Money.from(amount));
    }
}
