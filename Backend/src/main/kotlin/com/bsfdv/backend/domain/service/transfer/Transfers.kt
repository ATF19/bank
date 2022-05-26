package com.bsfdv.backend.domain.service.transfer

import com.bsfdv.backend.domain.model.transfer.Transfer

interface Transfers {
    fun pending(): List<Transfer>
    fun completed(): List<Transfer>
    fun rejected(): List<Transfer>
}