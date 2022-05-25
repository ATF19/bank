package com.bsfdv.backend.domain.model.account

import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.DomainException

class InvalidInitialBalance(initialBalance: Money) :
    DomainException("The initial balance, ${initialBalance.amount}, is invalid.")

class ClosingAccountWithBalance : DomainException("The account must have no balance in order to be closed.")
class NoSufficientBalanceForWithdrawal(amountToWithdraw: Money) :
    DomainException("The account does not have sufficient balance to withdraw ${amountToWithdraw.amount}.")