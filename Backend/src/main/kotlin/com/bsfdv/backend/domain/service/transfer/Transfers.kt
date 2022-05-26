package com.bsfdv.backend.domain.service.transfer

import com.bsfdv.backend.domain.model.transfer.Transfer
import com.bsfdv.backend.domain.model.transfer.TransferId

interface Transfers {
    fun by(id: TransferId): Transfer
    fun pending(): List<Transfer>
    fun completed(): List<Transfer>
    fun rejected(): List<Transfer>
}