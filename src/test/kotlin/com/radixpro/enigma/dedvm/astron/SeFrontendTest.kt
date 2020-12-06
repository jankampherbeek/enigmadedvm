package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.DateTimeParts
import io.kotest.matchers.doubles.plusOrMinus
import org.junit.jupiter.api.Test
import io.kotest.matchers.shouldBe

internal class SeFrontendTest {

    @Test
    fun `Calculation of Julian Day Number for a given date should return the expected result`() {
        val dtParts = DateTimeParts(2020, 12, 6, 10, 56, 0, 1.0)
        SeFrontend.defineJdUt(dtParts) shouldBe (2459189.91388889 plusOrMinus 0.000001)
    }
}

