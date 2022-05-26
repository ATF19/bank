package com.bsfdv.backend.domain.model.transfer.event

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.Version
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.domain.model.transfer.TransferStatus
import java.time.Instant

class TransferCreated(
    aggregateId: TransferId, val source: AccountId, val destination: AccountId, val amount: Money,
    val status: TransferStatus, val motif: TransferMotif, version: Version, time: Instant
) : Event<TransferId>(aggregateId, version, time)