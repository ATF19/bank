package com.bsfdv.backend.domain.model.core

open class DomainException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)
    constructor(message: String, throwable: Throwable) : super(message, throwable)
}