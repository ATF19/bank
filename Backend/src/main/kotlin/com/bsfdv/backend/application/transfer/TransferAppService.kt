package com.bsfdv.backend.application.transfer

import com.bsfdv.backend.application.core.ApplicationService
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.domain.service.transfer.Transfers

@ApplicationService
class TransferAppService(private val transfers: Transfers, private val eventWriter: EventWriter) {

    fun by(id: TransferId) = transfers.by(id)

    fun pending() = transfers.pending()

    fun completed() = transfers.completed()

    fun rejected() = transfers.rejected()

}