package com.bsfdv.backend.presentation.rest.transfer

import com.bsfdv.backend.application.transfer.DoTransferCommand
import com.bsfdv.backend.application.transfer.TransferAppService
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.suites.UNIT_TEST
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferRestServiceTest {

    private lateinit var transferRestService: TransferRestService
    private lateinit var transferAppService: TransferAppService
    private lateinit var transfer1: Transfer
    private lateinit var transfer2: Transfer
    private lateinit var transfer3: Transfer

    @BeforeEach
    fun setUp() {
        transferAppService = mockk()
        transferRestService = TransferRestService(transferAppService)
        transfer1 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer2 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer3 =
            Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(200)), TransferMotif("Just for testing"))
        transfer2.complete()
        transfer3.reject()
        every { transferAppService.by(transfer1.id) }.returns(transfer1)
        every { transferAppService.pending() }.returns(listOf(transfer1))
        every { transferAppService.completed() }.returns(listOf(transfer2))
        every { transferAppService.rejected() }.returns(listOf(transfer3))
    }

    @Test
    fun delegate_pending_transfers_retrieval_to_app_service() {
        // given

        // when
        val transferDtos = transferRestService.pending()

        // then
        assertThat(transferDtos).containsExactlyInAnyOrder(TransferDto.from(transfer1))
    }

    @Test
    fun delegate_completed_transfers_retrieval_to_app_service() {
        // given

        // when
        val transferDtos = transferRestService.completed()

        // then
        assertThat(transferDtos).containsExactlyInAnyOrder(TransferDto.from(transfer2))
    }

    @Test
    fun delegate_rejected_transfers_retrieval_to_app_service() {
        // given

        // when
        val transferDtos = transferRestService.rejected()

        // then
        assertThat(transferDtos).containsExactlyInAnyOrder(TransferDto.from(transfer3))
    }

    @Test
    fun delegate_transfer_retrieval_by_id_to_app_service() {
        // given
        val id = transfer1.id.rawId.toString()

        // when
        val transferDto = transferRestService.byId(id)

        // then
        assertThat(transferDto).isEqualTo(TransferDto.from(transfer1))
    }

    @Test
    fun delegate_transfer_creation_to_app_service() {
        // given
        val request = DoTransferRequestDto(
            transfer1.source.rawId.toString(), transfer1.destination.rawId.toString(),
            transfer1.amount.toString(), transfer1.motif.motif
        )
        val slot = slot<DoTransferCommand>()
        every { transferAppService.doTransfer(capture(slot)) }.returns(transfer1)

        // when
        transferRestService.doTransfer(request)

        // then
        assertThat(slot.captured.source).isEqualTo(transfer1.source)
        assertThat(slot.captured.destination).isEqualTo(transfer1.destination)
        assertThat(slot.captured.amount.toString()).isEqualTo(transfer1.amount.toString())
        assertThat(slot.captured.motif).isEqualTo(transfer1.motif)
    }
}