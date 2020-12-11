package com.radixpro.enigma.dedvm.di

import com.radixpro.enigma.dedvm.analysis.AspectsForChart
import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.McDistance
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.handlers.InputDataHandler
import com.radixpro.enigma.dedvm.handlers.SMAInSignHandler
import com.radixpro.enigma.dedvm.persistency.*

object Injector {

    fun injectAllChartsReader(): AllChartsReader {
        return AllChartsReader(injectJsonReader(), injectChartMapper())
    }

    fun injectAspectsForChart(): AspectsForChart {
        return AspectsForChart()
    }

    fun injectChartMapper(): ChartMapper {
        return ChartMapper()
    }

    fun injectChartsCalculator(): ChartsCalculator {
        return ChartsCalculator(injectSeFrontend())
    }

    fun injectChartsWriter(): ChartsWriter {
        return ChartsWriter(injectJsonWriter())
    }

    fun injectControldataCalendar(): ControlDataCalendar {
        return ControlDataCalendar()
    }

    fun injectControlDataCreator(): ControlDataCreator {
        return ControlDataCreator(injectListRandomizer(), injectControldataCalendar() )
    }

    fun injectCsvInputDataReader(): CsvInputDataReader {
        return CsvInputDataReader(injectCsvLinesReader())
    }

    fun injectCsvLinesReader(): CsvLinesReader {
        return CsvLinesReader()
    }

    fun injectHousePosition(): HousePosition {
        return HousePosition(injectSeFrontend())
    }

    fun injectInputDataHandler(): InputDataHandler {
        return InputDataHandler(injectCsvInputDataReader(), injectChartsCalculator(), injectControlDataCreator(), injectChartsWriter())
    }

    fun injectJsonReader():JsonReader {
        return JsonReader()
    }

    fun injectJsonWriter(): JsonWriter {
        return JsonWriter()
    }

    fun injectListRandomizer(): ListRandomizer {
        return ListRandomizer()
    }

    fun injectMcDistance(): McDistance {
        return McDistance()
    }

    fun injectResultsWriter(): ResultsWriter {
        return ResultsWriter(injectJsonWriter())
    }

    fun injectSeFrontend(): SeFrontend {
        return SeFrontend
    }

    fun injectSignPosition(): SignPosition {
        return SignPosition()
    }

    fun injectSMAInSignHandler(): SMAInSignHandler {
        return SMAInSignHandler(injectAllChartsReader(), injectSignPosition(), injectResultsWriter())
    }



}