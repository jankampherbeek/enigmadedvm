/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.core.Location

/**
 * Position of a specific longitude in ecliptical signs.
 */
class SignPosition {

    /**
     * Id of the sign where the point with given longitude (lon) is positioned.
     * Id is 1 for Aries ... 12 for Pisces
     */
    fun idOfSign(lon: Double): Int {
        val degrees = lon.toInt()
        val previousSigns = degrees / 30
        return previousSigns + 1
    }
}

/**
 * Position of a specific longitude in Placidus houses. Latitude is ignored.
 */
class HousePosition(private val seFrontend: SeFrontend) {

    /**
     * Id of the house where the point with given longitude (eclLon) is positioned.
     * Id is 1 for house 1 ... 12 for house 12.
     */
    fun idOfHouse(eclLon: Double, jdUt: Double, flags: Int, location: Location): Int {
        val armc = seFrontend.defineArmc(jdUt, flags, location)
        val epsilon = seFrontend.defineEpsilon(jdUt, flags)
        return seFrontend.defineEclipticalHousePosition(armc, location.geoLat, epsilon, eclLon)
    }

}

