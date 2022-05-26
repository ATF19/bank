package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(UNIT_TEST)
class AccountNumberGeneratorTest {

    @Test
    fun generate_random_account_number() {
        // given
        val generator = AccountNumberGenerator()

        // when
        val result = generator.generate()

        // then
        assertThat(result.number).hasSize(20)
    }

}