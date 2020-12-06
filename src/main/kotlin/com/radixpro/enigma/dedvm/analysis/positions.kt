package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.core.Location

class SignPosition {

    fun idOfSign(lon: Double): Int {
        val degrees = lon.toInt()
        val previousSigns = degrees / 30
        return previousSigns + 1
    }
}

class HousePosition() {

    fun idOfHouse(eclLon: Double, jdUt: Double, flags: Int, location: Location): Int {
        val armc = SeFrontend.defineArmc(jdUt, flags, location)
        val epsilon = SeFrontend.defineEpsilon(jdUt, flags)
        return SeFrontend.defineEclipticalHousePosition(armc, location.geoLat, epsilon, eclLon)
    }

}