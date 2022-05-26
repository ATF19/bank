package com.bsfdv.backend.presentation.rest.core

import com.bsfdv.backend.domain.model.account.ClosingAccountWithBalance
import com.bsfdv.backend.domain.model.account.InvalidInitialBalance
import com.bsfdv.backend.domain.model.account.NoSufficientBalanceForWithdrawal
import com.bsfdv.backend.domain.service.core.NoSuchEntityExists
import com.bsfdv.backend.presentation.core.EmptyRequiredInput
import com.bsfdv.backend.presentation.core.InvalidInputValue
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomExceptionMapper : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleAll(exception: Exception, request: WebRequest?): ResponseEntity<ErrorDto> {
        val status: HttpStatus
        val errorCode: ErrorCode
        var message = exception.message ?: ""

        when (exception) {
            is NoSuchEntityExists -> {
                status = HttpStatus.NOT_FOUND
                errorCode = ErrorCode.NO_SUCH_ELEMENT
            }

            is ClosingAccountWithBalance -> {
                status = HttpStatus.BAD_REQUEST
                errorCode = ErrorCode.NON_ZERO_BALANCE
            }

            is InvalidInitialBalance -> {
                status = HttpStatus.BAD_REQUEST
                errorCode = ErrorCode.WRONG_VALUE
            }

            is NoSufficientBalanceForWithdrawal -> {
                status = HttpStatus.BAD_REQUEST
                errorCode = ErrorCode.NOT_SUFFICIENT_BALANCE
            }

            is InvalidInputValue -> {
                status = HttpStatus.BAD_REQUEST
                errorCode = ErrorCode.WRONG_VALUE
            }

            is EmptyRequiredInput -> {
                status = HttpStatus.BAD_REQUEST
                errorCode = ErrorCode.MISSING_INFORMATION
            }

            else -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR
                errorCode = ErrorCode.INTERNAL_ERROR
            }
        }

        return ResponseEntity(ErrorDto(errorCode, message), HttpHeaders(), status)
    }
}