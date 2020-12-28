package com.radixpro.enigma.dedvm.di

import com.radixpro.enigma.dedvm.analysis.AspectsForChart
import com.radixpro.enigma.dedvm.analysis.HousePosition
import com.radixpro.enigma.dedvm.analysis.McDistance
import com.radixpro.enigma.dedvm.analysis.SignPosition
import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.astron.SeFrontend
import com.radixpro.enigma.dedvm.handlers.*
import com.radixpro.enigma.dedvm.persistency.*
import com.radixpro.enigma.dedvm.ui.Dashboard

object Injector {

    fun injectAllChartsReader(): AllChartsReader {
        return AllChartsReader(injectJsonReader(), injectChartMapper())
    }

    fun injectAspectsForChart(): AspectsForChart {
        return AspectsForChart()
    }

    fun injectBodiesAtCornersHandler(): BodiesAtCornersHandler {
        return BodiesAtCornersHandler(injectAllChartsReader(), injectResultsWriter())
    }

    fun injectBodiesInHouseHandler(): BodiesInHouseHandler {
        return BodiesInHouseHandler(injectAllChartsReader(), injectHousePosition(), injectResultsWriter())
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

    fun injectDashboard(): Dashboard {
        return Dashboard()
    }

    fun injectElevationHandler(): ElevationHandler {
        return ElevationHandler(injectAllChartsReader(), injectResultsWriter())
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

    fun injectMaxPointsHandler(): MaxPointsHandler {
        return MaxPointsHandler(injectAllChartsReader(), injectSignPosition(), injectHousePosition(), injectResultsWriter())
    }

    fun injectMcDistance(): McDistance {
        return McDistance()
    }

    fun injectPrincipleHandler(): PrincipleHandler {
        return PrincipleHandler(injectAllChartsReader(), injectSignPosition(), injectHousePosition(), injectAspectsForChart(), injectResultsWriter())
    }

    fun injectProminentAspectsHandler(): ProminentAspectsHandler {
        return ProminentAspectsHandler(injectAllChartsReader(), injectAspectsForChart(), injectSignPosition(), injectResultsWriter())
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

    fun injectUnaspectedPointsHandler(): UnaspectedPointsHandler {
        return UnaspectedPointsHandler(injectAllChartsReader(), injectAspectsForChart(), injectResultsWriter())
    }


}