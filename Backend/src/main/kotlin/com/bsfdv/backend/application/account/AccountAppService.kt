package com.bsfdv.backend.application.account

import com.bsfdv.backend.application.core.ApplicationService
import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.service.account.Accounts
import com.bsfdv.backend.domain.service.core.EventWriter

@ApplicationService
class AccountAppService(private val accounts: Accounts, private val eventWriter: EventWriter) {

    fun all() = accounts.all()

    fun openAccount(openAccountCommand: OpenAccountCommand): Account {
        val account = Account.openAccount(openAccountCommand.holder, openAccountCommand.initialBalance)
        return saveAndReturnAccount(account)
    }

    fun updateHolderInformation(command: UpdateHolderInformationCommand): Account {
        val account = accounts.by(command.accountId)
        account.updateHolderInformation(command.newHolderInfo)
        return saveAndReturnAccount(account)
    }

    fun depositAmount(command: DepositAmountCommand): Account {
        val account = accounts.by(command.accountId)
        account.deposit(command.amountToDeposit)
        return saveAndReturnAccount(account)
    }

    fun withdrawAmount(command: WithdrawAmountCommand): Account {
        val account = accounts.by(command.accountId)
        account.withdraw(command.amountToWithdraw)
        return saveAndReturnAccount(account)
    }

    fun closeAccount(command: CloseAccountCommand): Account {
        val account = accounts.by(command.accountId)
        account.closeAccount()
        return saveAndReturnAccount(account)
    }

    private fun saveAndReturnAccount(account: Account): Account {
        val unitOfWork = UnitOfWork(account)
        eventWriter.save(unitOfWork)
        return account
    }
}