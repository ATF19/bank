package com.bsfdv.backend.presentation.rest.transfer

import com.bsfdv.backend.domain.model.transfer.Transfer
import io.swagger.v3.oas.annotations.media.Schema

data class TransferDto(
    val id: String,
    val source: String,
    val destination: String,
    val amount: String,
    val motif: String,
    val status: String
) {
    companion object {
        fun from(transfer: Transfer) = TransferDto(
            transfer.id.rawId.toString(), transfer.source.rawId.toString(), transfer.destination.rawId.toString(),
            transfer.amount.toString(), transfer.motif.motif, transfer.status.toString()
        )
    }
}

data class DoTransferRequestDto(
    @Schema(
        description = "Id of the account to transfer money from.",
        required = true,
        example = "f10b0154-e52c-4fb6-b60c-58fd29257c89"
    ) val source: String,
    @Schema(
        description = "Id of the account to transfer money to.",
        required = true,
        example = "3dde8c7f-63ad-4f9d-93b6-1d882fa210d8"
    ) val destination: String,
    @Schema(
        description = "The amount of money to transfer.",
        required = true,
        example = "1500.50"
    ) val amount: String,
    @Schema(
        description = "Motif of the transfer.",
        required = true,
        example = "Wire transfer to my child."
    ) val motif: String
)