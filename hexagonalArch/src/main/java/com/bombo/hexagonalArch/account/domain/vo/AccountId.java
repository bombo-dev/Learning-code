package com.bombo.hexagonalArch.account.domain.vo;

import lombok.Getter;

@Getter
public class AccountId {

    private final Long id;

    public AccountId(Long id) {
        this.id = id;
    }
}
