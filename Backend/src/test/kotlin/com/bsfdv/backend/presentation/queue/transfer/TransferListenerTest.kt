package com.bsfdv.backend.presentation.queue.transfer

import com.bsfdv.backend.application.account.AccountAppService
import com.bsfdv.backend.application.account.ReceiveAmountCommand
import com.bsfdv.backend.application.account.TransferAmountCommand
import com.bsfdv.backend.application.transfer.CompleteTransferCommand
import com.bsfdv.backend.application.transfer.RejectTransferCommand
import com.bsfdv.backend.application.transfer.TransferAppService
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.account.NoSufficientBalanceForWithdrawalOrTransfer
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.domain.model.transfer.event.TransferCreated
import com.bsfdv.backend.suites.UNIT_TEST
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferListenerTest {

    private lateinit var transferListener: TransferListener
    private lateinit var transferAppService: TransferAppService
    private lateinit var accountAppService: AccountAppService
    private lateinit var transfer: Transfer
    private lateinit var transferCreated: TransferCreated

    @BeforeEach
    fun setUp() {
        transferAppService = mockk(relaxed = true)
        accountAppService = mockk(relaxed = true)
        transferListener = TransferListener(transferAppService, accountAppService)
        transfer = Transfer.doTransfer(
            AccountId(),
            AccountId(),
            Money(BigDecimal(1000)),
            TransferMotif("Testing the listener")
        )
        transferCreated = transfer.history.events().first() as TransferCreated
    }

    @Test
    fun reject_transfer_if_an_error_occurs_when_moving_money() {
        // given
        every { accountAppService.transferAmount(any()) }.throws(NoSufficientBalanceForWithdrawalOrTransfer(transfer.amount))
        val slot = slot<RejectTransferCommand>()

        // when
        transferListener.transferCreated(transferCreated)

        // then
        verify { transferAppService.rejectTransfer(capture(slot)) }
        assertThat(slot.captured.transferId).isEqualTo(transfer.id)
    }

    @Test
    fun complete_transfer_after_moving_money() {
        // given
        val transferAmountSlot = slot<TransferAmountCommand>()
        val receiveAmountSlot = slot<ReceiveAmountCommand>()
        val completeTransferSlot = slot<CompleteTransferCommand>()

        // when
        transferListener.transferCreated(transferCreated)

        // then
        verify { accountAppService.transferAmount(capture(transferAmountSlot)) }
        verify { accountAppService.receiveAmount(capture(receiveAmountSlot)) }
        verify { transferAppService.completeTransfer(capture(completeTransferSlot)) }
        assertMoneyWasMovedFromSource(transferAmountSlot)
        assertMoneyWasMovedToDestination(receiveAmountSlot)
        assertThat(completeTransferSlot.captured.transferId).isEqualTo(transfer.id)
    }

    private fun assertMoneyWasMovedToDestination(receiveAmountSlot: CapturingSlot<ReceiveAmountCommand>) {
        assertThat(receiveAmountSlot.captured.accountId).isEqualTo(transfer.destination)
        assertThat(receiveAmountSlot.captured.source).isEqualTo(transfer.source)
        assertThat(receiveAmountSlot.captured.amountToReceive).isEqualTo(transfer.amount)
    }

    private fun assertMoneyWasMovedFromSource(transferAmountSlot: CapturingSlot<TransferAmountCommand>) {
        assertThat(transferAmountSlot.captured.accountId).isEqualTo(transfer.source)
        assertThat(transferAmountSlot.captured.destination).isEqualTo(transfer.destination)
        assertThat(transferAmountSlot.captured.amountToTransfer).isEqualTo(transfer.amount)
    }
}