package com.bsfdv.backend.domain.model.common

import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
internal class MoneyTest {

    private lateinit var money: Money

    @BeforeEach
    fun setUp() {
        money = Money(BigDecimal(20000))
    }

    @Test
    fun can_add_amount() {
        // given
        val amountToAdd = Money(BigDecimal(5000))

        // when
        val result = money.plus(amountToAdd)

        // then
        assertThat(result).isEqualTo(Money(BigDecimal(25000)))
    }

    @Test
    fun can_subtract_amount() {
        // given
        val amountToSubtract = Money(BigDecimal(9000))

        // when
        val result = money.minus(amountToSubtract)

        // then
        assertThat(result).isEqualTo(Money(BigDecimal(11000)))
    }

    @Test
    fun is_zero() {
        // given
        val money = Money(BigDecimal(0))

        // when
        val result = money.isZero()

        // then
        assertThat(result).isTrue
    }

    @Test
    fun is_below_zero() {
        // given
        val money = Money(BigDecimal(-20))

        // when
        val result = money.isBelowZero()

        // then
        assertThat(result).isTrue
    }
}