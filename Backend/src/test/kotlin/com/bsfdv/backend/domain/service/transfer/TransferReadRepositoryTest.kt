package com.bsfdv.backend.domain.service.transfer

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.domain.service.core.EventReader
import com.bsfdv.backend.suites.UNIT_TEST
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferReadRepositoryTest {

    private lateinit var transfers: Transfers
    private lateinit var eventReader: EventReader
    private lateinit var transfer1: Transfer
    private lateinit var transfer2: Transfer
    private lateinit var transfer3: Transfer
    private lateinit var transfer4: Transfer

    @BeforeEach
    fun setUp() {
        eventReader = mockk()
        transfers = TransferReadRepository(eventReader)
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
        every { eventReader.byType(Transfer::class) }.returns(
            listOf(
                transfer1.history,
                transfer2.history,
                transfer3.history,
                transfer4.history
            )
        )
    }

    @Test
    fun can_retrieve_pending_transfers() {
        // given

        // when
        val result = transfers.pending()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer1)
    }

    @Test
    fun can_retrieve_completed_transfers() {
        // given

        // when
        val result = transfers.completed()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer2, transfer3)
    }

    @Test
    fun can_retrieve_rejected_transfers() {
        // given

        // when
        val result = transfers.rejected()

        // then
        assertThat(result).containsExactlyInAnyOrder(transfer4)
    }
}