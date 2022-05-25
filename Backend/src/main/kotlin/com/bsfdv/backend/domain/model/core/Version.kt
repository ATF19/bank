package com.bsfdv.backend.domain.model.core

data class Version(val rawVersion: Int) : Comparable<Version> {
    fun next() = Version(rawVersion + 1)
    fun isAfter(version: Version) = compareTo(version) > 0
    override fun compareTo(other: Version) = rawVersion - other.rawVersion
}

val START_VERSION = Version(1)