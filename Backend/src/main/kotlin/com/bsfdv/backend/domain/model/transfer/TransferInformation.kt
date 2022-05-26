package com.bsfdv.backend.domain.model.transfer

import com.bsfdv.backend.domain.model.core.Id
import java.util.*

class TransferId : Id {
    constructor() : super()
    constructor(rawId: UUID) : super(rawId)
    constructor(rawId: String) : super(rawId)
}

data class TransferMotif(val motif: String)

enum class TransferStatus { PENDING, COMPLETED, REJECTED }