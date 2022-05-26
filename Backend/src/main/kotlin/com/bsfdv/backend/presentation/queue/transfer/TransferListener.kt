package com.bsfdv.backend.presentation.queue.transfer

import com.bsfdv.backend.application.account.AccountAppService
import com.bsfdv.backend.application.account.ReceiveAmountCommand
import com.bsfdv.backend.application.account.TransferAmountCommand
import com.bsfdv.backend.application.transfer.CompleteTransferCommand
import com.bsfdv.backend.application.transfer.RejectTransferCommand
import com.bsfdv.backend.application.transfer.TransferAppService
import com.bsfdv.backend.domain.model.core.DomainException
import com.bsfdv.backend.domain.model.transfer.event.TransferCreated
import com.bsfdv.backend.presentation.queue.EventBusListener
import com.google.common.eventbus.Subscribe
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.logging.Logger

@Component
@Transactional
class TransferListener(
    private val transferAppService: TransferAppService,
    private val accountAppService: AccountAppService
) : EventBusListener {

    private val logger = Logger.getLogger(this::class.qualifiedName)

    @Subscribe
    fun transferCreated(transferCreated: TransferCreated) {
        logger.info("Handling transfer with ID ${transferCreated.aggregateId.rawId}")
        try {
            moveMoneyFromSourceToDestination(transferCreated)
            transferAppService.completeTransfer(CompleteTransferCommand(transferCreated.aggregateId))
            logger.info("Successfully completed transfer with ID ${transferCreated.aggregateId.rawId}")
        } catch (ex: DomainException) {
            transferAppService.rejectTransfer(RejectTransferCommand(transferCreated.aggregateId))
            logger.severe(
                "The following error occurred when handling transfer with ID ${transferCreated.aggregateId.rawId}: " +
                        ex.localizedMessage
            )
        }
    }

    private fun moveMoneyFromSourceToDestination(transferCreated: TransferCreated) {
        moveMoneyFromSource(transferCreated)
        moveMoneyToDestination(transferCreated)
    }

    private fun moveMoneyFromSource(transferCreated: TransferCreated) {
        accountAppService.transferAmount(
            TransferAmountCommand(
                transferCreated.source,
                transferCreated.destination,
                transferCreated.amount
            )
        )
    }

    private fun moveMoneyToDestination(transferCreated: TransferCreated) {
        accountAppService.receiveAmount(
            ReceiveAmountCommand(
                transferCreated.destination,
                transferCreated.source,
                transferCreated.amount
            )
        )
    }
}