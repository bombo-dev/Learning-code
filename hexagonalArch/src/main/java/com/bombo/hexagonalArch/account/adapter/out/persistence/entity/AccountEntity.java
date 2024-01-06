package com.bombo.hexagonalArch.account.adapter.out.persistence.entity;

import com.bombo.hexagonalArch.account.domain.Account;
import com.bombo.hexagonalArch.account.domain.vo.AccountId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "accounts")
@Entity
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    private AccountEntity(Long id) {
        this.id = id;
    }

    public static AccountEntity from(Account account) {
        return AccountEntity.builder()
                .id(account.getAccountId())
                .build();
    }

    public Account toDomain() {
        return Account.builder()
                .accountId(new AccountId(id))
                .build();
    }
}
