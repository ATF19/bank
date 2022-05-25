package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.service.core.DomainService
import com.bsfdv.backend.domain.service.core.EventReader

@DomainService
class AccountReadRepository(private val eventReader: EventReader) : Accounts {

    override fun all(): List<Account> {
        return eventReader.byType(Account::class)
            .map { Account(it) }
    }

}