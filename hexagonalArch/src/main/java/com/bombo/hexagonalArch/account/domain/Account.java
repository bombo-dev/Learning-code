package com.bombo.hexagonalArch.account.domain;

import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import lombok.Builder;

public class Account {
    private final AccountId accountId;

    @Builder
    public Account(AccountId accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId.getId();
    }
}
