package com.bsfdv.backend.application.account

import com.bsfdv.backend.application.core.Command
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money

class OpenAccountCommand(val holder: AccountHolder, val initialBalance: Money) : Command

class UpdateHolderInformationCommand(val accountId: AccountId, val newHolderInfo: AccountHolder) : Command

class DepositAmountCommand(val accountId: AccountId, val amountToDeposit: Money) : Command

class WithdrawAmountCommand(val accountId: AccountId, val amountToWithdraw: Money) : Command

class CloseAccountCommand(val accountId: AccountId) : Command