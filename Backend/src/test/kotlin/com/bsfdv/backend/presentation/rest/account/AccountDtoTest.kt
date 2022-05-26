package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.account.AccountNumber
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Tag(UNIT_TEST)
internal class AccountDtoTest {

    private lateinit var account: Account

    @BeforeEach
    fun setUp() {
        account = Account.openAccount(AccountNumber("TEST1"), AccountHolder("John", "Doe"), Money(BigDecimal(2000)))
    }

    @Test
    fun create_dto_from_account() {
        // given

        // when
        val dto = AccountDto.from(account)

        // then
        assertThat(dto.id).isEqualTo(account.id.rawId.toString())
        assertThat(dto.number).isEqualTo("TEST1")
        assertThat(dto.firstName).isEqualTo("John")
        assertThat(dto.lastName).isEqualTo("Doe")
        assertThat(dto.balance).isEqualTo("2000.0000")
    }
}