package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
import io.kotest.matchers.doubles.plusOrMinus
import org.junit.jupiter.api.Test
import io.kotest.matchers.shouldBe

internal class SeFrontendTest {

   private val delta = 0.00001

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

    @Test
    fun `Calculation of Placidean cusps gives the expected results` () {
        val jdUt = 2459189.913888888889    // 2020/12/6 9:56 UT
        val gLat = 52.0
        val gLon = 6.9
        val location = Location(gLat, gLon)
        val flags = 0 or 2 or 256   // 2 = SwissEph, 256 = speed
        val results = SeFrontend.defineLongitudeForPlacidus(jdUt, flags, location)
        results[1] shouldBe (288.610093126 plusOrMinus delta)       // 18 gr 38' 36" Capricorn
        results[2] shouldBe (345.478322372 plusOrMinus delta)       // 15 gr 28'41" Pisces
        results[9] shouldBe (208.296500003 plusOrMinus delta)       // 28 gr 17'47" Libra
    }

    @Test
    fun `Calculation of true Epsilon gives the correct value` () {
        val jdUt = 2459189.913888888889    // 2020/12/6 9:56 UT
        val flags = 0 or 2 or 256   // 2 = SwissEph, 256 = speed
        SeFrontend.defineEpsilon(jdUt, flags) shouldBe(23.4368782 plusOrMinus delta)
    }

}

