package com.bsfdv.backend.presentation.rest.core

enum class ErrorCode {
    INTERNAL_ERROR, MISSING_INFORMATION, WRONG_VALUE, NO_SUCH_ELEMENT,
    NOT_SUFFICIENT_BALANCE, NON_ZERO_BALANCE
}

data class ErrorDto(val code: ErrorCode, val message: String)
