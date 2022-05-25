package com.bsfdv.backend.domain.model.account

import com.bsfdv.backend.domain.model.account.event.*
import com.bsfdv.backend.domain.model.common.Money
import com.bsfdv.backend.domain.model.core.*
import java.time.Instant

open class Account(history: EventStream<AccountId>) : DomainEntity<AccountId>(history) {

    lateinit var holder: AccountHolder private set
    lateinit var balance: Money private set

    companion object {
        fun openAccount(holder: AccountHolder, initialBalance: Money): Account {
            verifyBalance(initialBalance)
            val accountOpened = AccountOpened(AccountId(), holder, initialBalance, START_VERSION, Instant.now())
            val eventStream = EventStream(accountOpened)
            return Account(eventStream)
        }

        private fun verifyBalance(initialBalance: Money) {
            if (initialBalance.amount.signum() < 0)
                throw InvalidInitialBalance(initialBalance)
        }
    }

    fun updateHolderInformation(newHolderInfo: AccountHolder) {
        val holderInformationUpdated = HolderInformationUpdated(id, newHolderInfo, version.next(), Instant.now())
        addToHistoryAndApply(holderInformationUpdated)
    }

    fun deposit(amount: Money) {
        val moneyDeposited = MoneyDeposited(id, amount, version.next(), Instant.now())
        addToHistoryAndApply(moneyDeposited)
    }

    fun withdraw(amount: Money) {
        verifyCanWithdrawAmount(amount)
        val moneyWithdrew = MoneyWithdrew(id, amount, version.next(), Instant.now())
        addToHistoryAndApply(moneyWithdrew)
    }

    fun closeAccount() {
        verifyAccountHasNoBalance()
        val accountClosed = AccountClosed(id, version.next(), Instant.now())
        addToHistoryAndApply(accountClosed)
    }

    private fun verifyAccountHasNoBalance() {
        if (!balance.isZero())
            throw ClosingAccountWithBalance()
    }

    private fun verifyCanWithdrawAmount(amountToWithdraw: Money) {
        if (balance.minus(amountToWithdraw).isBelowZero())
            throw NoSufficientBalanceForWithdrawal(amountToWithdraw)
    }

    override fun apply(event: Event<AccountId>) {
        when (event) {
            is AccountOpened -> applyAccountOpened(event)
            is HolderInformationUpdated -> applyHolderInformationUpdated(event)
            is MoneyDeposited -> applyMoneyDeposited(event)
            is MoneyWithdrew -> applyMoneyWithdrew(event)
        }
    }

    private fun applyAccountOpened(accountOpened: AccountOpened) {
        holder = accountOpened.holder
        balance = accountOpened.initialBalance
    }

    private fun applyHolderInformationUpdated(holderInformationUpdated: HolderInformationUpdated) {
        holder = holderInformationUpdated.newHolderInformation
    }

    private fun applyMoneyDeposited(moneyDeposited: MoneyDeposited) {
        balance = balance.plus(moneyDeposited.amount)
    }

    private fun applyMoneyWithdrew(moneyWithdrew: MoneyWithdrew) {
        balance = balance.minus(moneyWithdrew.amount)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false
        if (!super.equals(other)) return false

        if (holder != other.holder) return false
        if (balance != other.balance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + holder.hashCode()
        result = 31 * result + balance.hashCode()
        return result
    }

    override fun toString(): String {
        return "Account(holder=$holder, balance=$balance)"
    }
}

