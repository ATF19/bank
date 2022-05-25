package com.bsfdv.backend.domain.model.core

import java.time.Instant

abstract class DomainEntity<TId : Id> {
    val id: TId
    val createdAt: Instant
    var updatedAt: Instant? private set
    var deleted: Boolean private set
    var version: Version private set
    var history: EventStream<TId> private set

    constructor(history: EventStream<TId>) {
        this.id = history.id()
        this.history = history
        createdAt = history.creationTime()
        version = START_VERSION
        updatedAt = null
        deleted = false
        applyHistory()
    }

    abstract fun apply(event: Event<TId>)


    fun addToHistoryAndApply(event: Event<TId>) {
        history.append(event)
        applyInternal(event)
    }

    private fun applyHistory() {
        for (event in history.events())
            applyInternal(event)
    }

    private fun applyInternal(event: Event<TId>) {
        if (event.isForDeletion)
            deleted = true
        else
            apply(event)
        version = event.version
        updatedAt = event.time
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DomainEntity<*>) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "DomainEntity(id=$id, createdAt=$createdAt, updatedAt=$updatedAt, deleted=$deleted, version=$version)"
    }
}