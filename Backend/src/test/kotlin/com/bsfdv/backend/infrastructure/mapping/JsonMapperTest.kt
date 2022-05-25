package com.bsfdv.backend.infrastructure.mapping

import com.bsfdv.backend.domain.model.core.START_VERSION
import com.bsfdv.backend.domain.testUtils.TestEvent
import com.bsfdv.backend.domain.testUtils.TestId
import com.bsfdv.backend.domain.testUtils.TestName
import com.bsfdv.backend.infrastructure.springboot.BackendApplication
import com.bsfdv.backend.suites.INTEGRATION_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@Tag(INTEGRATION_TEST)
@SpringBootTest(classes = [BackendApplication::class])
internal class JsonMapperTest {

    @Autowired
    private lateinit var jsonMapper: JsonMapper

    private lateinit var testEvent: TestEvent

    @BeforeEach
    fun setup() {
        testEvent = TestEvent(TestId(), START_VERSION, Instant.now(), false, TestName("Just for test"))
    }

    @Test
    fun can_serialize_and_deserialize_event() {
        // given

        // when
        val serialized = jsonMapper.write(testEvent)
        val deserialized = jsonMapper.readFromString<TestEvent>(serialized, TestEvent::class.qualifiedName!!)

        // then
        assertThat(deserialized).isEqualTo(testEvent)
    }

}