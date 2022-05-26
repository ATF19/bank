package com.bsfdv.backend.domain.model.transfer

import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.DomainException

class SourceAndDestinationAccountsShouldBeDifferent() :
    DomainException("Cannot do a transfer to the same account.")
