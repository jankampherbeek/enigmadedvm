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

data class PointPosition(val point: Points,
                         val lon: Double,
                         val speed: Double)

data class Chart(val id: String,
                 val name: String,
                 val location: Location,
                 val jdUt: Double,
                 val armc: Double,
                 val epsilon: Double,
                 val dateTimeParts: DateTimeParts,
                 val pointPositions: List<PointPosition>,
                 val cusps: List<Double>)

data class AllCharts(val name: String,
                     val creation: String,
                     val charts: List<Chart>)

data class ActualAspect(val point1: Points,
                        val point2: Points,
                        val aspect: Aspects)

/**
 * Identification of a chart and a list of counted values.
 */
data class ChartCount(val id: String,
                      val name: String,
                      val counts: List<Int>)

/**
 * Counts of Sun, Moon and Ascendant in signs
 */
data class SMAInSign(val totals: List<Int>,
                     val countsPerChart: List<ChartCount> )
