package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.application.account.*
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.common.Money
import io.swagger.v3.oas.annotations.Operation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.NumberUtils
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.logging.Logger


@RestController
@RequestMapping("/account")
@Transactional
class AccountRestService(private val accountAppService: AccountAppService) {

    private val logger = Logger.getLogger(this::class.qualifiedName)

    @GetMapping
    @Operation(summary = "Retrieve all accounts.")
    fun all(): List<AccountDto> {
        logger.info("Retrieving all accounts.")
        return accountAppService.all()
            .map { AccountDto.from(it) }
    }

    @PostMapping
    @Operation(summary = "Open an account.")
    fun openAccount(@RequestBody request: OpenAccountRequestDto): AccountDto {
        logger.info("Opening an account.")
        val command =
            OpenAccountCommand(AccountHolder(request.firstName, request.lastName), getAmount(request.initialBalance))
        val account = accountAppService.openAccount(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/update-info")
    @Operation(summary = "Update account information.")
    fun updateHolderInformation(
        @RequestParam("id") id: String,
        @RequestBody request: UpdateHolderInformationRequestDto
    ): AccountDto {
        logger.info("Updating account information.")
        val command = UpdateHolderInformationCommand(AccountId(id), AccountHolder(request.firstName, request.lastName))
        val account = accountAppService.updateHolderInformation(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/deposit")
    @Operation(summary = "Deposit amount.")
    fun depositAmount(@RequestParam("id") id: String, @RequestBody request: DepositAmountRequestDto): AccountDto {
        logger.info("Depositing money.")
        val command = DepositAmountCommand(AccountId(id), getAmount(request.amountToDeposit))
        val account = accountAppService.depositAmount(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw amount.")
    fun withdrawAmount(@RequestParam("id") id: String, @RequestBody request: WithdrawAmountRequestDto): AccountDto {
        logger.info("Withdrawing money.")
        val command = WithdrawAmountCommand(AccountId(id), getAmount(request.amountToWithdraw))
        val account = accountAppService.withdrawAmount(command)
        return AccountDto.from(account)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Close account.")
    fun closeAccount(@RequestParam("id") id: String) {
        logger.info("Closing account.")
        val command = CloseAccountCommand(AccountId(id))
        accountAppService.closeAccount(command)
    }

    private fun getAmount(rawAmount: String): Money {
        val parsedAmount = NumberUtils.parseNumber(rawAmount, BigDecimal::class.java)
        return Money(parsedAmount)
    }
}