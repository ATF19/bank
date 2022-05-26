package com.bsfdv.backend.domain.model.transfer

import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.DomainEntity
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.EventStream
import com.bsfdv.backend.domain.model.common.InvalidAmount
import com.bsfdv.backend.domain.model.core.START_VERSION
import com.bsfdv.backend.domain.model.transfer.event.TransferCompleted
import com.bsfdv.backend.domain.model.transfer.event.TransferCreated
import com.bsfdv.backend.domain.model.transfer.event.TransferRejected
import java.math.BigDecimal
import java.time.Instant

open class Transfer(history: EventStream<TransferId>) : DomainEntity<TransferId>(history) {

    lateinit var source: AccountId private set
    lateinit var destination: AccountId private set
    lateinit var amount: Money private set
    lateinit var status: TransferStatus private set
    lateinit var motif: TransferMotif private set

    companion object {
        fun doTransfer(source: AccountId, destination: AccountId, amount: Money, motif: TransferMotif): Transfer {
            verifyTransferAmountIsGreaterThanTen(amount)
            verifySourceAndDestinationAreDifferent(source, destination)
            val transferCreated = TransferCreated(
                TransferId(), source, destination, amount, TransferStatus.PENDING,
                motif, START_VERSION, Instant.now()
            )
            val eventStream = EventStream(transferCreated)
            return Transfer(eventStream)
        }

        private fun verifyTransferAmountIsGreaterThanTen(amount: Money) {
            if (amount.amount.compareTo(BigDecimal.TEN) < 0)
                throw InvalidAmount(amount)
        }

        private fun verifySourceAndDestinationAreDifferent(source: AccountId, destination: AccountId) {
            if (source == destination)
                throw SourceAndDestinationAccountsShouldBeDifferent()
        }
    }

    fun complete() {
        val transferCompleted = TransferCompleted(id, version.next(), Instant.now())
        addToHistoryAndApply(transferCompleted)
    }

    fun reject() {
        val transferRejected = TransferRejected(id, version.next(), Instant.now())
        addToHistoryAndApply(transferRejected)
    }

    override fun apply(event: Event<TransferId>) {
        when (event) {
            is TransferCreated -> applyTransferCreated(event)
            is TransferCompleted -> applyTransferCompleted()
            is TransferRejected -> applyTransferRejected()
        }
    }

    private fun applyTransferCreated(transferCreated: TransferCreated) {
        source = transferCreated.source
        destination = transferCreated.destination
        amount = transferCreated.amount
        status = transferCreated.status
        motif = transferCreated.motif
    }

    private fun applyTransferCompleted() {
        status = TransferStatus.COMPLETED
    }

    private fun applyTransferRejected() {
        status = TransferStatus.REJECTED
    }
}