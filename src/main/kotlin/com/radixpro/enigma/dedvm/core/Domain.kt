/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.core

/**
 * Elements of date and time.
 */
data class DateTimeParts(val year: Int,
                         val month: Int,
                         val day: Int,
                         val hour: Int,
                         val minute: Int,
                         val second: Int,
                         val offsetUt: Double)

/**
 * Geographic latitude and longitude of a location.
 */
data class Location(val geoLat: Double,
                    val geoLon: Double)

/**
 * Input for the calculation of a chart.
 */
data class ChartInputData(val id: Int,
                          val name: String,
                          val dateTimeParts: DateTimeParts,
                          val location: Location)

/**
 * Longitude and speed for a specific celestial point.
 */
data class PointPosition(val point: Points,
                         val lon: Double,
                         val speed: Double)

/**
 * A calculated chart. Cusps start with index 1.
 */
data class Chart(val id: String,
                 val name: String,
                 val location: Location,
                 val jdUt: Double,
                 val armc: Double,
                 val epsilon: Double,
                 val dateTimeParts: DateTimeParts,
                 val pointPositions: List<PointPosition>,
                 val cusps: List<Double>)

/**
 * Collection of charts.
 */
data class AllCharts(val name: String,
                     val creation: String,
                     val charts: List<Chart>)

/**
 * An aspect and the points that form the aspect.
 */
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
 * Counts of bodies at specific positions.
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

/**
 * Counts of values for a specific body, according to principles as defined by Threes Brouwers.
 */
data class PrincipleBodyDetail(val body: Points,
                               val values: MutableList<Int>)

/**
 * Counts of values for a specific chart, according to principles as defined by Threes Brouwers.
 */
data class PrincipleChartDetails(val id: String,
                                 val name: String,
                                 val description: String,
                                 val totals: PrincipleBodyDetail,
                                 val details: List<PrincipleBodyDetail>)

/**
 * Full results for a specific principle.
 */
data class PrincipleComplete(val principleIndex: Int,
                             val totals: List<PrincipleBodyDetail>,
                             val details: List<PrincipleChartDetails>)

/**
 * House and point for a specific principle. Also an indication if MC and or Ascendant are relevant for this principle.
 */
data class PrinciplePlayers(val house: Int,
                            val point: CelPoints,
                            val checkMcOrAsc: Boolean)

