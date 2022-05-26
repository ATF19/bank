package com.bsfdv.backend.presentation.rest.transfer

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferMotif
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferDtoTest {

    private lateinit var transfer: Transfer

    @BeforeEach
    fun setUp() {
        transfer = Transfer.doTransfer(AccountId(), AccountId(), Money(BigDecimal(100)), TransferMotif("For testing"))
    }

    @Test
    fun map_transfer_to_dto() {
        // given

        // when
        val dto = TransferDto.from(transfer)

        // then
        assertThat(dto.id).isEqualTo(transfer.id.rawId.toString())
        assertThat(dto.destination).isEqualTo(transfer.destination.rawId.toString())
        assertThat(dto.destination).isEqualTo(transfer.destination.rawId.toString())
        assertThat(dto.amount).isEqualTo(transfer.amount.toString())
        assertThat(dto.motif).isEqualTo(transfer.motif.motif)
        assertThat(dto.status).isEqualTo("PENDING")
    }
}