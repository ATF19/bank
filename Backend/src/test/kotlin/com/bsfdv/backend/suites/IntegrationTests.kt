package com.bsfdv.backend.suites

import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.Suite

@IncludeTags(INTEGRATION_TEST)
@Suite
class IntegrationTests

const val INTEGRATION_TEST = "integration-test"