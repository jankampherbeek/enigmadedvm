package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.core.PointPosition
import com.radixpro.enigma.dedvm.core.CelPoints
import com.radixpro.enigma.dedvm.core.Location
import com.radixpro.enigma.dedvm.util.Range

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
class HousePosition {

    /**
     * If od the house wwwhere the point with given longitude (eclLon) is positioned.
     * Id is 1 for house 1 ... 12 for house 12.
     */
    fun idOfHouse(eclLon: Double, jdUt: Double, flags: Int, location: Location): Int {
        val armc = SeFrontend.defineArmc(jdUt, flags, location)
        val epsilon = SeFrontend.defineEpsilon(jdUt, flags)
        return SeFrontend.defineEclipticalHousePosition(armc, location.geoLat, epsilon, eclLon)
    }

}

/**
 * Distance from Mc in longitude.
 */
class McDistance {

    /**
     * Find celestial object that is closest to te MC, measured alongside the ecliptic and ignoring latitude.
     */
    fun closestToMc(points: List<PointPosition>, longMc: Double): CelPoints {
        var shortestDistance = 180.0
        var distance: Double
        var shortestPoint = CelPoints.SUN
        for (celPoint in points) {
            distance = Range.checkValue(celPoint.lon - longMc, 0.0, 180.0)
            if (distance <= shortestDistance) {
                shortestDistance = distance
                shortestPoint = celPoint.point as CelPoints
            }
        }
        return shortestPoint
    }

}