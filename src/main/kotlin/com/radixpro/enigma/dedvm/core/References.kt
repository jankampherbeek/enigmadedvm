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

enum class EmptyPoints(override val id: Int, override val nameTxt: String): Points {
    EXISTS_NOT(1000, "Does not exist"),
    ERROR(1001, "Result of error"),
    TOTAL(1002, "Placeholder for total")
}


/**
 * Supported aspects.
 */
enum class Aspects(val degrees: Double, val orbForLights: Double, val orb: Double, val nameTxt: String) {
    CONJUNCTION(0.0, 8.0, 6.0, "Conjunction"),
    OPPOSITION(180.0, 8.0, 6.0,"Opposition"),
    TRIGON(120.0, 8.0, 6.0,"Trigon"),
    SQUARE(90.0, 8.0, 6.0,"Square"),
    SEXTILE(60.0, 6.0, 4.0, "Sextile"),
    INCONJUNCT(150.0, 3.5, 3.0, "Inconjunct")
}

enum class Signs(val index: Int, val nameTxt:String, val abbr: String, val strong: Points, val weak: Points, val exalt: Points, val fall: Points) {
    ARIES(1, "Aries", "AR", CelPoints.MARS, CelPoints.VENUS,  CelPoints.SUN, CelPoints.SATURN),
    TAURUS(2,"Taurus", "TA", CelPoints.VENUS, CelPoints.MARS, CelPoints.MOON, EmptyPoints.EXISTS_NOT),
    GEMINI(3, "Gemini", "GE", CelPoints.MERCURY, CelPoints.JUPITER, EmptyPoints.EXISTS_NOT, EmptyPoints.EXISTS_NOT),
    CANCER(4, "Cancer", "CN", CelPoints.MOON, CelPoints.SATURN, CelPoints.JUPITER, CelPoints.MARS),
    LEO(5, "Leo", "LE", CelPoints.SUN, CelPoints.URANUS, EmptyPoints.EXISTS_NOT, EmptyPoints.EXISTS_NOT),
    VIRGO(6, "Virgo", "VI", CelPoints.MERCURY, CelPoints.NEPTUNE, CelPoints.MERCURY, CelPoints.VENUS),
    LIBRA(7, "Libra", "LI", CelPoints.VENUS, CelPoints.MARS, CelPoints.SATURN, CelPoints.SUN),
    SCORPIO(8, "Scorpio", "SC", CelPoints.PLUTO, CelPoints.VENUS, EmptyPoints.EXISTS_NOT, CelPoints.MOON),
    SAGITTARIUS(9, "Sagittarius", "SA", CelPoints.JUPITER, CelPoints.MERCURY, EmptyPoints.EXISTS_NOT, EmptyPoints.EXISTS_NOT),
    CAPRICORN(10, "Capricorn", "CP", CelPoints.SATURN, CelPoints.MOON, EmptyPoints.EXISTS_NOT, CelPoints.JUPITER),
    AQUARIUS(11, "Aquarius", "AQ", CelPoints.URANUS, CelPoints.SUN, EmptyPoints.EXISTS_NOT, EmptyPoints.EXISTS_NOT),
    PISCES(12, "Pisces", "PI", CelPoints.NEPTUNE, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MERCURY)
}
