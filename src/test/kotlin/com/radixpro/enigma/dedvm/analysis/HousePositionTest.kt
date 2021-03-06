/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.core.Location
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class HousePositionTest {

    private val jdUt = 2459189.913888888889     // 2020/12/6 9:56 UT
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
    private val gLat = 52.0
    private val gLon = 6.9
    private val location = Location(gLat, gLon)
    private val seFrontend = SeFrontend         // no mock but the real instance, mocking does not add value in this case

    @Test
    fun `HousePosition should return correct house number for a given longitude`() {
        val eclLon = 18.0
        HousePosition(seFrontend).idOfHouse(eclLon, jdUt, flags, location) shouldBe 2
    }


}