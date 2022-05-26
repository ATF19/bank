package com.bsfdv.backend.domain.service.account

import com.bsfdv.backend.domain.model.account.AccountNumber
import com.bsfdv.backend.domain.service.core.DomainService
import java.util.*

@DomainService
class AccountNumberGenerator {

    private val prefix = "BSFDV"
    private val numberLength = 15

    fun generate(): AccountNumber {
        val rand = Random()
        var number = StringBuilder(prefix)

        for (i in 0..numberLength - 1)
            number = number.append(rand.nextInt(10))

        return AccountNumber(number.toString())
    }

}