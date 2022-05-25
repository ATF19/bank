package com.bsfdv.backend.domain.model.account.event

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.Version
import java.time.Instant

class AccountClosed(aggregateId: AccountId, version: Version, time: Instant) :
    Event<AccountId>(aggregateId, version, time, true)