package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.service.core.DomainService
import com.bsfdv.backend.domain.service.core.EventReader
import com.bsfdv.backend.domain.service.core.NoSuchEntityExists

@DomainService
class AccountReadRepository(private val eventReader: EventReader) : Accounts {

    override fun all(): List<Account> {
        return eventReader.byType(Account::class)
            .map { Account(it) }
    }

    override fun by(accountId: AccountId): Account {
        val eventStream = eventReader.by(accountId)
        if (eventStream.isEmpty())
            throw NoSuchEntityExists("Account", accountId)

        return Account(eventStream)
    }

}