package com.bsfdv.backend.presentation.core

import com.bsfdv.backend.domain.model.common.Money
import com.google.common.base.Strings
import org.springframework.util.NumberUtils
import java.math.BigDecimal

fun getAmountFromRawString(rawAmount: String): Money {
    try {
        val parsedAmount = NumberUtils.parseNumber(rawAmount, BigDecimal::class.java)
        return Money(parsedAmount)
    } catch (ex: NumberFormatException) {
        throw InvalidInputValue("Amount", rawAmount)
    }
}

fun verifyAllInputsHaveValues(vararg inputNameValuePairs: Pair<String, String>) {
    for (inputNameValuePair in inputNameValuePairs) {
        if (Strings.isNullOrEmpty(inputNameValuePair.second))
            throw EmptyRequiredInput(inputNameValuePair.first)
    }
}