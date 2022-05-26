package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.account.AccountNumber
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.EventStream
import com.bsfdv.backend.domain.service.core.EventReader
import com.bsfdv.backend.domain.service.core.NoSuchEntityExists
import com.bsfdv.backend.suites.UNIT_TEST
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
internal class AccountReadRepositoryTest {

    private lateinit var accounts: Accounts
    private lateinit var eventReader: EventReader
    private lateinit var account1: Account
    private lateinit var account2: Account
    private lateinit var account3: Account

    @BeforeEach
    fun setUp() {
        eventReader = mockk()
        accounts = AccountReadRepository(eventReader)
        account1 = Account.openAccount(AccountNumber("TEST1"), AccountHolder("Joh", "Doe"), Money(BigDecimal(2000)))
        account2 = Account.openAccount(AccountNumber("TEST2"), AccountHolder("Jessica", "Doe"), Money(BigDecimal(0)))
        account3 =
            Account.openAccount(AccountNumber("TEST3"), AccountHolder("Amelie", "Scarlit"), Money(BigDecimal(100)))
        every { eventReader.byType(Account::class) }.returns(
            listOf(
                account1.history,
                account2.history,
                account3.history
            )
        )
    }

    @Test
    fun can_retrieve_all_accounts() {
        // given

        // when
        val result = accounts.all()

        // then
        assertThat(result).containsExactlyInAnyOrder(account1, account2, account3)
    }

    @Test
    fun throw_error_if_no_account_exists_for_given_id() {
        // given
        val id = AccountId()
        every { eventReader.by(id) }.returns(EventStream(sortedSetOf()))

        // when

        // then
        assertThatThrownBy { accounts.by(id) }
            .isInstanceOf(NoSuchEntityExists::class.java)
    }

    @Test
    fun retrieve_account_by_id() {
        // given
        every { eventReader.by(account1.id) }.returns(account1.history)

        // when
        val result = accounts.by(account1.id)

        // then
        assertThat(result).isEqualTo(account1)
    }

    @Test
    fun return_null_if_no_account_with_given_number_exists() {
        // given
        val number = AccountNumber("NON-EXISTING")

        // when
        val result = accounts.by(number)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun retrieve_account_by_number() {
        // given

        // when
        val result = accounts.by(account1.number)

        // then
        assertThat(result).isEqualTo(account1)
    }
}