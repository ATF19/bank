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

        if (id != other.id) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (deleted != other.deleted) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + deleted.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }

    override fun toString(): String {
        return "DomainEntity(id=$id, createdAt=$createdAt, updatedAt=$updatedAt, deleted=$deleted, version=$version)"
    }
}