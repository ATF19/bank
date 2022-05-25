package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.domain.model.account.Account
import javax.validation.constraints.NotNull

data class AccountDto(val id: String, val firstName: String, val lastName: String, val balance: String) {
    companion object {
        fun from(account: Account) = AccountDto(
            account.id.rawId.toString(), account.holder.firstName, account.holder.lastName,
            account.balance.toString()
        )
    }
}

data class OpenAccountRequestDto(
    @NotNull val firstName: String,
    @NotNull val lastName: String,
    @NotNull val initialBalance: String
)

data class UpdateHolderInformationRequestDto(@NotNull val firstName: String, @NotNull val lastName: String)

data class DepositAmountRequestDto(@NotNull val amountToDeposit: String)

data class WithdrawAmountRequestDto(@NotNull val amountToWithdraw: String)