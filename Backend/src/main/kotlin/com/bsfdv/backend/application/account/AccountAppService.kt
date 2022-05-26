package com.bsfdv.backend.application.account

import com.bsfdv.backend.application.core.ApplicationService
import com.bsfdv.backend.domain.model.account.Account
import com.bsfdv.backend.domain.model.account.AccountNumber
import com.bsfdv.backend.domain.model.core.DomainException
import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.service.account.AccountNumberGenerator
import com.bsfdv.backend.domain.service.account.Accounts
import com.bsfdv.backend.domain.service.core.EventWriter

@ApplicationService
class AccountAppService(
    private val accounts: Accounts, private val accountNumberGenerator: AccountNumberGenerator,
    private val eventWriter: EventWriter
) {

    fun all() = accounts.all()

    fun openAccount(openAccountCommand: OpenAccountCommand): Account {
        val number = generateUniqueNumber()
        val account = Account.openAccount(number, openAccountCommand.holder, openAccountCommand.initialBalance)
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

    fun transferAmount(command: TransferAmountCommand): Account {
        val account = accounts.by(command.accountId)
        account.transfer(command.amountToTransfer, command.destination)
        return saveAndReturnAccount(account)
    }

    fun receiveAmount(command: ReceiveAmountCommand): Account {
        val account = accounts.by(command.accountId)
        account.receive(command.amountToReceive, command.source)
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

    private fun generateUniqueNumber(): AccountNumber {
        val maxRetries = 20
        var retryNumber = 1
        var number: AccountNumber
        do {
            if (retryNumber > maxRetries)
                throw DomainException("Could not generate a unique account number. Please try again.")

            number = accountNumberGenerator.generate()
            retryNumber++
        } while (accounts.by(number) != null)
        return number
    }
}