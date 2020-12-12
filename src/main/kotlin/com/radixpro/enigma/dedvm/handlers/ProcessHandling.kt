package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.core.*
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ResultsWriter

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

    private val fileNameForAscMcData = "BAMResultsAM.json"
    private val fileNameForAscMcControlData = "BAMControlDataResultsAM.json"
    private val fileNameForCornerData = "BAMResultsCorner.json"
    private val fileNameForCornerControlData = "BAMControlDataResultsCorner.json"
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