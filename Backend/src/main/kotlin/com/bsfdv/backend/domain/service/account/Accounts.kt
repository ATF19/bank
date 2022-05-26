package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.account.AccountNumber

interface Accounts {
    fun all(): List<Account>
    fun by(accountId: AccountId): Account
    fun by(number: AccountNumber): Account?
}