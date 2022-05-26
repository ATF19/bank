package com.bsfdv.backend.domain.service.transfer

import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.model.transfer.TransferStatus
import com.bsfdv.backend.domain.service.core.DomainService
import com.bsfdv.backend.domain.service.core.EventReader

@DomainService
class TransferReadRepository(private val eventReader: EventReader) : Transfers {

    override fun by(id: TransferId) = Transfer(eventReader.by(id))

    override fun pending() = all().filter { it.status == TransferStatus.PENDING }

    override fun completed() = all().filter { it.status == TransferStatus.COMPLETED }

    override fun rejected() = all().filter { it.status == TransferStatus.REJECTED }

    private fun all(): List<Transfer> {
        return eventReader.byType(Transfer::class)
            .map { Transfer(it) }
            .filter { !it.deleted }
    }
}