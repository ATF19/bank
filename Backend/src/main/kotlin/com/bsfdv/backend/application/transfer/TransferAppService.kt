package com.bsfdv.backend.application.transfer

import com.bsfdv.backend.application.core.ApplicationService
import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.domain.service.transfer.Transfers

@ApplicationService
class TransferAppService(private val transfers: Transfers, private val eventWriter: EventWriter) {

    fun by(id: TransferId) = transfers.by(id)

    fun pending() = transfers.pending()

    fun completed() = transfers.completed()

    fun rejected() = transfers.rejected()

    fun doTransfer(command: DoTransferCommand): Transfer {
        val transfer = Transfer.doTransfer(command.source, command.destination, command.amount, command.motif)
        return saveAndReturnTransfer(transfer)
    }

    fun completeTransfer(command: CompleteTransferCommand): Transfer {
        val transfer = transfers.by(command.transferId)
        transfer.complete()
        return saveAndReturnTransfer(transfer)
    }

    fun rejectTransfer(command: RejectTransferCommand): Transfer {
        val transfer = transfers.by(command.transferId)
        transfer.reject()
        return saveAndReturnTransfer(transfer)
    }

    private fun saveAndReturnTransfer(transfer: Transfer): Transfer {
        val unitOfWork = UnitOfWork(transfer)
        eventWriter.save(unitOfWork)
        return transfer
    }
}