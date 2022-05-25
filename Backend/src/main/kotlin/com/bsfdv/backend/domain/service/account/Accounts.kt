package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountId

interface Accounts {
    fun all(): List<Account>
    fun by(accountId: AccountId): Account
}