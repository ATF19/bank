package com.bsfdv.backend.infrastructure.persistence

import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.domain.testUtils.TestEntity
import com.bsfdv.backend.domain.testUtils.TestName
import com.bsfdv.backend.infrastructure.springboot.BackendApplication
import com.bsfdv.backend.suites.INTEGRATION_TEST
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Tag(INTEGRATION_TEST)
@SpringBootTest(classes = [BackendApplication::class])
internal class SqlEventWriterTest {

    @Autowired
    private lateinit var eventWriter: EventWriter

    @Autowired
    private lateinit var datasource: DataSource

    @Autowired
    private lateinit var flyway: Flyway

    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var testEntity: TestEntity
    private lateinit var unitOfWork: UnitOfWork

    @BeforeEach
    fun setup() {
        flyway.clean()
        flyway.migrate()
        jdbcTemplate = JdbcTemplate(datasource)
        testEntity = TestEntity.create(TestName("Testing the creation"))
        unitOfWork = UnitOfWork(testEntity)
    }

    @Test
    fun can_save_unit_of_work() {
        // given

        // when
        eventWriter.save(unitOfWork)

        // then
        assertThat(numberOfEventsInDb()).isEqualTo(1)
    }

    @Test
    fun only_persist_unsaved_events() {
        // given
        val testEntity2 = TestEntity.create(TestName("Another test entity"))
        unitOfWork = UnitOfWork(testEntity, testEntity2)

        // when
        eventWriter.save(unitOfWork)

        // then
        assertThat(numberOfEventsInDb()).isEqualTo(2)
    }

    private fun numberOfEventsInDb(): Int {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM $EVENTS_TABLE", Int::class.java) ?: 0
    }
}