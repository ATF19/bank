package com.bsfdv.backend.domain.model.core

import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(UNIT_TEST)
internal class VersionTest {

    @Test
    fun get_next_version() {
        // given
        val version = Version(1)

        // when
        val nextVersion = version.next()

        // then
        assertThat(nextVersion).isEqualTo(Version(2))
    }

    @Test
    fun is_after() {
        // given
        val version = Version(1)
        val next = Version(2)

        // when
        val result = next.isAfter(version)

        // then
        assertThat(result).isTrue
    }

    @Test
    fun is_not_after() {
        // given
        val version = Version(1)
        val next = Version(0)

        // when
        val result = version.isAfter(version)

        // then
        assertThat(result).isFalse
    }
}