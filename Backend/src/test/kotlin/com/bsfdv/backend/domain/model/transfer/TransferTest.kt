package com.bsfdv.backend.domain.model.transfer

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.common.InvalidAmount
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
class TransferTest {

    lateinit var source: AccountId private set
    lateinit var destination: AccountId private set
    lateinit var amount: Money private set
    lateinit var motif: TransferMotif private set

    @BeforeEach
    internal fun setUp() {
        source = AccountId()
        destination = AccountId()
        amount = Money(BigDecimal(500))
        motif = TransferMotif("A test transfer")
    }

    @Test
    fun throw_error_when_trying_to_create_a_transfer_with_an_amount_less_than_10() {
        // given
        amount = Money(BigDecimal(9))

        // when

        // then
        assertThatThrownBy { Transfer.doTransfer(source, destination, amount, motif) }
            .isInstanceOf(InvalidAmount::class.java)
    }

    @Test
    fun throw_error_when_trying_to_create_a_transfer_with_the_same_source_and_destination() {
        // given
        destination = AccountId(source.rawId)

        // when

        // then
        assertThatThrownBy { Transfer.doTransfer(source, destination, amount, motif) }
            .isInstanceOf(SourceAndDestinationAccountsShouldBeDifferent::class.java)
    }

    @Test
    fun create_transfer() {
        // given

        // when
        val transfer = Transfer.doTransfer(source, destination, amount, motif)

        // then
        assertThat(transfer.source).isEqualTo(source)
        assertThat(transfer.destination).isEqualTo(destination)
        assertThat(transfer.amount).isEqualTo(amount)
        assertThat(transfer.status).isEqualTo(TransferStatus.PENDING)
        assertThat(transfer.motif).isEqualTo(motif)
    }

    @Test
    fun accept_transfer() {
        // given
        val transfer = Transfer.doTransfer(source, destination, amount, motif)

        // when
        transfer.complete()

        // then
        assertThat(transfer.status).isEqualTo(TransferStatus.COMPLETED)
    }

    @Test
    fun reject_transfer() {
        // given
        val transfer = Transfer.doTransfer(source, destination, amount, motif)

        // when
        transfer.reject()

        // then
        assertThat(transfer.status).isEqualTo(TransferStatus.REJECTED)
    }
}