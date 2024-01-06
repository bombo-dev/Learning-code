package com.bombo.hexagonalArch.account.application.port.out;

import com.bombo.hexagonalArch.account.domain.Account;
import com.bombo.hexagonalArch.account.domain.vo.AccountId;

public interface AccountFindPort {
    Account findAccount(AccountId id);
}
