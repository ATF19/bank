package com.bsfdv.backend.domain.model.core

import com.bsfdv.backend.domain.testUtils.TestId
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.*

@Tag(UNIT_TEST)
internal class IdTest {

    @Test
    fun can_generate_id() {
        // given

        // when
        val id = TestId()

        // then
        assertThat(id.rawId).isNotNull
    }

    @Test
    fun can_create_id_from_existing_uuid() {
        // given
        val uuid = UUID.randomUUID()

        // when
        val id = TestId(uuid)

        // then
        assertThat(id.rawId).isEqualTo(uuid)
    }

    @Test
    fun can_create_id_from_existing_raw_uuid() {
        // given
        val uuid = UUID.randomUUID()
        val rawUuid = uuid.toString()

        // when
        val id = TestId(rawUuid)

        // then
        assertThat(id.rawId).isEqualTo(uuid)
    }
}