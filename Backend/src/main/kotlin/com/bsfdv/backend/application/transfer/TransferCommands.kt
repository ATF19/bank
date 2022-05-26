package com.bsfdv.backend.application.transfer

import com.bsfdv.backend.application.core.Command
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.model.transfer.TransferMotif

class DoTransferCommand(
    val source: AccountId,
    val destination: AccountId,
    val amount: Money,
    val motif: TransferMotif
) : Command

class CompleteTransferCommand(val transferId: TransferId) : Command

class RejectTransferCommand(val transferId: TransferId) : Command
