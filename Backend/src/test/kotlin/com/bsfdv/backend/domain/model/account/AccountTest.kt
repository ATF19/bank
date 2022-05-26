package com.bsfdv.backend.domain.model.account

import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
internal class AccountTest {

    private lateinit var number: AccountNumber
    private lateinit var holder: AccountHolder
    private lateinit var balance: Money
    private lateinit var account: Account

    @BeforeEach
    fun setUp() {
        number = AccountNumber("TEST1")
        holder = AccountHolder("John", "Doe")
        balance = Money(BigDecimal(1500))
        account = Account.openAccount(number, holder, balance)
    }

    @Test
    fun throw_error_when_trying_to_open_an_account_with_negative_balance() {
        // given
        balance = Money(BigDecimal(-150))

        // when

        // then
        assertThatThrownBy { Account.openAccount(number, holder, balance) }
            .isInstanceOf(InvalidInitialBalance::class.java)
    }

    @Test
    fun can_open_an_account() {
        // given

        // when
        val account = Account.openAccount(number, holder, balance)

        // then
        assertThat(account.number).isEqualTo(number)
        assertThat(account.holder).isEqualTo(holder)
        assertThat(account.balance).isEqualTo(balance)
        assertThat(account.deleted).isFalse
    }

    @Test
    fun throw_error_when_trying_to_close_an_account_with_balance() {
        // given

        // when

        // then
        assertThatThrownBy { account.closeAccount() }
            .isInstanceOf(ClosingAccountWithBalance::class.java)
    }

    @Test
    fun can_close_an_account() {
        // given
        balance = Money(BigDecimal(0))
        account = Account.openAccount(number, holder, balance)

        // when
        account.closeAccount()

        // then
        assertThat(account.deleted).isTrue
    }

    @Test
    fun can_update_holder_info() {
        // given
        val newHolderInfo = AccountHolder("Jessica", "Doe")

        // when
        account.updateHolderInformation(newHolderInfo)

        // then
        assertThat(account.holder).isEqualTo(newHolderInfo)
    }

    @Test
    fun can_deposit_money() {
        // given
        val amount = Money(BigDecimal(2000))

        // when
        account.deposit(amount)

        // then
        assertThat(account.balance).isEqualTo(Money(BigDecimal(3500)))
    }

    @Test
    fun throw_error_when_trying_to_withdraw_more_money_than_what_account_has_in_balance() {
        // given
        val amount = Money(BigDecimal(2000))

        // when

        // then
        assertThatThrownBy { account.withdraw(amount) }
            .isInstanceOf(NoSufficientBalanceForWithdrawalOrTransfer::class.java)
        assertThat(account.balance).isEqualTo(balance)
    }

    @Test
    fun can_withdraw_money() {
        // given
        val amount = Money(BigDecimal(500))

        // when
        account.withdraw(amount)

        // then
        assertThat(account.balance).isEqualTo(Money(BigDecimal(1000)))
    }

    @Test
    fun throw_error_when_trying_to_transfer_more_money_than_what_account_has_in_balance() {
        // given
        val amount = Money(BigDecimal(2000))
        val destination = AccountId()

        // when

        // then
        assertThatThrownBy { account.transfer(amount, destination) }
            .isInstanceOf(NoSufficientBalanceForWithdrawalOrTransfer::class.java)
        assertThat(account.balance).isEqualTo(balance)
    }

    @Test
    fun can_transfer_money() {
        // given
        val amount = Money(BigDecimal(500))
        val destination = AccountId()

        // when
        account.transfer(amount, destination)

        // then
        assertThat(account.balance).isEqualTo(Money(BigDecimal(1000)))
    }

    @Test
    fun can_receive_money() {
        // given
        val amount = Money(BigDecimal(500))
        val source = AccountId()

        // when
        account.receive(amount, source)

        // then
        assertThat(account.balance).isEqualTo(Money(BigDecimal(2000)))
    }
}