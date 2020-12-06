package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.DateTimeParts
import io.kotest.matchers.doubles.plusOrMinus
import org.junit.jupiter.api.Test
import io.kotest.matchers.shouldBe

internal class SeFrontendTest {

   private val delta = 0.0000001

    @Test
    fun `Calculation of Julian Day Number for a given date should return the expected result`() {
        val dtParts = DateTimeParts(2020, 12, 6, 10, 56, 0, 1.0)
        SeFrontend.defineJdUt(dtParts) shouldBe (2459189.91388889 plusOrMinus delta)
    }

    @Test
    fun `Calculation for a celestial body should give the correct longitude and speed` () {
        val jdUt = 2459189.913888888889    // 2020/12/6 9:56 UT
        val seId = 0    // Sun
        val flags = 0 or 2 or 256   // 2 = SwissEph, 256 = speed
        val results = SeFrontend.defineLongitudeForBody(jdUt, seId, flags)
        results[0] shouldBe (254.73672479 plusOrMinus delta)
        results[1] shouldBe (1.0151252 plusOrMinus delta)
    }
}

