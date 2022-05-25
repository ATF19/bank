package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account

interface Accounts {
    fun all(): List<Account>
}