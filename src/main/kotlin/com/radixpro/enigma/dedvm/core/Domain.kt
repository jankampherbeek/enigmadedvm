package com.radixpro.enigma.dedvm.core

data class DateTimeParts(val year: Int,
                         val month: Int,
                         val day: Int,
                         val hour: Int,
                         val minute: Int,
                         val second: Int,
                         val offsetUt: Double)

data class Location(val geoLat: Double,
                    val geoLon: Double)

data class ChartInputData(val id: Int,
                          val name: String,
                          val dateTimeParts: DateTimeParts,
                          val location: Location)

data class CelPointPosition(val point: CelPoints,
                            val lon: Double,
                            val speed: Double)

data class Chart(val id: String,
                 val name: String,
                 val location: Location,
                 val jdUt: Double,
                 val armc: Double,
                 val epsilon: Double,
                 val dateTimeParts: DateTimeParts,
                 val celPointPositions: List<CelPointPosition>,
                 val cusps: List<Double>)

