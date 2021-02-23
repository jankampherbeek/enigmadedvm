/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.core.AllCharts
import com.radixpro.enigma.dedvm.persistency.AllChartsReader
import com.radixpro.enigma.dedvm.persistency.ChartsWriter
import com.radixpro.enigma.dedvm.persistency.ControlDataCreator
import com.radixpro.enigma.dedvm.persistency.CsvInputDataReader
import com.radixpro.enigma.dedvm.ui.Dashboard
import org.apache.log4j.Logger
import java.io.File
import java.io.File.separator as SEPARATOR

/**
 * Handler for input data.
 */
class InputDataHandler(
    private val csvInputDataReader: CsvInputDataReader,
    private val chartsCalculator: ChartsCalculator,
    private val controlDataCreator: ControlDataCreator,
    private val chartsWriter: ChartsWriter
) {

    private val log: Logger = Logger.getLogger(InputDataHandler::class.java)

    fun handleData(fileAndPath: String) {
        val fileNameForData = ".${SEPARATOR}data${SEPARATOR}calculatedcharts.json"
        val fileNameForCtrlData = ".${SEPARATOR}data${SEPARATOR}controlcharts.json"
        val inputDataRecords = csvInputDataReader.readInputData(fileAndPath)
        val calculatedCharts = chartsCalculator.processInputData(inputDataRecords)
        chartsWriter.writeCharts(calculatedCharts, fileNameForData)
        val controlDataRecords = controlDataCreator.createControlData(inputDataRecords)
        val calculatedControlCharts = chartsCalculator.processInputData(controlDataRecords)
        chartsWriter.writeCharts(calculatedControlCharts, fileNameForCtrlData)
    }


    fun handleDataForMultipleSubControlGroups(inputDataFile: File, multiplicity: Int) {
        val fileNameForData = ".${SEPARATOR}data${SEPARATOR}calculatedcharts.json"
        val fileNamePrefixForCtrlData = ".${SEPARATOR}data${SEPARATOR}subcontrolgroups${SEPARATOR}subcontrolcharts_"
        val inputDataRecords = csvInputDataReader.readInputData(inputDataFile)
        val calculatedCharts = chartsCalculator.processInputData(inputDataRecords)
        chartsWriter.writeCharts(calculatedCharts, fileNameForData)
        val controlDataSubRecords = controlDataCreator.createMultipleControlData(inputDataRecords, multiplicity)
        for((counter, subset) in controlDataSubRecords.withIndex()) {
            var calculatedSubControlCharts = chartsCalculator.processInputData(subset)
            log.info("Writing ctrl-data subset  $fileNamePrefixForCtrlData$counter.json" )
            chartsWriter.writeCharts(calculatedSubControlCharts, "$fileNamePrefixForCtrlData$counter.json")
        }
    }

}
