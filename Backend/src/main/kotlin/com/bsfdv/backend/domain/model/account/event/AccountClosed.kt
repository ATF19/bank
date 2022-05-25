package com.bsfdv.backend.domain.model.account.event

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.Version
import java.time.Instant

class AccountClosed(id: AccountId, version: Version, time: Instant) : Event<AccountId>(id, version, time, true)