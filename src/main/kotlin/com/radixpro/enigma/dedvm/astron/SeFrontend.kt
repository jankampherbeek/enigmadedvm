/*
 * Jan Kampherbeek, (c) 2020.
 * All Enigma projects are open source.
 * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.DateTimeParts
import swisseph.SweDate
import swisseph.SwissEph


/**
 * Frontend for the Thomas Mack port to the Swiss Ephemeris. Provides essential astronomical calculations.</br>
 * Implemented as a singleton to prevent multiple instantiations.
 */
object SeFrontend {

    private const val PATH: String = "./se"
    private const val MINUTES_PER_HOUR = 60.0
    private const val SECONDS_PER_HOUR = 3600.0
    private const val GREGORIAN = true
    private val swissEph = SwissEph(PATH)

    /**
     * Calculate Julian Day for UT.
     */
    fun defineJdUt(dtParts: DateTimeParts): Double {
        val time = ((dtParts.hour - dtParts.offsetUt) + dtParts.minute / MINUTES_PER_HOUR + dtParts.second / SECONDS_PER_HOUR)
        val sweDate = SweDate(dtParts.year, dtParts.month, dtParts.day, time, GREGORIAN)
        return sweDate.julDay
    }

    /**
     * Calculate the longitude for a celestial body.
     * @param seId: the id as used by the Swiss Ephemeris
     * @returns array with longitude at position 0 and daily speed at position 1
     */
    fun defineLongitudeForBody(jdUt: Double, seId: Int, flags: Int): DoubleArray {
        val allPositions = DoubleArray(6)
        val errorMsg = StringBuffer()
        swissEph.swe_calc_ut(jdUt, seId, flags, allPositions, errorMsg)
        val result = DoubleArray(2)
        result[0] = allPositions[0]
        result[1] = allPositions[3]
        return result
    }


}