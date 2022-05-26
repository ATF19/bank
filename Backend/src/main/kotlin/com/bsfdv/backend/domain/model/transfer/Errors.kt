package com.bsfdv.backend.domain.model.transfer

import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.DomainException

class InvalidTransferAmount(amount: Money) :
    DomainException("The transfer amount, ${amount.amount}, is invalid.")
