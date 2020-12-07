package com.radixpro.enigma.dedvm.core

interface Points{
    val id: Int
    val nameTxt: String
}


/**
 * Supported celestial points. Id is the same as seId for the Swiss Ephemeris.
 */
enum class CelPoints(override val id: Int, val glyph: String, override val nameTxt: String): Points {
    SUN(0, "a", "Sun"),
    MOON(1,"b","Moon"),
    MERCURY(2,"c","Mercury"),
    VENUS(3,"d","Venus"),
    MARS(4,"f","Mars"),
    JUPITER(5,"g","Jupiter"),
    SATURN(6,"h","Saturn"),
    URANUS(7,"i","Uranus"),
    NEPTUNE(8,"j","Neptune"),
    PLUTO(9,"k","Pluto"),
    MEAN_NODE(10,"{","Lunar Node"),
    MEAN_APOGEE(12,",","Black Moon"),
    CHIRON(15,"w","Cheiron")
}

/**
 * Major mundanepoints (as used in aspects)
 */
enum class MundanePoints(override val id: Int, override val nameTxt: String): Points {
    MC(100,"Mc"),
    ASC(101,"Ascendant")
}


/**
 * Supported aspects.
 */
enum class Aspects(val degrees: Double, val orb: Double, val nameTxt: String) {
    CONJUNCTION(0.0, 8.0, "Conjunction"),
    OPPOSITION(180.0, 8.0, "Opposition"),
    TRIGON(120.0, 7.0, "Trigon"),
    SQUARE(90.0, 7.0, "Square"),
    SEXTILE(60.0, 6.0, "Sextile")
}
