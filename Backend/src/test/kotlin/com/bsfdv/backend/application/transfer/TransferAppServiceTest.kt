package com.bsfdv.backend.application.transfer

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.domain.model.transfer.TransferStatus
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.domain.service.transfer.Transfers
import com.bsfdv.backend.suites.UNIT_TEST
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferAppServiceTest {

    private lateinit var transferAppService: TransferAppService
    private lateinit var transfers: Transfers
    private lateinit var eventWriter: EventWriter
    private lateinit var transfer1: Transfer
    private lateinit var transfer2: Transfer
    private lateinit var transfer3: Transfer
    private lateinit var transfer4: Transfer

    @BeforeEach
    fun setUp() {
        transfers = mockk()
        eventWriter = mockk(relaxed = true)
        transferAppService = TransferAppService(transfers, eventWriter)
        transfer1 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer2 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer3 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer4 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer2.complete()
        transfer3.complete()
        transfer4.reject()
        every { transfers.by(transfer1.id) }.returns(transfer1)
        every { transfers.pending() }.returns(listOf(transfer1))
        every { transfers.completed() }.returns(listOf(transfer2, transfer3))
        every { transfers.rejected() }.returns(listOf(transfer4))
    }

    @Test
    fun delegate_retrieve_transfer_by_id_to_domain_service() {
        // given

        // when
        val result = transferAppService.by(transfer1.id)

        // then
        assertThat(result).isEqualTo(transfer1)
    }

    @Test
    fun delegate_retrieve_pending_transfers_to_domain_service() {
        // given

        // when
        val result = transferAppService.pending()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer1)
    }

    @Test
    fun delegate_retrieve_completed_transfers_to_domain_service() {
        // given

        // when
        val result = transferAppService.completed()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer2, transfer3)
    }

    @Test
    fun delegate_retrieve_rejected_transfers_to_domain_service() {
        // given

        // when
        val result = transferAppService.rejected()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer4)
    }

    @Test
    fun delegate_do_transfer_to_domain_service() {
        // given
        val command =
            DoTransferCommand(AccountId(), AccountId(), Money(BigDecimal(2000)), TransferMotif("Just for testing"))

        // when
        val result = transferAppService.doTransfer(command)

        // then
        assertThat(result.source).isEqualTo(command.source)
        assertThat(result.destination).isEqualTo(command.destination)
        assertThat(result.amount).isEqualTo(command.amount)
        assertThat(result.motif).isEqualTo(command.motif)
    }

    @Test
    fun delegate_complete_transfer_to_domain_service() {
        // given
        val command = CompleteTransferCommand(transfer1.id)

        // when
        val result = transferAppService.completeTransfer(command)

        // then
        assertThat(result.status).isEqualTo(TransferStatus.COMPLETED)
    }

    @Test
    fun delegate_reject_transfer_to_domain_service() {
        // given
        val command = RejectTransferCommand(transfer1.id)

        // when
        val result = transferAppService.rejectTransfer(command)

        // then
        assertThat(result.status).isEqualTo(TransferStatus.REJECTED)
    }
}