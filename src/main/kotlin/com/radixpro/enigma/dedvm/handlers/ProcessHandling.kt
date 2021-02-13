/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.analysis.AspectsForChart
import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.core.*
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ResultsWriter
import com.radixpro.enigma.dedvm.util.Range
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val fileNameForCharts = "calculatedcharts.json"
private const val fileNameForControlData = "controlcharts.json"


/**
 * Shared function, calculates the effect of a prespan when close to a cusp.
 */
fun checkForCuspOrb(lon: Double, speed: Double, house: Int, cusp: Double): Int {
    var newHouse = house
    val signForLon = SignPosition().idOfSign(lon)
    val signForCusp = SignPosition().idOfSign(cusp)
    val diff =  Range.checkValue(abs(cusp - lon), 0.0, 360.0)
    val corner = house == 12 || house == 3 || house == 6 || house == 9
    val close = (diff < 3.0) || (diff < 4.0 && corner)
    if (speed > 0 && signForLon == signForCusp && close) newHouse++
    if (newHouse > 12) newHouse = 1
    return newHouse
}


/**
 * Counts the numer of occurrences of Sun, Moon and Ascendant in signs.
 */
class SMAInSignHandler(
    private val allChartsReader: AllChartsReader,
    private val signPosition: SignPosition,
    private val resultsWriter: ResultsWriter
) {


    private val fileNameForSMAData = "SMAResults.json"
    private val fileNameForSMAControlData = "SMAControlDataResults.json"

    fun processCharts(nrOfCtrlGroups: Int) {
        handleCharts()
        if (nrOfCtrlGroups == 1) handleControlData() else {
            handleMultiControlData(nrOfCtrlGroups)
        }
    }

    private fun handleCharts() {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val detailCount = defineDetailsCount(allCharts)
        val smaInSign = defineTotals(detailCount)
        resultsWriter.writeResults(fileNameForSMAData, smaInSign)
    }

    private fun handleControlData() {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val detailCount = defineDetailsCount(allCharts)
        val smaInSign = defineTotals(detailCount)
        resultsWriter.writeResults(fileNameForSMAControlData, smaInSign)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int) {
        val combinedResults : MutableList<SMAInSign> = ArrayList()
        val fileNameTotals = "totalSMACtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subSMACtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val detailCount = defineDetailsCount(allCharts)
            val smaInSign = defineTotals(detailCount)
            combinedResults.add(smaInSign)
            resultsWriter.writeResults(fileNameResults, smaInSign)
        }
        var totSun = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 12
        var totMoon = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)    // 12
        var totAsc = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 12
        for (i in 0 until nrOfCtrlGroups) {
            for (j in 0..11) {
                totSun[j]+= combinedResults[i].totalsSun[j]
                totMoon[j]+= combinedResults[i].totalsMoon[j]
                totAsc[j]+= combinedResults[i].totalsAsc[j]
            }
        }
        var avgSun = DoubleArray(12)
        var avgMoon = DoubleArray(12)
        var avgAsc = DoubleArray(12)
        for (i in 0..11) {
            avgSun[i] = totSun[i].toDouble()/nrOfCtrlGroups
            avgMoon[i] = totMoon[i].toDouble()/nrOfCtrlGroups
            avgAsc[i] = totAsc[i].toDouble()/nrOfCtrlGroups
        }
        val smaTotals = SMAInSignAverages(avgSun.toList(), avgMoon.toList(), avgAsc.toList())
        resultsWriter.writeResults(fileNameTotals, smaTotals)
    }

    private fun defineDetailsCount(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val sunPos = signPosition.idOfSign(chart.pointPositions[0].lon)
            val moonPos = signPosition.idOfSign(chart.pointPositions[1].lon)
            val ascPos = signPosition.idOfSign(chart.cusps[1])
            val counts: MutableList<Int> = ArrayList()
            counts.add(sunPos)
            counts.add(moonPos)
            counts.add(ascPos)
            val chartCount = ChartCount(chart.id, chart.name, counts.toList())
            chartCounts.add(chartCount)
        }
        return chartCounts.toList()
    }

    private fun defineTotals(detailCount: List<ChartCount>): SMAInSign {
        val totalsSun = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val totalsMoon = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val totalsAsc = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        for (chartCount in detailCount) {
            totalsSun[chartCount.counts[0] - 1]++
            totalsMoon[chartCount.counts[1] - 1]++
            totalsAsc[chartCount.counts[2] - 1]++
        }
        return SMAInSign(totalsSun, totalsMoon, totalsAsc, detailCount)
    }

}

/**
 * Counts the occurrence of Sun .. Pluto and Cheiron in specific houses.
 */
class BodiesInHouseHandler(
    private val allChartsReader: AllChartsReader,
    private val housePosition: HousePosition,
    private val resultsWriter: ResultsWriter
) {

    private val fileNameForAscMcData = "BAMResults.json"
    private val fileNameForAscMcControlData = "BAMControlDataResults.json"
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON
    )

    fun processChartsAscMc(nrOfCtrlGroups: Int) {
        val cusps = listOf(1, 10)
        handleCharts(cusps, fileNameForAscMcData)
        if (nrOfCtrlGroups == 1) handleControlData(cusps, fileNameForAscMcControlData)
        else handleMultiControlData(nrOfCtrlGroups, cusps)
    }


    private fun handleCharts(cusps: List<Int>, fileName: String) {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val detailCount = defineDetailsCount(allCharts, cusps)
        val bodiesInRange = defineTotals(detailCount)
        resultsWriter.writeResults(fileName, bodiesInRange)
    }


    private fun handleControlData(cusps: List<Int>, fileName: String) {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val detailCount = defineDetailsCount(allCharts, cusps)
        val bodiesInRange = defineTotals(detailCount)
        resultsWriter.writeResults(fileName, bodiesInRange)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int, cusps: List<Int>) {
        val combinedResults : MutableList<BodiesInRange> = ArrayList()
        val fileNameTotals = "totalBAMCtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subBAMCtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val detailCount = defineDetailsCount(allCharts, cusps)
            val pointInMcAsc = defineTotals(detailCount)
            combinedResults.add(pointInMcAsc)
            resultsWriter.writeResults(fileNameResults, pointInMcAsc)
        }
        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }
        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val birAverages = BodiesInRangeAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, birAverages)
    }


    private fun defineDetailsCount(allCharts: AllCharts, cusps: List<Int>): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val details: MutableList<Int> = ArrayList()
              for (point in supportedBodies) {
                  val pointPos = findPointInChart(point, chart)
                  var house =  housePosition.idOfHouse(pointPos.lon, chart.jdUt, flags, chart.location)
                  var nextCusp = house + 1     // array cusps starts with 0
                  if (nextCusp > 12) nextCusp = 1
                    house = checkForCuspOrb(pointPos.lon, pointPos.speed, house, chart.cusps[nextCusp])
                    if (cusps.contains(house)) details.add(1) else details.add(0)
              }
            chartCounts.add(ChartCount(chart.id, chart.name, details.toList()))
        }
        return chartCounts.toList()
    }

    private fun findPointInChart(point: Points, chart: Chart):PointPosition {
        for (pointPos in chart.pointPositions) {
            if (point == pointPos.point) return pointPos
        }
        throw RuntimeException("Could not find point in chart for BAM")
    }

    private fun defineTotals(detailCount: List<ChartCount>): BodiesInRange {
        val totalsForSigns = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForSigns[i] += chartCount.counts[i]
            }
        }
        return BodiesInRange(supportedBodies, totalsForSigns.toList(), detailCount)
    }

}

/**
 * Checks if points are positioned in corners.
 */
class BodiesAtCornersHandler(private val allChartsReader: AllChartsReader, private val resultsWriter: ResultsWriter) {

    private val fileNameForCornerData = "BCOResults.json"
    private val fileNameForCornerControlData = "BCOControlDataResults.json"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON
    )

    fun processCharts(nrOfCtrlGroups: Int) {
        val cusps = listOf(1, 4, 7, 10)
        handleCharts(cusps, fileNameForCornerData)
        if (nrOfCtrlGroups == 1) handleControlData(cusps, fileNameForCornerControlData)
        else (handleMultiControlData(nrOfCtrlGroups, cusps))

    }

    private fun handleCharts(cusps: List<Int>, fileName: String) {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val detailCount = defineDetailsCount(allCharts, cusps)
        val bodiesInRange = defineTotals(detailCount)
        resultsWriter.writeResults(fileName, bodiesInRange)
    }


    private fun handleControlData(cusps: List<Int>, fileName: String) {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val detailCount = defineDetailsCount(allCharts, cusps)
        val bodiesInRange = defineTotals(detailCount)
        resultsWriter.writeResults(fileName, bodiesInRange)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int, cusps: List<Int>) {
        val combinedResults : MutableList<BodiesInRange> = ArrayList()
        val fileNameTotals = "totalBCOCtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subBCOCtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val detailCount = defineDetailsCount(allCharts, cusps)
            val pointAtCorner = defineTotals(detailCount)
            combinedResults.add(pointAtCorner)
            resultsWriter.writeResults(fileNameResults, pointAtCorner)
        }
        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }
        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val birAverages = BodiesInRangeAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, birAverages)
    }

    private fun defineDetailsCount(allCharts: AllCharts, cusps: List<Int>): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val details: MutableList<Int> = ArrayList()
            val asc = chart.cusps[1]
            val desc = chart.cusps[7]
            val mc = chart.cusps[10]
            val ic = chart.cusps[4]
            for (pointPos in chart.pointPositions) {
                val orb = if (pointPos.point == CelPoints.SUN || pointPos.point == CelPoints.MOON) 8.0 else 6.0
                if (pointPos.point != CelPoints.MEAN_APOGEE && pointPos.point != CelPoints.MEAN_NODE) {
                    if (withinOrb(asc, pointPos.lon, orb) || withinOrb(desc, pointPos.lon, orb) ||
                        withinOrb(mc, pointPos.lon, orb) || withinOrb(ic, pointPos.lon, orb)
                    ) details.add(1) else details.add(0)
                }
            }
            chartCounts.add(ChartCount(chart.id, chart.name, details.toList()))
        }
        return chartCounts.toList()
    }

    private fun withinOrb(lon1: Double, lon2: Double, orb: Double): Boolean {
        val diff = (max(lon1, lon2) - min(lon1, lon2))
        val signLon1 = SignPosition().idOfSign(lon1)
        val signLon2 = SignPosition().idOfSign(lon2)
        return (Range.checkValue(diff, 0.0, 360.0) <= orb) && signLon1 == signLon2
    }

    private fun defineTotals(detailCount: List<ChartCount>): BodiesInRange {
        val totalsForBodies = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11 positions
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForBodies[i] += chartCount.counts[i]
            }
        }
        return BodiesInRange(supportedBodies, totalsForBodies.toList(), detailCount)
    }

}


/**
 * Checks for the celestial body that is closest to the MC, mesured in longitude.
 */
class ElevationHandler(private val allChartsReader: AllChartsReader, private val resultsWriter: ResultsWriter) {

    private val fileNameForELEVData = "ELEVResults.json"
    private val fileNameForELEVControlData = "ELEVControlDataResults.json"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON
    )

    fun processCharts(nrOfCtrlGroups: Int) {
        handleCharts()
        if (nrOfCtrlGroups == 1) handleControlData() else handleMultiControlData(nrOfCtrlGroups)
    }

    private fun handleCharts() {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val details = defineDetails(allCharts)
        val smaInSign = defineTotals(details)
        resultsWriter.writeResults(fileNameForELEVData, smaInSign)
    }

    private fun handleControlData() {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val details = defineDetails(allCharts)
        val elevated = defineTotals(details)
        resultsWriter.writeResults(fileNameForELEVControlData, elevated)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int) {
        val combinedResults : MutableList<ElevationValues> = ArrayList()
        val fileNameTotals = "totalELEVCtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subELEVCtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val details = defineDetails(allCharts)
            val elevated = defineTotals(details)
            combinedResults.add(elevated)
            resultsWriter.writeResults(fileNameResults, elevated)
        }

        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }
        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val elevAverages = ElevationAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, elevAverages)
    }


    private fun defineDetails(allCharts: AllCharts): List<MinMaxPositionsPerChart> {
        val elevatedPoints: MutableList<MinMaxPositionsPerChart> = ArrayList()
        for (chart in allCharts.charts) {
            var pointWithShortestDistance: Points = EmptyPoints.EXISTS_NOT
            val mc = chart.cusps[10]
            var shortestDistance = 180.0
            var distance = 0.0
            for (pointPos in chart.pointPositions) {
                if (pointPos.point != CelPoints.MEAN_NODE && pointPos.point != CelPoints.MEAN_APOGEE) {
                    distance = abs(Range.checkValue(abs(mc - pointPos.lon), -180.0, 180.0))
                    if (distance <= shortestDistance) {
                        shortestDistance = distance
                        pointWithShortestDistance = pointPos.point as CelPoints
                    }
                }
            }
            if (shortestDistance <= 60.0) elevatedPoints.add(MinMaxPositionsPerChart(chart.id, chart.name, pointWithShortestDistance as CelPoints, shortestDistance))
        }
        return elevatedPoints.toList()
    }

    private fun defineTotals(details: List<MinMaxPositionsPerChart>): ElevationValues {
        val totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)       // 11 positions
        for (mmPos in details) {
            when (mmPos.point) {
                CelPoints.SUN -> totals[0]++
                CelPoints.MOON -> totals[1]++
                CelPoints.MERCURY -> totals[2]++
                CelPoints.VENUS -> totals[3]++
                CelPoints.MARS -> totals[4]++
                CelPoints.JUPITER -> totals[5]++
                CelPoints.SATURN -> totals[6]++
                CelPoints.URANUS -> totals[7]++
                CelPoints.NEPTUNE -> totals[8]++
                CelPoints.PLUTO -> totals[9]++
                CelPoints.CHIRON -> totals[10]++
            }
        }
        return ElevationValues(supportedBodies, totals, details)
    }

}

/**
 * Checks if a point is prominently aspected.
 */
class ProminentAspectsHandler(
    private val allChartsReader: AllChartsReader,
    private val aspectsForChart: AspectsForChart,
    private val signPosition: SignPosition,
    private val resultsWriter: ResultsWriter
) {

    private val fileNameForPRAData = "PRAResults.json"
    private val fileNameForPRAControlData = "PRAControlDataResults.json"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON
    )

    fun processCharts(nrOfCtrlGroups: Int) {
        handleCharts()
        if (nrOfCtrlGroups == 1) handleControlData() else handleMultiControlData(nrOfCtrlGroups)
    }


    private fun handleCharts() {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val details = defineDetails(allCharts)
        val praCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForPRAData, praCounts)
    }

    private fun handleControlData() {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val details = defineDetails(allCharts)
        val praCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForPRAControlData, praCounts)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int) {
        val combinedResults : MutableList<AspectCounts> = ArrayList()
        val fileNameTotals = "totalPRACtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subPRACtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val details = defineDetails(allCharts)
            val aspCounts = defineTotals(details)
            combinedResults.add(aspCounts)
            resultsWriter.writeResults(fileNameResults, aspCounts)
        }

        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }
        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val aspCountAverages = AspectCountAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, aspCountAverages)
    }


    private fun defineDetails(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val praAspects = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 11 positions
            val signAscIndex = signPosition.idOfSign(chart.cusps[1])
            val signAsc = Signs.values()[signAscIndex - 1]
            val rulerAsc = signAsc.strong
            val allAspects = aspectsForChart.findAspects(chart)

            for (asp in allAspects) {
                if (asp.point1 != CelPoints.MEAN_APOGEE && asp.point1 != CelPoints.MEAN_NODE &&
                    asp.point2 != CelPoints.MEAN_APOGEE && asp.point2 != CelPoints.MEAN_NODE
                ) {
                    if ((asp.point1 == CelPoints.SUN ||
                                asp.point1 == CelPoints.MOON ||
                                asp.point1 == MundanePoints.ASC ||
                                asp.point1 == MundanePoints.MC
                                ) && (supportedBodies.contains(asp.point2))
                    ) praAspects[supportedBodies.indexOf(asp.point2)]++
                    if ((asp.point2 == CelPoints.SUN ||
                                asp.point2 == CelPoints.MOON ||
                                asp.point2 == MundanePoints.ASC ||
                                asp.point2 == MundanePoints.MC
                                ) && (supportedBodies.contains(asp.point1))
                    ) praAspects[supportedBodies.indexOf(asp.point1)]++
                    // additional run for ruler asc as this can be Sun or Moon and should be counted twice if that occurs
                    if (asp.point1 == rulerAsc  && (supportedBodies.contains(asp.point2))) praAspects[supportedBodies.indexOf(asp.point2)]++
                    if (asp.point2 == rulerAsc  && (supportedBodies.contains(asp.point1))) praAspects[supportedBodies.indexOf(asp.point1)]++
                }
            }

            var maxCount = 0
            val totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 11 positions
            for (i in 0..10) if (praAspects[i] > maxCount) maxCount = praAspects[i]
            for (i in 0..10) if (praAspects[i] == maxCount) totals[i] = 1

            chartCounts.add(ChartCount(chart.id, chart.name, totals))

        }
        return chartCounts.toList()
    }


    private fun defineTotals(detailCount: List<ChartCount>): AspectCounts {
        val totalsForPra = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForPra[i] += chartCount.counts[i]
            }
        }
        return AspectCounts(supportedBodies, totalsForPra.toList(), detailCount)
    }
}

/**
 * Checks is point is unaspected or part of an unaspected duet.
 */
class UnaspectedPointsHandler(
    private val allChartsReader: AllChartsReader,
    private val aspectsForChart: AspectsForChart,
    private val resultsWriter: ResultsWriter
) {

    private val fileNameForNASData = "NASResults.json"
    private val fileNameForNASControlData = "NASControlDataResults.json"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON
    )

    fun processCharts(nrOfCtrlGroups: Int) {
        handleCharts()
        if (nrOfCtrlGroups == 1) handleControlData() else handleMultiControlData(nrOfCtrlGroups)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int) {
        val combinedResults : MutableList<AspectCounts> = ArrayList()
        val fileNameTotals = "totalNASCtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subNASCtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val details = defineDetails(allCharts)
            val aspCounts = defineTotals(details)
            combinedResults.add(aspCounts)
            resultsWriter.writeResults(fileNameResults, aspCounts)
        }

        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }

        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val nasAverages = AspectCountAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, nasAverages)
    }


    private fun handleCharts() {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val details = defineDetails(allCharts)
        val praCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForNASData, praCounts)
    }

    private fun handleControlData() {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val details = defineDetails(allCharts)
        val praCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForNASControlData, praCounts)
    }

    private fun defineDetails(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val nasValues = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 11 positions
            val allAspects = aspectsForChart.findAspects(chart)
            val aspPartnersPerCelPoint = arrayListOf<List<Points>>()
            for (point1 in supportedBodies) {
                        aspPartnersPerCelPoint.add(checkAspects(point1, allAspects))
            }
            for (point1 in supportedBodies) {
                val nrOfAspectsPoint1 = aspPartnersPerCelPoint[supportedBodies.indexOf(point1)].size
                var noAspects = nrOfAspectsPoint1 == 0
                if (nrOfAspectsPoint1 == 1) {       // check for duet
                    val point2 = aspPartnersPerCelPoint[supportedBodies.indexOf(point1)][0]
                    val nrOfAspectsPoint2 = aspPartnersPerCelPoint[supportedBodies.indexOf(point2)].size
                    noAspects = nrOfAspectsPoint2 == 1
                }
                if (noAspects) nasValues[supportedBodies.indexOf(point1)] = 1
            }
            chartCounts.add(ChartCount(chart.id, chart.name, nasValues))
        }
        return chartCounts.toList()
    }

    private fun checkAspects(point1: Points, allAspects: List<ActualAspect>): List<Points> {
        val aspPartners: MutableList<Points> = ArrayList()
        for (point2 in supportedBodies) {
            if (point1 != point2) {
                for (asp in allAspects) {
                    if ((point1 == asp.point1) && (point2 == asp.point2))  {
                        aspPartners.add(point2)
                    }
                    if ((point2 == asp.point1) && (point1 == asp.point2)) {
                        aspPartners.add(point2)
                    }
                }
            }
        }
        return aspPartners
    }

    private fun defineTotals(detailCount: List<ChartCount>): AspectCounts {
        val totalsForPra = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForPra[i] += chartCount.counts[i]
            }
        }
        return AspectCounts(supportedBodies, totalsForPra.toList(), detailCount)
    }
}

/**
 * Checks the point with maximum value, based on rulership, exaltation and position in houses.
 */
class MaxPointsHandler(
    private val allChartsReader: AllChartsReader,
    private val signPosition: SignPosition,
    private val housePosition: HousePosition,
    private val resultsWriter: ResultsWriter
) {

    private val fileNameForMAXData = "MAXResults.json"
    private val fileNameForMAXControlData = "MAXControlDataResults.json"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO
    )
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed

    fun processCharts(nrOfCtrlGroups: Int) {
        handleCharts()
        if (nrOfCtrlGroups == 1) handleControlData() else handleMultiControlData(nrOfCtrlGroups)
    }

    private fun handleCharts() {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val details = defineDetails(allCharts)
        val maxCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForMAXData, maxCounts)
    }

    private fun handleControlData() {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val details = defineDetails(allCharts)
        val maxCounts = defineTotals(details)
        resultsWriter.writeResults(fileNameForMAXControlData, maxCounts)
    }

    private fun handleMultiControlData(nrOfCtrlGroups: Int) {
        val combinedResults : MutableList<MaxCounts> = ArrayList()
        val fileNameTotals = "totalMAXCtrlResults.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subMAXCtrlResults_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val details = defineDetails(allCharts)
            val maxCounts = defineTotals(details)
            combinedResults.add(maxCounts)
            resultsWriter.writeResults(fileNameResults, maxCounts)
        }

        var totals = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 11
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                totals[j] += combinedResults[i].totals[j]
            }
        }

        var averages = DoubleArray(supportedBodies.size)
        for (i in supportedBodies.indices) {
            averages[i] = totals[i].toDouble() / nrOfCtrlGroups
        }
        val maxAverages = MaxCountAverages(supportedBodies, averages.toList())
        resultsWriter.writeResults(fileNameTotals, maxAverages)
    }

    private fun defineDetails(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val maxValues = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 10 positions
            for (point in supportedBodies) {
                if (isMax(point, chart)) maxValues[supportedBodies.indexOf(point)] = 1
            }
            chartCounts.add(ChartCount(chart.id, chart.name, maxValues))
        }
        return chartCounts.toList()
    }

    private fun defineTotals(detailCount: List<ChartCount>): MaxCounts {
        val totalsForMax = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)     // 10 positions
        for (chartCount in detailCount) {
            for (i in 0..9) {
                totalsForMax[i] += chartCount.counts[i]
            }
        }
        return MaxCounts(supportedBodies, totalsForMax.toList(), detailCount)
    }

    private fun isMax(point: CelPoints, chart: Chart): Boolean {
        var lon = 0.0
        var speed = 0.0
        for (pointPos in chart.pointPositions) if (pointPos.point == point) {
            lon = pointPos.lon
            speed = pointPos.speed
        }
        val sign = signPosition.idOfSign(lon)
        var house = housePosition.idOfHouse(lon, chart.jdUt, flags, chart.location)
        var nextHouse = house+1
        if (nextHouse == 13) nextHouse = 1
        house = checkForCuspOrb(lon, speed, house, chart.cusps[nextHouse])
        when (point) {
            CelPoints.SUN -> {
                if ((1 == sign || 5 == sign) && !(7 == house || 11 == house || 12 == house)) return true
            }
            CelPoints.MOON -> {
                if ((2 == sign || 4 == sign) && !(8 == house || 10 == house)) return true
            }
            CelPoints.MERCURY -> {
                if ((3 == sign || 6 == sign) && !(4 == house || 8 == house || 9 == house || 12 == house)) return true
            }
            CelPoints.VENUS -> {
                if ((2 == sign || 7 == sign || 12 == sign) && !(1 == house || 6 == house || 8 == house)) return true
            }
            CelPoints.MARS -> {
                if ((1 == sign || 8 == sign || 10 == sign) && !(2 == house || 4 == house || 7 == house || 12 == house)) return true
            }
            CelPoints.JUPITER -> {
                if ((4 == sign || 9 == sign || 12 == sign) && !(3 == house || 6 == house || 10 == house)) return true
            }
            CelPoints.SATURN -> {
                if ((7 == sign || 10 == sign || 11 == sign) && !(1 == house || 4 == house || 5 == house || 12 == house)) return true
            }
            CelPoints.URANUS -> {
                if ((8 == sign || 10 == sign || 11 == sign) && !(2 == house || 4 == house || 5 == house)) return true
            }
            CelPoints.NEPTUNE -> {
                if ((9 == sign || 12 == sign) && !(3 == house || 6 == house || 10 == house || 11 == house)) return true
            }
            CelPoints.PLUTO -> {
                if ((1 == sign || 8 == sign) && !(2 == house || 7 == house)) return true
            }
        }
        return false
    }
}

/**
 * Handles the 'principles'.
 */
class PrincipleHandler(
    private val allChartsReader: AllChartsReader,
    private val signPosition: SignPosition,
    private val housePosition: HousePosition,
    private val aspectsForChart: AspectsForChart,
    private val resultsWriter: ResultsWriter
) {
    private val fileNamePrefixForPRIData = "PRIResults_"
    private val fileNamePrefixForPRIControlData = "PRIControlDataResults_"
    private val supportedBodies = listOf(
        CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON, CelPoints.MEAN_NODE, CelPoints.MEAN_APOGEE
    )
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed

    fun processCharts(nrOfCtrlGroups: Int) {
        for (i in 1..12) {
            handleCharts(i)
            if (nrOfCtrlGroups == 1) handleControlData(i) else handleMultiControlData(nrOfCtrlGroups, i)
        }
    }

    private fun handleCharts(index: Int) {
        val allCharts = allChartsReader.readAllCharts(fileNameForCharts)
        val details = defineDetails(index, allCharts)
        val principleComplete = defineTotals(index, details)
        resultsWriter.writeResults("$fileNamePrefixForPRIData$index.json", principleComplete)
    }

    private fun handleControlData(index: Int) {
        val allCharts = allChartsReader.readAllCharts(fileNameForControlData)
        val details = defineDetails(index, allCharts)
        val principleComplete = defineTotals(index, details)
        resultsWriter.writeResults("$fileNamePrefixForPRIControlData$index.json", principleComplete)
    }


    private fun handleMultiControlData(nrOfCtrlGroups: Int, index: Int) {
        val combinedResults : MutableList<PrincipleComplete> = ArrayList()
        val fileNameTotals = "totalPRICtrlResults$index.json"
        for (i in 0 until nrOfCtrlGroups) {
            val fileNameCtrlGroup = "subcontrolgroups" + File.separator  + "subcontrolcharts_" + i + ".json"
            val fileNameResults = "subcontrolgroups" + File.separator  +"subPRICtrl${index}Results_" + i + ".json"
            val allCharts = allChartsReader.readAllCharts(fileNameCtrlGroup)
            val details = defineDetails(index, allCharts)
            val priTotals = defineTotals(index, details)
            combinedResults.add(priTotals)
            resultsWriter.writeResults(fileNameResults, priTotals)
        }

        var totals = Array(nrOfCtrlGroups) { IntArray(5) }
        for (i in 0 until nrOfCtrlGroups) {
            for (j in supportedBodies.indices) {
                for (k in 0 until 5) {
                    totals[j][k]+= combinedResults[i].totals[j].values[k]
                }
            }
        }

        var averages = Array(supportedBodies.size) {DoubleArray(5)}
        for (i in supportedBodies.indices) {
            for (j in 0 until 5) {
                averages[i][j] = totals[i][j].toDouble() / nrOfCtrlGroups
            }

        }
        val priAverages = PrincipleAverages(supportedBodies, averages)
        resultsWriter.writeResults(fileNameTotals, priAverages)
    }

    private fun defineDetails(index: Int, allCharts: AllCharts): List<PrincipleChartDetails > {

        val descr = "Planet in own sign - asp/planet - in house - asp/lord - asp asc/mc (if appl.) - total"
        val players = definePlayers(index)
        val chartDetails: MutableList<PrincipleChartDetails> = ArrayList()
        for (chart in allCharts.charts) {
            val aspects = aspectsForChart.findAspects(chart)
            val asc = PointPosition(MundanePoints.ASC, chart.cusps[1], 0.0)
            val mc = PointPosition(MundanePoints.MC, chart.cusps[10], 0.0)
            val ruler = defineRuler(index, chart)
            val lordCusp = defineRuler(index, chart)
            lateinit var lordCuspPointPos : PointPosition

            for (pointPos in chart.pointPositions) {
                if (lordCusp == pointPos.point) {
                    lordCuspPointPos = pointPos
                }
            }
            val lonPlayer = definePointPos(players.point, chart)
            val bodyDetails: MutableList<PrincipleBodyDetail> = ArrayList()
            val totalValues = mutableListOf(0, 0, 0, 0, 0, 0)
            for (lonPointToCheck in chart.pointPositions) {
                val priValues = mutableListOf(0, 0, 0, 0, 0, 0)
                if (checkInSignOfPrinciple(lonPointToCheck, index)) priValues[0] = 1
                if ((players.point != lonPointToCheck.point) && (checkAspect(lonPlayer, lonPointToCheck, aspects))) priValues[1] = 1
                if (checkHouse(chart, lonPointToCheck) == index) priValues[2] = 1
                if (ruler != lonPointToCheck.point && checkAspect(lordCuspPointPos, lonPointToCheck, aspects)) priValues[3] = 1
                if (players.checkMcOrAsc && index == 1 && checkAspect(asc, lonPointToCheck, aspects)) priValues[4] = 1
                if (players.checkMcOrAsc && index == 10 && checkAspect(mc, lonPointToCheck, aspects)) priValues[4] = 1
                for (i in 0..4) priValues[5] += priValues[i]
                for (i in 0..5) totalValues[i] += priValues[i]
                bodyDetails.add(PrincipleBodyDetail(lonPointToCheck.point, priValues))
            }
            val totals = PrincipleBodyDetail(EmptyPoints.TOTAL, totalValues)
            chartDetails.add(PrincipleChartDetails(chart.id, chart.name, descr, totals, bodyDetails))
        }
        return chartDetails.toList()
    }

    private fun defineTotals(index: Int, chartDetails: List<PrincipleChartDetails>): PrincipleComplete {
        val totalsPerBody: MutableList<PrincipleBodyDetail> = ArrayList()
        for (point in supportedBodies) {
            totalsPerBody.add(PrincipleBodyDetail(point, mutableListOf(0, 0, 0, 0, 0, 0)))
        }
        for (detailsRow in chartDetails) {                                      // check all PrincipleChartDetails
            val allDetails = detailsRow.details                                 // list of PrincipleBodyDetail
            for (bodyDetails in allDetails) {                                   // specific PrincipleBodyDetail
                val bodyIndex = supportedBodies.indexOf(bodyDetails.body)       // index of body
                for (i in 0..5) {
                    totalsPerBody[bodyIndex].values[i] += bodyDetails.values[i]
                }
            }
        }
        return PrincipleComplete(index, totalsPerBody.toList(), chartDetails)
    }

    private fun checkInSignOfPrinciple(pointToCheck: PointPosition, principleId: Int): Boolean {
        val signIndex = signPosition.idOfSign(pointToCheck.lon)
        return signIndex == principleId
    }

    private fun checkAspect(pos1: PointPosition, pos2: PointPosition, aspects: List<ActualAspect>): Boolean {
        for (aspect in aspects) {
            if ((aspect.point1 == pos1.point && aspect.point2 == pos2.point) || (aspect.point1 == pos2.point && aspect.point2 == pos1.point)) return true
        }
        return false
    }

    private fun checkHouse(chart: Chart, pointPos: PointPosition): Int {
        val house =  housePosition.idOfHouse(pointPos.lon, chart.jdUt, flags, chart.location)
        return if (pointPos.point == CelPoints.MEAN_NODE || pointPos.point == CelPoints.MEAN_APOGEE) house
        else {
            var nextHouse = house + 1
            if (nextHouse > 12) nextHouse = 1
            checkForCuspOrb(pointPos.lon, pointPos.speed, house, chart.cusps[nextHouse])
        }
    }

    private fun defineRuler(priIndex: Int, chart: Chart): CelPoints {
        val lonCusp = chart.cusps[priIndex]
        return when (val signOnCusp = signPosition.idOfSign(lonCusp)) {
            1 -> CelPoints.MARS
            2, 7 -> CelPoints.VENUS
            3, 6 -> CelPoints.MERCURY
            4 -> CelPoints.MOON
            5 -> CelPoints.SUN
            8 -> CelPoints.PLUTO
            9 -> CelPoints.JUPITER
            10 -> CelPoints.SATURN
            11 -> CelPoints.URANUS
            12 -> CelPoints.NEPTUNE
            else -> throw IllegalArgumentException("Received $signOnCusp as index for a zodiac sign, while only 1..12 are supported.")
        }
    }


    private fun definePointPos(player: Points, chart: Chart): PointPosition {
        for (pointPos in chart.pointPositions) {
            if (player == pointPos.point) return pointPos
        }
        throw IllegalArgumentException("Received ${player.nameTxt} as player but could not find it in the accompanying chart.")
    }


    private fun definePlayers(priIndex: Int): PrinciplePlayers {
        when (priIndex) {
            1 -> return PrinciplePlayers(1, CelPoints.MARS, true)
            2 -> return PrinciplePlayers(2, CelPoints.VENUS, false)
            3 -> return PrinciplePlayers(3, CelPoints.MERCURY, false)
            4 -> return PrinciplePlayers(4, CelPoints.MOON, false)
            5 -> return PrinciplePlayers(5, CelPoints.SUN, false)
            6 -> return PrinciplePlayers(6, CelPoints.MERCURY, false)
            7 -> return PrinciplePlayers(7, CelPoints.VENUS, false)
            8 -> return PrinciplePlayers(8, CelPoints.PLUTO, false)
            9 -> return PrinciplePlayers(9, CelPoints.JUPITER, false)
            10 -> return PrinciplePlayers(10, CelPoints.SATURN, true)
            11 -> return PrinciplePlayers(11, CelPoints.URANUS, false)
            12 -> return PrinciplePlayers(12, CelPoints.NEPTUNE, false)
        }
        throw IllegalArgumentException("Received $priIndex as index for a Principle, while only 1..12 are supported.")
    }


}




