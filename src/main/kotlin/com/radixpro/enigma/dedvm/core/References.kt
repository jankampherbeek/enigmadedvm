package com.radixpro.enigma.dedvm.core

enum class CelPoints(val seId: Int, val glyph: String, val nameTxt: String) {
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

