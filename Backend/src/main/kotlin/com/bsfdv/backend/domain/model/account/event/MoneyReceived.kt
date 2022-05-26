package com.bsfdv.backend.domain.model.account.event

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.Version
import java.time.Instant

class MoneyReceived(aggregateId: AccountId, val amount: Money, val source: AccountId, version: Version, time: Instant) :
    Event<AccountId>(aggregateId, version, time)