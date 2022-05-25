package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.application.account.*
import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.common.Money
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
class AccountRestServiceTest {

    private lateinit var accountRestService: AccountRestService
    private lateinit var accountAppService: AccountAppService
    private lateinit var account1: Account
    private lateinit var account2: Account

    @BeforeEach
    fun setUp() {
        accountAppService = mockk(relaxed = true)
        accountRestService = AccountRestService(accountAppService)
        account1 = Account.openAccount(AccountHolder("John", "Doe"), Money(BigDecimal(2000)))
        account2 = Account.openAccount(AccountHolder("Jessica", "Doe"), Money(BigDecimal(0)))
        every { accountAppService.all() }.returns(listOf(account1, account2))
    }

    @Test
    fun delegate_all_accounts_retrieval_to_app_service() {
        // given

        // when
        val accountDtos = accountRestService.all()

        // then
        assertThat(accountDtos).containsExactlyInAnyOrder(AccountDto.from(account1), AccountDto.from(account2))
    }

    @Test
    fun delegate_opening_an_account_to_app_service() {
        // given
        val request = OpenAccountRequestDto("John", "Doe", "1230")
        val slot = slot<OpenAccountCommand>()
        every { accountAppService.openAccount(capture(slot)) }.returns(account1)

        // when
        accountRestService.openAccount(request)

        // then
        val command = slot.captured
        assertThat(command.holder.firstName).isEqualTo("John")
        assertThat(command.holder.lastName).isEqualTo("Doe")
        assertThat(command.initialBalance).isEqualTo(Money(BigDecimal.valueOf(1230)))
    }

    @Test
    fun delegate_info_update_to_app_service() {
        // given
        val request = UpdateHolderInformationRequestDto("Jessica", "Doe")
        val slot = slot<UpdateHolderInformationCommand>()
        every { accountAppService.updateHolderInformation(capture(slot)) }.returns(account1)

        // when
        accountRestService.updateHolderInformation(account1.id.rawId.toString(), request)

        // then
        val command = slot.captured
        assertThat(command.accountId).isEqualTo(account1.id)
        assertThat(command.newHolderInfo.firstName).isEqualTo("Jessica")
        assertThat(command.newHolderInfo.lastName).isEqualTo("Doe")
    }

    @Test
    fun delegate_deposit_to_app_service() {
        // given
        val request = DepositAmountRequestDto("50990")
        val slot = slot<DepositAmountCommand>()
        every { accountAppService.depositAmount(capture(slot)) }.returns(account1)

        // when
        accountRestService.depositAmount(account1.id.rawId.toString(), request)

        // then
        val command = slot.captured
        assertThat(command.accountId).isEqualTo(account1.id)
        assertThat(command.amountToDeposit).isEqualTo(Money(BigDecimal.valueOf(50990)))
    }

    @Test
    fun delegate_withdrawal_to_app_service() {
        // given
        val request = WithdrawAmountRequestDto("50990")
        val slot = slot<WithdrawAmountCommand>()
        every { accountAppService.withdrawAmount(capture(slot)) }.returns(account1)

        // when
        accountRestService.withdrawAmount(account1.id.rawId.toString(), request)

        // then
        val command = slot.captured
        assertThat(command.accountId).isEqualTo(account1.id)
        assertThat(command.amountToWithdraw).isEqualTo(Money(BigDecimal.valueOf(50990)))
    }

    @Test
    fun delegate_closing_of_an_account_to_app_service() {
        // given
        val slot = slot<CloseAccountCommand>()
        every { accountAppService.closeAccount(capture(slot)) }.returns(account1)

        // when
        accountRestService.closeAccount(account1.id.rawId.toString())

        // then
        val command = slot.captured
        assertThat(command.accountId).isEqualTo(account1.id)
    }
}