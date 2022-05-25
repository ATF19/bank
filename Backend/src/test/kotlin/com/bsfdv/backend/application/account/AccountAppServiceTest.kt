package com.bsfdv.backend.application.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.service.account.Accounts
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.math.BigDecimal

@Tag(UNIT_TEST)
class AccountAppServiceTest {

    private lateinit var accountAppService: AccountAppService
    private lateinit var accounts: Accounts
    private lateinit var eventWriter: EventWriter
    private lateinit var account1: Account
    private lateinit var account2: Account

    @BeforeEach
    fun setUp() {
        accounts = mock(Accounts::class.java)
        eventWriter = mock(EventWriter::class.java)
        accountAppService = AccountAppService(accounts, eventWriter)
        account1 = Account.openAccount(AccountHolder("Joh", "Doe"), Money(BigDecimal(2000)))
        account2 = Account.openAccount(AccountHolder("Jessica", "Doe"), Money(BigDecimal(0)))
        given(accounts.all()).willReturn(listOf(account1, account2))
        given(accounts.by(account1.id)).willReturn(account1)
        given(accounts.by(account2.id)).willReturn(account2)
    }

    @Test
    fun delegate_account_creation_to_domain_entity() {
        // given
        val command = OpenAccountCommand(AccountHolder("Joh", "Doe"), Money(BigDecimal.TEN))

        // when
        val openedAccount = accountAppService.openAccount(command)

        // then
        assertThat(openedAccount.holder).isEqualTo(command.holder)
        assertThat(openedAccount.balance).isEqualTo(command.initialBalance)
    }

    @Test
    fun delegate_account_retrieval_to_repo() {
        // given

        // when
        val result = accountAppService.all()

        // then
        verify(accounts).all()
        assertThat(result).containsExactlyInAnyOrder(account1, account2)
    }

    @Test
    fun delegate_account_holder_update_to_domain_entity() {
        // given
        val command = UpdateHolderInformationCommand(account1.id, AccountHolder("Jessica", "Doe"))

        // when
        val result = accountAppService.updateHolderInformation(command)

        // then
        assertThat(result.holder).isEqualTo(command.newHolderInfo)
    }

    @Test
    fun delegate_deposit_to_domain_entity() {
        // given
        val oldBalance = account1.balance
        val command = DepositAmountCommand(account1.id, Money(BigDecimal(1000)))

        // when
        val result = accountAppService.depositAmount(command)

        // then
        assertThat(result.balance).isEqualTo(oldBalance.plus(command.amountToDeposit))
    }

    @Test
    fun delegate_withdraw_to_domain_entity() {
        // given
        val oldBalance = account1.balance
        val command = WithdrawAmountCommand(account1.id, Money(BigDecimal(1000)))

        // when
        val result = accountAppService.withdrawAmount(command)

        // then
        assertThat(result.balance).isEqualTo(oldBalance.minus(command.amountToWithdraw))
    }

    @Test
    fun delegate_account_closing_to_domain_entity() {
        // given
        val command = CloseAccountCommand(account2.id)

        // when
        val result = accountAppService.closeAccount(command)

        // then
        assertThat(result.deleted).isTrue
    }
}