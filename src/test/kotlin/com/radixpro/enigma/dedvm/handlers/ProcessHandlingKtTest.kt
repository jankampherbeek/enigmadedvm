
/*
 *
 *  * Jan Kampherbeek, (c) 2020, 2021.
 *  * EnigmaDedVM is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.handlers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ProcessHandlingKtTest {

    @Test
    fun `The orb rule should not be applied if not relevant`() {
        val lon = 110.0
        val cusp = 100.0
        val speed = 1.0
        val house = 3
        checkForCuspOrb(lon, speed, house, cusp) shouldBe 3
    }

    @Test
    fun `The orb rule should be correctly applied if relevant`() {
        val lon = 170.981
        val cusp = 173.1
        val speed = 12.0
        val house = 12
        checkForCuspOrb(lon, speed, house, cusp) shouldBe 1
    }

    @Test
    fun `The orb rule should be correctly applied at corners using larger orb`() {
        val lon = 170.981
        val cusp = 174.1
        val speed = 12.0
        val house = 12
        checkForCuspOrb(lon, speed, house, cusp) shouldBe 1
    }

}


//fun checkForCuspOrb(lon: Double, speed: Double, house: Int, cusp: Double): Int {