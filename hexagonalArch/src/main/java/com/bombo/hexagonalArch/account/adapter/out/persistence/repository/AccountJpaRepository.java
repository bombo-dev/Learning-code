package com.bombo.hexagonalArch.account.adapter.out.persistence.repository;

import com.bombo.hexagonalArch.account.adapter.out.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
}
