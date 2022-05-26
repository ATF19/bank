package com.bsfdv.backend.presentation.core

import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@Tag(UNIT_TEST)
class UtilsTest {

    @Test
    fun throw_error_if_trying_to_get_amount_from_invalid_input() {
        // given
        val raw = "168Invalid"

        // when

        // then
        assertThatThrownBy { getAmountFromRawString(raw) }
            .isInstanceOf(InvalidInputValue::class.java)
    }

    @Test
    fun get_amount_from_input() {
        // given
        val raw = "168.5"

        // when
        val amount = getAmountFromRawString(raw)

        // then
        assertThat(amount.toString()).isEqualTo("168.5000")
    }

    @Test
    fun throw_error_if_given_empty_input_value() {
        // given
        val inputValue1 = Pair("Input 1", "Value 1")
        val inputValue2 = Pair("Input 2", "")

        // when

        // then
        assertThatThrownBy { verifyAllInputsHaveValues(inputValue1, inputValue2) }
            .isInstanceOf(EmptyRequiredInput::class.java)
    }

    @Test
    fun do_not_throw_error_if_given_filled_input_value() {
        // given
        val inputValue1 = Pair("Input 1", "Value 1")
        val inputValue2 = Pair("Input 2", "Value 2")

        // when

        // then
        assertDoesNotThrow { verifyAllInputsHaveValues(inputValue1, inputValue2) }
    }
}