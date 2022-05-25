package com.bsfdv.backend.domain.model.common

import java.math.BigDecimal

data class Money(val amount: BigDecimal) {
    fun plus(amountToAdd: Money) = Money(amount.plus(amountToAdd.amount))
    fun minus(amountToSubtract: Money) = Money(amount.minus(amountToSubtract.amount))
    fun isBelowZero() = amount.signum() < 0
    fun isZero() = amount.signum() == 0
}