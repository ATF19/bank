package com.bsfdv.backend.presentation.rest.transfer

import com.bsfdv.backend.application.transfer.DoTransferCommand
import com.bsfdv.backend.application.transfer.TransferAppService
import com.bsfdv.backend.domain.model.account.AccountId
import com.bsfdv.backend.domain.model.transfer.TransferId
import com.bsfdv.backend.domain.model.transfer.TransferMotif
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
@RequestMapping("/transfer")
@Transactional
class TransferRestService(private val transferAppService: TransferAppService) {

    private val logger = Logger.getLogger(this::class.qualifiedName)

    @GetMapping("/pending")
    @Operation(summary = "Retrieve pending transfers.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retrieved pending transfers."),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when retrieving pending transfers.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun pending(): List<TransferDto> {
        logger.info("Retrieving pending transfers.")
        return transferAppService.pending()
            .map { TransferDto.from(it) }
    }

    @GetMapping("/completed")
    @Operation(summary = "Retrieve completed transfers.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retrieved completed transfers."),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when completed pending transfers.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun completed(): List<TransferDto> {
        logger.info("Retrieving completed transfers.")
        return transferAppService.completed()
            .map { TransferDto.from(it) }
    }

    @GetMapping("/rejected")
    @Operation(summary = "Retrieve rejected transfers.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retrieved rejected transfers."),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when retrieving rejected transfers.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun rejected(): List<TransferDto> {
        logger.info("Retrieving rejected transfers.")
        return transferAppService.rejected()
            .map { TransferDto.from(it) }
    }

    @GetMapping("/{id}")
    @Operation(operationId = "Transfer status", summary = "Retrieve transfer.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retrieved transfer."),
            ApiResponse(
                responseCode = "404",
                description = "No transfer is found with given ID.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when retrieving transfer.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun byId(@RequestParam("id") id: String): TransferDto {
        logger.info("Retrieving transfer by ID $id.")
        return TransferDto.from(transferAppService.by(TransferId(id)))
    }

    @PostMapping
    @Operation(summary = "Do a transfer.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Transfer requested with success.",
            ),
            ApiResponse(
                responseCode = "400",
                description = "The transfer parameters are not correct, please verify the given inputs.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "An error occurred when doing the transfer.",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    fun doTransfer(@RequestBody request: DoTransferRequestDto): TransferDto {
        logger.info("Doing a transfer.")
        verifyAllInputsHaveValues(
            Pair("Source", request.source),
            Pair("Destination", request.destination),
            Pair("Amount", request.amount),
            Pair("Motif", request.motif)
        )
        val command =
            DoTransferCommand(
                AccountId(request.source), AccountId(request.destination),
                getAmountFromRawString(request.amount), TransferMotif(request.motif)
            )
        val transfer = transferAppService.doTransfer(command)
        return TransferDto.from(transfer)
    }
}