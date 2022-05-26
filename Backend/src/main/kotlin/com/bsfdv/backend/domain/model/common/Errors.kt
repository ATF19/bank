package com.bsfdv.backend.domain.model.common

import com.bsfdv.backend.domain.model.core.DomainException

class InvalidAmount(amount: Money) :
    DomainException("The amount ${amount.amount} is invalid. Please choose an amount which is equal or greater than 10.")