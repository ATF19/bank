package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.domain.model.account.Account
import io.swagger.v3.oas.annotations.media.Schema

data class AccountDto(val id: String, val firstName: String, val lastName: String, val balance: String) {
    companion object {
        fun from(account: Account) = AccountDto(
            account.id.rawId.toString(), account.holder.firstName, account.holder.lastName,
            account.balance.toString()
        )
    }
}

data class OpenAccountRequestDto(
    @Schema(description = "First name of the account holder", required = true, example = "John") val firstName: String,
    @Schema(description = "Last name of the account holder", required = true, example = "Doe") val lastName: String,
    @Schema(
        description = "The initial balance of the account",
        required = true,
        example = "1500.50"
    ) val initialBalance: String
)

data class UpdateHolderInformationRequestDto(
    @Schema(description = "First name of the account holder", required = true, example = "John") val firstName: String,
    @Schema(description = "Last name of the account holder", required = true, example = "Doe") val lastName: String
)

data class DepositAmountRequestDto(
    @Schema(description = "The amount to deposit.", required = true, example = "200") val amountToDeposit: String
)

data class WithdrawAmountRequestDto(
    @Schema(
        description = "The amount to deposit to withdraw.",
        required = true,
        example = "1800"
    ) val amountToWithdraw: String
)