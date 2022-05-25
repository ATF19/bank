package com.bsfdv.backend.suites

import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.Suite

@IncludeTags(UNIT_TEST)
@Suite
class UnitTests

const val UNIT_TEST = "unit-test"