package com.bsfdv.backend.presentation.rest.account

import com.bsfdv.backend.application.account.*
import com.bsfdv.backend.domain.model.account.AccountHolder
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.presentation.core.getAmountFromRawString
import com.bsfdv.backend.presentation.core.verifyAllInputsHaveValues
import com.bsfdv.backend.presentation.rest.core.ErrorDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger


@RestController
@RequestMapping("/account")
@Transactional
class AccountRestService(private val accountAppService: AccountAppService) {

    private val logger = Logger.getLogger(this::class.qualifiedName)

    @GetMapping
    @Operation(summary = "Retrieve all accounts.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retrieved all accounts."),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when retrieving all accounts.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun all(): List<AccountDto> {
        logger.info("Retrieving all accounts.")
        return accountAppService.all()
            .map { AccountDto.from(it) }
    }

    @PostMapping
    @Operation(summary = "Open an account.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Account opened with success."),
            ApiResponse(
                responseCode = "400",
                description = "An error occurred when opening the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when opening the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun openAccount(@RequestBody request: OpenAccountRequestDto): AccountDto {
        logger.info("Opening an account.")
        verifyAllInputsHaveValues(
            Pair("First name", request.firstName),
            Pair("Last name", request.lastName),
            Pair("Initial balance", request.initialBalance)
        )
        val command =
            OpenAccountCommand(
                AccountHolder(request.firstName, request.lastName),
                getAmountFromRawString(request.initialBalance)
            )
        val account = accountAppService.openAccount(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/update-info")
    @Operation(summary = "Update account information.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Account updated with success."),
            ApiResponse(
                responseCode = "400",
                description = "An error occurred when updating the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No account was found for the given ID.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when updating the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun updateHolderInformation(
        @RequestParam("id") id: String,
        @RequestBody request: UpdateHolderInformationRequestDto
    ): AccountDto {
        logger.info("Updating account information.")
        verifyAllInputsHaveValues(Pair("First name", request.firstName), Pair("Last name", request.lastName))
        val command = UpdateHolderInformationCommand(AccountId(id), AccountHolder(request.firstName, request.lastName))
        val account = accountAppService.updateHolderInformation(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/deposit")
    @Operation(summary = "Deposit amount.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Money deposited with success."),
            ApiResponse(
                responseCode = "400",
                description = "An error occurred when depositing the amount.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No account was found for the given ID.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when depositing the amount.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun depositAmount(@RequestParam("id") id: String, @RequestBody request: DepositAmountRequestDto): AccountDto {
        logger.info("Depositing money.")
        verifyAllInputsHaveValues(Pair("Amount", request.amountToDeposit))
        val command = DepositAmountCommand(AccountId(id), getAmountFromRawString(request.amountToDeposit))
        val account = accountAppService.depositAmount(command)
        return AccountDto.from(account)
    }

    @PutMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw amount.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Money withdrew with success."),
            ApiResponse(
                responseCode = "400",
                description = "An error occurred when withdrawing the amount.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No account was found for the given ID.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when withdrawing the amount.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun withdrawAmount(@RequestParam("id") id: String, @RequestBody request: WithdrawAmountRequestDto): AccountDto {
        logger.info("Withdrawing money.")
        verifyAllInputsHaveValues(Pair("Amount", request.amountToWithdraw))
        val command = WithdrawAmountCommand(AccountId(id), getAmountFromRawString(request.amountToWithdraw))
        val account = accountAppService.withdrawAmount(command)
        return AccountDto.from(account)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Close account.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Account closed with success."),
            ApiResponse(
                responseCode = "400",
                description = "An error occurred when closing the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No account was found for the given ID.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when closing the account.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun closeAccount(@RequestParam("id") id: String) {
        logger.info("Closing account.")
        val command = CloseAccountCommand(AccountId(id))
        accountAppService.closeAccount(command)
    }
}