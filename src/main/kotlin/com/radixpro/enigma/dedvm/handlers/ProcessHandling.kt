package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.analysis.AspectsForChart
import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.core.*
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ResultsWriter
import com.radixpro.enigma.dedvm.util.Range
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON)

    fun processChartsAscMc() {
        val cusps = listOf(1, 10)
        handleCharts(cusps, fileNameForAscMcData)
        handleControlData(cusps, fileNameForAscMcControlData)
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

class BodiesAtCornersHandler(private val allChartsReader: AllChartsReader, private val resultsWriter: ResultsWriter) {

    private val fileNameForCornerData = "BCOResults.json"
    private val fileNameForCornerControlData = "BCOControlDataResults.json"
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO, CelPoints.CHIRON)

    fun processCharts() {
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
            val asc = chart.cusps[0]
            val desc = chart.cusps[6]
            val mc = chart.cusps[9]
            val ic = chart.cusps[3]
            for (pointPos in chart.pointPositions) {
                val orb = if (pointPos.point == CelPoints.SUN || pointPos.point == CelPoints.MOON) 8.0 else 6.0
                if (pointPos.point != CelPoints.MEAN_APOGEE && pointPos.point != CelPoints.MEAN_NODE) {
                    if (withinOrb(asc, pointPos.lon, orb) || withinOrb(desc, pointPos.lon, orb) ||
                        withinOrb(mc, pointPos.lon, orb) || withinOrb(ic, pointPos.lon, orb )) details.add(1) else details.add(0)
                }
            }
            chartCounts.add(ChartCount(chart.id, chart.name, details.toList()))
        }
        return chartCounts.toList()
    }

    private fun withinOrb(lon1: Double, lon2: Double, orb: Double): Boolean {
        val diff = (max(lon1, lon2) - min(lon1, lon2))
        return Range.checkValue(diff, 0.0, 360.0) <= orb
    }

    private fun defineTotals(detailCount: List<ChartCount>): BodiesInRange {
        val totalsForBodies  = mutableListOf(0,0,0,0,0,0,0,0,0,0,0)     // 11 positions
        for (chartCount in detailCount) {
            for (i in 0..10) {
                totalsForBodies[i]+= chartCount.counts[i]
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

class UnaspectedPointsHandler(private val allChartsReader: AllChartsReader,
                              private val aspectsForChart: AspectsForChart,
                              private val resultsWriter: ResultsWriter) {

    private val fileNameForNASData = "NASResults.json"
    private val fileNameForNASControlData = "NASControlDataResults.json"
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
            for (point in supportedBodies) {
                var count = 0
                for (asp in allAspects) {
                    if (CelPoints.MEAN_NODE != asp.point1 && CelPoints.MEAN_NODE != asp.point2
                        && CelPoints.MEAN_APOGEE != asp.point1 && CelPoints.MEAN_APOGEE != asp.point2
                        && point == asp.point1 || point == asp.point2) count++
                }
                if (count == 0) nasValues[supportedBodies.indexOf(point)] = 1
            }
            chartCounts.add(ChartCount(chart.id, chart.name, nasValues))
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


class MaxPointsHandler(private val allChartsReader: AllChartsReader,
                       private val signPosition: SignPosition,
                       private val housePosition: HousePosition,
                       private val resultsWriter: ResultsWriter) {

    private val fileNameForMAXData = "MAXResults.json"
    private val fileNameForMAXControlData = "MAXControlDataResults.json"
    private val supportedBodies = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY, CelPoints.VENUS, CelPoints.MARS, CelPoints.JUPITER,
        CelPoints.SATURN, CelPoints.URANUS, CelPoints.NEPTUNE, CelPoints.PLUTO)
    private val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed

    fun processCharts() {
        handleCharts()
        handleControlData()
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
                totalsForMax[i]+= chartCount.counts[i]
            }
        }
        return MaxCounts(supportedBodies, totalsForMax.toList(), detailCount)
    }

    private fun isMax(point: CelPoints, chart: Chart): Boolean {
        var lon = 0.0
        for (pointPos in chart.pointPositions) if (pointPos.point == point) lon = pointPos.lon
        val sign = signPosition.idOfSign(lon)
        val house = housePosition.idOfHouse(lon, chart.jdUt, flags, chart.location)
        when (point) {
            CelPoints.SUN -> { if ( (1 == sign || 5 == sign) && !(7 == house || 11 == house || 12 == house)) return true}
            CelPoints.MOON -> { if ( (2 == sign || 4 == sign) && !(8 == house || 10 == house )) return true}
            CelPoints.MERCURY -> { if ( (3 == sign || 6 == sign) && !(4 == house || 8 == house || 9 == house || 12 == house)) return true}
            CelPoints.VENUS -> { if ( (2 == sign || 7 == sign || 12 == sign) && !(1 == house || 6 == house || 8 == house )) return true}
            CelPoints.MARS -> { if ( (1 == sign || 8 == sign || 10 == sign) && !(2 == house || 4 == house || 7 == house || 12 == house)) return true}
            CelPoints.JUPITER -> { if ( (4 == sign || 9 == sign || 12 == sign) && !(3 == house || 6 == house || 10 == house)) return true}
            CelPoints.SATURN -> { if ( (7 == sign || 10 == sign || 11 == sign) && !(1 == house || 4 == house || 5 == house || 12 == house)) return true}
            CelPoints.URANUS -> { if ( (8 == sign || 10 == sign || 11 == sign) && !(2 == house || 4 == house || 5 == house)) return true}
            CelPoints.NEPTUNE -> { if ( (9 == sign || 12 == sign) && !(3 == house || 6 == house || 10 == house || 11 == house)) return true}
            CelPoints.PLUTO -> { if ( (1 == sign || 8 == sign) && !(2 == house || 7 == house)) return true}
        }
        return false
    }

}


