package com.bombo.hexagonalArch.account.domain;

import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import com.bombo.hexagonalArch.account.domain.vo.Money;
import lombok.Builder;

public class Account {
    private final AccountId accountId;
    private final Money balance;

    @Builder
    public Account(AccountId accountId, Money balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId.getId();
    }

    public Long getBalance() {
        return balance.getAmount().longValue();
    }
}
