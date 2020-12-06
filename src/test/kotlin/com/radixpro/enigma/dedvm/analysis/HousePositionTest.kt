package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.core.Location
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class HousePositionTest {

    private val jdUt = 2459189.913888888889     // 2020/12/6 9:56 UT
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
    private val gLat = 52.0
    private val gLon = 6.9
    private val location = Location(gLat, gLon)

    @Test
    fun `HousePosition should return correct house number for a given longitude`() {
        val eclLon = 18.0
        HousePosition().idOfHouse(eclLon, jdUt, flags, location) shouldBe 2
    }


}