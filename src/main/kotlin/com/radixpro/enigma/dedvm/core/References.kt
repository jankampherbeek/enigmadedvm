/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

@file:Suppress("unused", "unused", "unused")

package com.radixpro.enigma.dedvm.core

interface Points{
    val id: Int
    val nameTxt: String
}


/**
 * Supported celestial points. Id is the same as seId for the Swiss Ephemeris.
 */
enum class CelPoints(override val id: Int, override val nameTxt: String): Points {
    SUN(0, "Sun"),
    MOON(1, "Moon"),
    MERCURY(2, "Mercury"),
    VENUS(3, "Venus"),
    MARS(4, "Mars"),
    JUPITER(5, "Jupiter"),
    SATURN(6, "Saturn"),
    URANUS(7, "Uranus"),
    NEPTUNE(8, "Neptune"),
    PLUTO(9, "Pluto"),
    MEAN_NODE(10, "Lunar Node"),
    MEAN_APOGEE(12, "Black Moon"),
    CHIRON(15, "Cheiron")
}

/**
 * Major mundanepoints (as used in aspects)
 */
enum class MundanePoints(override val id: Int, override val nameTxt: String): Points {
    MC(100,"Mc"),
    ASC(101,"Ascendant")
}

/**
 * Filler to be used if a point does not (yet) exist.
 */
enum class EmptyPoints(override val id: Int, override val nameTxt: String): Points {
    EXISTS_NOT(1000, "Does not exist"),
    TOTAL(1002, "Placeholder for total")
}


/**
 * Supported aspects and their orbs.
 */
enum class Aspects(val degrees: Double, val orbForLights: Double, val orb: Double) {
    CONJUNCTION(0.0, 8.0, 6.0),
    OPPOSITION(180.0, 8.0, 6.0),
    TRIGON(120.0, 8.0, 6.0),
    SQUARE(90.0, 8.0, 6.0),
    SEXTILE(60.0, 6.0, 4.0),
    INCONJUNCT(150.0, 3.5, 3.0)
}

/**
 * Ecliptical signs and their rulers and exaltated bodies.
 */
enum class Signs(val index: Int, val abbr: String, val strong: Points, val weak: Points, val fall: Points) {
    ARIES(1, "AR", CelPoints.MARS, CelPoints.VENUS, CelPoints.SATURN),
    TAURUS(2, "TA", CelPoints.VENUS, CelPoints.MARS, EmptyPoints.EXISTS_NOT),
    GEMINI(3, "GE", CelPoints.MERCURY, CelPoints.JUPITER, EmptyPoints.EXISTS_NOT),
    CANCER(4, "CN", CelPoints.MOON, CelPoints.SATURN, CelPoints.MARS),
    LEO(5, "LE", CelPoints.SUN, CelPoints.URANUS, EmptyPoints.EXISTS_NOT),
    VIRGO(6, "VI", CelPoints.MERCURY, CelPoints.NEPTUNE, CelPoints.VENUS),
    LIBRA(7, "LI", CelPoints.VENUS, CelPoints.MARS, CelPoints.SUN),
    SCORPIO(8, "SC", CelPoints.PLUTO, CelPoints.VENUS, CelPoints.MOON),
    SAGITTARIUS(9, "SA", CelPoints.JUPITER, CelPoints.MERCURY, EmptyPoints.EXISTS_NOT),
    CAPRICORN(10, "CP", CelPoints.SATURN, CelPoints.MOON, CelPoints.JUPITER),
    AQUARIUS(11, "AQ", CelPoints.URANUS, CelPoints.SUN, EmptyPoints.EXISTS_NOT),
    PISCES(12, "PI", CelPoints.NEPTUNE, CelPoints.MERCURY, CelPoints.MERCURY)
}
