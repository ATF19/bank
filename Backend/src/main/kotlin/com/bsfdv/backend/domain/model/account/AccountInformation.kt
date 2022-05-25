package com.bsfdv.backend.domain.model.account

import com.bsfdv.backend.domain.model.core.Id
import java.util.*

class AccountId : Id {
    constructor() : super()
    constructor(rawId: UUID) : super(rawId)
    constructor(rawId: String) : super(rawId)
}

data class AccountHolder(val firstName: String, val lastName: String)