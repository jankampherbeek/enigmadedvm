package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.core.AllCharts
import com.radixpro.enigma.dedvm.core.ChartCount
import com.radixpro.enigma.dedvm.core.SMAInSign
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ResultsWriter

/**
 * Counts the numer of occurrences of Sun, Moon and Ascendant in signs.
 */
class SMAInSignHandler(private val allChartsReader: AllChartsReader,
                       private val signPosition: SignPosition,
                       private val resultsWriter: ResultsWriter ) {

    private val fileNameForCharts = "calculatedcharts.json"
    private val fileNameForControlData = "controlcharts.json"
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
            val ascPos = signPosition.idOfSign(chart.cusps[1])
            val counts: MutableList<Int> = ArrayList()
            counts[0] = sunPos
            counts[1] = moonPos
            counts[2] = ascPos
            val chartCount = ChartCount(chart.id, chart.name, counts.toList())
            chartCounts.add(chartCount)
        }
        return chartCounts.toList()
    }

    private fun defineTotals(detailCount: List<ChartCount>): SMAInSign {
        val totals = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)   // 12 values
        for (chartCount in detailCount) {
            totals[chartCount.counts[0]-1]++        // Sun
            totals[chartCount.counts[1]-1]++        // Moon
            totals[chartCount.counts[2]-1]++        // Ascendant
        }
        return SMAInSign(totals, detailCount)
    }


}