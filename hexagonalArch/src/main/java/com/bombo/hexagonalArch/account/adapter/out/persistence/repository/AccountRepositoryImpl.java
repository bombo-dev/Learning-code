package com.bombo.hexagonalArch.account.adapter.out.persistence.repository;

import com.bombo.hexagonalArch.account.adapter.out.persistence.entity.AccountEntity;
import com.bombo.hexagonalArch.account.application.port.out.AccountRepository;
import com.bombo.hexagonalArch.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository repository;

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id)
                .map(AccountEntity::toDomain);
    }
}
