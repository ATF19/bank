package com.bsfdv.backend.infrastructure.persistence

import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.service.core.EventReader
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.domain.testUtils.TestEntity
import com.bsfdv.backend.domain.testUtils.TestName
import com.bsfdv.backend.infrastructure.springboot.BackendApplication
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@Tag(com.bsfdv.backend.suites.INTEGRATION_TEST)
@SpringBootTest(classes = [BackendApplication::class])
internal class SqlEventReaderTest {

    @Autowired
    private lateinit var eventReader: EventReader

    @Autowired
    private lateinit var eventWriter: EventWriter

    @Autowired
    private lateinit var flyway: Flyway

    private lateinit var testEntity: TestEntity

    @BeforeEach
    fun setup() {
        flyway.clean()
        flyway.migrate()
        testEntity = TestEntity.create(TestName("Testing the creation"))
        val unitOfWork = UnitOfWork(testEntity)
        eventWriter.save(unitOfWork)
    }

    @Test
    fun can_read_events_by_id_from_db() {
        // given

        // when
        val result = eventReader.by(testEntity.id)

        // then
        assertThat(result.events()).containsExactlyInAnyOrderElementsOf(testEntity.history.events())
    }

    @Test
    fun can_read_events_by_type_from_db() {
        // given

        // when
        val result = eventReader.byType(TestEntity::class)

        // then
        assertThat(result).hasSize(1)
        assertThat(result[0].events()).containsExactlyInAnyOrderElementsOf(testEntity.history.events())
    }
}