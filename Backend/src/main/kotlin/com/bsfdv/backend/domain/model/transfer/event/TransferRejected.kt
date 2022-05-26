package com.bsfdv.backend.domain.model.transfer.event

import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.Version
import com.bsfdv.backend.domain.model.transfer.TransferId
import java.time.Instant

class TransferRejected(aggregateId: TransferId, version: Version, time: Instant) :
    Event<TransferId>(aggregateId, version, time)