package com.bombo.hexagonalArch.account.adapter.out.persistence.repository;

import com.bombo.hexagonalArch.account.application.port.out.AccountFindPort;
import com.bombo.hexagonalArch.account.application.port.out.AccountRepository;
import com.bombo.hexagonalArch.account.domain.Account;
import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Component
public class AccountRepositoryAdapter implements AccountFindPort {

    private final AccountRepository accountRepository;

    @Override
    public Account findAccount(AccountId id) {
        Account account = accountRepository.findById(id.getId())
                .orElseThrow(EntityNotFoundException::new);

        return account;
    }
}
