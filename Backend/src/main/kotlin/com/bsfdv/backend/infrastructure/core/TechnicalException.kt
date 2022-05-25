package com.bsfdv.backend.infrastructure.core

open class TechnicalException(message: String, e: Throwable) : RuntimeException(message, e)