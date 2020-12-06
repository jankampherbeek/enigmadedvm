/*
 * Jan Kampherbeek, (c) 2020.
 * All Enigma projects are open source.
 * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
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

    /**
     * Calculate the longitudes for all cusps using the Placidus system.
     */
    fun defineLongitudeForPlacidus(jdUt:Double, flags: Int, location: Location) : DoubleArray {
        val cusps = DoubleArray(13)     // cusp positions will be on indexes 1..12
        val ascMc = DoubleArray(10)
        val system = 'P'.toInt()             // Placidus
        swissEph.swe_houses(jdUt, flags, location.geoLat, location.geoLon, system, cusps, ascMc)
        return cusps
    }

    /**
     * Calculate the value of true Epsilon (obliquity of the earth axis, corrected for nutation).
     */
    fun defineEpsilon(jdUt: Double, flags: Int): Double {
        val seId = -1
        val results = DoubleArray(6)
        val errorMsg = StringBuffer()
        swissEph.swe_calc_ut(jdUt, seId, flags, results, errorMsg)
        return results[0]
    }

    /**
     * Calculate right ascension of the MC.
     */
    fun defineArmc(jdUt:Double, flags: Int, location: Location): Double {
        val cusps = DoubleArray(13)
        val ascMc = DoubleArray(10)     // armc at index
        val system = 'W'.toInt()             // sign houses for fast calculation
        swissEph.swe_houses(jdUt, flags, location.geoLat, location.geoLon, system, cusps, ascMc)
        return ascMc[2]
    }


    /**
     * Define house (identified by number) for a given ecliptical longitude while ignoring the effect of latitude.
     * @param armc: Right ascension of the MC
     * @param geoLat: geographical latitude
     * @param epsilon: true obliquity of the earth axis
     * @param eclLon: ecliptical longitude
     */
    fun defineEclipticalHousePosition(armc: Double, geoLat: Double, epsilon: Double, eclLon: Double): Int {
        val hSys = 'P'.toInt()
        val lonLat= DoubleArray(2)
        lonLat[0] = eclLon
        lonLat[1] = 0.0
        val errorMsg = StringBuffer()
        return swissEph.swe_house_pos(armc, geoLat, epsilon, hSys, lonLat, errorMsg).toInt()
    }

}