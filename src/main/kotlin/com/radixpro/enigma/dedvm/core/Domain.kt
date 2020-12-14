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
 * Counts of Sun, Moon and Ascendant in signs plus totals
 */
data class SMAInSign(val totalsSun: List<Int>,
                     val totalsMoon: List<Int>,
                     val totalsAsc: List<Int>,
                     val countsPerChart: List<ChartCount> )

/**
 * Counts of bodies in signs or houses.
 */
data class BodiesInRange(val bodySpec: List<CelPoints>,
                         val totals: List<Int>,
                         val details: List<ChartCount>)

/**
 * Specification of a point with a specific maximum of minimum in a chart.
 */
data class MinMaxPositionsPerChart(val id: String,
                                   val name: String,
                                   val point: CelPoints,
                                   val distance: Double)

/**
 * Counts of bodies that are closest to the MC, measured in longitude.
 */
data class ElevationValues(val bodySpec: List<CelPoints>,
                           val totals: List<Int>,
                           val details: List<MinMaxPositionsPerChart>)

/**
 * Counts of specific aspects per celestial body.
 */
data class AspectCounts(val bodySpec: List<CelPoints>,
                        val totals: List<Int>,
                        val details: List<ChartCount>)

/**
 * Counts of max points per celestial body.
 */
data class MaxCounts(val bodySpec: List<CelPoints>,
                     val totals: List<Int>,
                     val details: List<ChartCount>)