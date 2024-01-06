package com.bombo.hexagonalArch.account.application.port.out;

import com.bombo.hexagonalArch.account.domain.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);
}
