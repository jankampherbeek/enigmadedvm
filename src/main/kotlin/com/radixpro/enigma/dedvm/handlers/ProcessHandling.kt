package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.analysis.AspectsForChart
import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.core.*
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ResultsWriter
import com.radixpro.enigma.dedvm.util.Range
import kotlin.math.abs

private const val fileNameForCharts = "calculatedcharts.json"
private const val fileNameForControlData = "controlcharts.json"

/**
 * Counts the numer of occurrences of Sun, Moon and Ascendant in signs.
 */
class SMAInSignHandler(private val allChartsReader: AllChartsReader,
                       private val signPosition: SignPosition,
                       private val resultsWriter: ResultsWriter ) {


    private val fileNameForSMAData = "SMAResults.json"
    private val fileNameForSMAControlData = "SMAControlDataResults.json"

    fun processCharts() {
        handleCharts()
        handleControlData()
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

    private fun defineDetailsCount(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val sunPos = signPosition.idOfSign(chart.pointPositions[0].lon)
            val moonPos = signPosition.idOfSign(chart.pointPositions[1].lon)
            val ascPos = signPosition.idOfSign(chart.cusps[0])
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
        val totalsSun = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        val totalsMoon = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        val totalsAsc = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        for (chartCount in detailCount) {
            totalsSun[chartCount.counts[0]-1]++
            totalsMoon[chartCount.counts[1]-1]++
            totalsAsc[chartCount.counts[2]-1]++
        }
        return SMAInSign(totalsSun, totalsMoon, totalsAsc, detailCount)
    }

}

/**
 * Counts the occurrence of Sun .. Pluto and Cheiron in specific houses.
 */
class BodiesInHouseHandler(private val allChartsReader: AllChartsReader,
                           private val housePosition: HousePosition,
                           private val resultsWriter: ResultsWriter
                           ) {

    private val fileNameForAscMcData = "BAMResults.json"
    private val fileNameForAscMcControlData = "BAMControlDataResults.json"
    private val fileNameForCornerData = "BCOResults.json"
    private val fileNameForCornerControlData = "BCOControlDataResults.json"
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON)

    fun processChartsAscMc() {
        val cusps = listOf(1, 10)
        handleCharts(cusps, fileNameForAscMcData)
        handleControlData(cusps, fileNameForAscMcControlData)
    }

    fun processCorners() {
        val cusps = listOf(1, 4, 7, 10)
        handleCharts(cusps, fileNameForCornerData)
        handleControlData(cusps, fileNameForCornerControlData)
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

    private fun defineDetailsCount(allCharts: AllCharts, cusps: List<Int>): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val details: MutableList<Int> = ArrayList()
            for (pointPos in chart.pointPositions) {
                if (pointPos.point != CelPoints.MEAN_APOGEE && pointPos.point != CelPoints.MEAN_NODE) {
                    val house = housePosition.idOfHouse(pointPos.lon, chart.jdUt, flags, chart.location)
                    if (cusps.contains(house)) details.add(1) else details.add(0)
                }
            }
            chartCounts.add(ChartCount(chart.id, chart.name, details.toList()))
        }
        return chartCounts.toList()
    }

    private fun defineTotals(detailCount: List<ChartCount>): BodiesInRange {
        val totalsForSigns  = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForSigns[i]+= chartCount.counts[i]
            }
        }
        return BodiesInRange(supportedBodies, totalsForSigns.toList(), detailCount)
    }

}


/**
 * Checks for the celestial body that is closest to the MC, mesured in longitude.
 */
class ElevationHandler(private val allChartsReader: AllChartsReader, private val resultsWriter: ResultsWriter) {

    private val fileNameForELEVData = "ELEVResults.json"
    private val fileNameForELEVControlData = "ELEVControlDataResults.json"
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON)

    fun processCharts() {
        handleCharts()
        handleControlData()
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
        val smaInSign = defineTotals(details)
        resultsWriter.writeResults(fileNameForELEVControlData, smaInSign)
    }

    private fun defineDetails(allCharts: AllCharts): List<MinMaxPositionsPerChart> {
        val elevatedPoints: MutableList<MinMaxPositionsPerChart> = ArrayList()
        for (chart in allCharts.charts) {
            var pointWithShortestDistance: Points = EmptyPoints.EXISTS_NOT
            val mc = chart.cusps[9]
            var shortestDistance = 180.0
            var distance = 0.0
            for (pointPos in chart.pointPositions) {
                if (pointPos.point != CelPoints.MEAN_NODE && pointPos.point != CelPoints.MEAN_APOGEE) {      // todo check which bodies to use
                    distance = Range.checkValue(abs(mc - pointPos.lon), 0.0, 180.0)
                    if (distance <= shortestDistance) {
                        shortestDistance = distance
                        pointWithShortestDistance = pointPos.point as CelPoints
                    }
                }
            }
            elevatedPoints.add(MinMaxPositionsPerChart(chart.id, chart.name, pointWithShortestDistance as CelPoints, shortestDistance))
        }
        return elevatedPoints.toList()
    }

    private fun defineTotals(details: List<MinMaxPositionsPerChart>): ElevationValues {
        val totals = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)       // 11 positions
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

class ProminentAspectsHandler(private val allChartsReader: AllChartsReader,
                              private val aspectsForChart: AspectsForChart,
                              private val signPosition: SignPosition,
                              private val resultsWriter: ResultsWriter) {

    private val fileNameForPRAData = "PRAResults.json"
    private val fileNameForPRAControlData = "PRAControlDataResults.json"
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON)

    fun processCharts() {
        handleCharts()
        handleControlData()
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

    private fun defineDetails(allCharts: AllCharts): List<ChartCount> {
        val chartCounts: MutableList<ChartCount> = ArrayList()
        for (chart in allCharts.charts) {
            val praAspects = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 11 positions
            val signAscIndex = signPosition.idOfSign(chart.cusps[0])
            val signAsc = Signs.values()[signAscIndex-1]
            val rulerAsc = signAsc.strong
            val allAspects = aspectsForChart.findAspects(chart)

            for (asp in allAspects) {
                if (asp.point1 != CelPoints.MEAN_APOGEE && asp.point1 != CelPoints.MEAN_NODE &&
                    asp.point2 != CelPoints.MEAN_APOGEE && asp.point2 != CelPoints.MEAN_NODE) {

                    if ((asp.point1 == CelPoints.SUN ||
                        asp.point1 == CelPoints.MOON ||
                        asp.point1 == rulerAsc ||
                        asp.point1 == MundanePoints.ASC ||
                        asp.point1 == MundanePoints.MC
                         ) && (supportedBodies.contains(asp.point2))) praAspects[supportedBodies.indexOf(asp.point2)]++
                    if ((asp.point2 == CelPoints.SUN ||
                        asp.point2 == CelPoints.MOON ||
                        asp.point2 == rulerAsc ||
                        asp.point2 == MundanePoints.ASC ||
                        asp.point2 == MundanePoints.MC
                    ) && (supportedBodies.contains(asp.point1))) praAspects[supportedBodies.indexOf(asp.point1)]++
                }
            }

            var maxCount = 0
            val totals =  mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)   // 11 positions
            for (i in 0..10) if (praAspects[i] > maxCount) maxCount++
            for (i in 0..10) if (praAspects[i] == maxCount) totals[i]++

            chartCounts.add(ChartCount(chart.id, chart.name, totals))
        }
        return chartCounts.toList()
    }


    private fun defineTotals(detailCount: List<ChartCount>): AspectCounts {
        val totalsForPra = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForPra[i]+= chartCount.counts[i]
            }
        }
        return AspectCounts(supportedBodies, totalsForPra.toList(), detailCount)
    }

}