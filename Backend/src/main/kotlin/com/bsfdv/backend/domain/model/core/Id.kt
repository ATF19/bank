package com.bsfdv.backend.domain.model.core

import java.util.*

abstract class Id(val rawId: UUID) {

    constructor() : this(UUID.randomUUID())

    constructor(rawId: String) : this(UUID.fromString(rawId))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Id) return false

        if (rawId != other.rawId) return false

        return true
    }

    override fun hashCode(): Int {
        return rawId.hashCode()
    }

    override fun toString(): String {
        return "Id(rawId=$rawId)"
    }
}