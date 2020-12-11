package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.persistency.ControlDataCreator
import com.radixpro.enigma.dedvm.persistency.CsvInputDataReader
import com.radixpro.enigma.dedvm.persistency.JsonWriter
import java.io.File.separator as SEPARATOR

class InputDataHandler(
    private val csvInputDataReader: CsvInputDataReader,
    private val chartsCalculator: ChartsCalculator,
    private val controlDataCreator: ControlDataCreator,
    private val jsonWriter: JsonWriter
) {

    fun handleData(fileAndPath: String) {
        val inputDataRecords = csvInputDataReader.readInputData(fileAndPath)
        val allCharts = chartsCalculator.processInputData(inputDataRecords)
        val fileNameData = defineNameForData(fileAndPath)
        val fullPathNameData = ".${SEPARATOR}testdata${SEPARATOR}${fileNameData}"
        jsonWriter.write2File(fullPathNameData, allCharts, true)
        val controlDataRecords = controlDataCreator.createControlData(inputDataRecords)
        val controlCharts = chartsCalculator.processInputData(controlDataRecords)
        val fileNameControlData = defineNameForControlData(fileAndPath)
        val fullPathControlData = ".${SEPARATOR}testdata${SEPARATOR}${fileNameControlData}"
        jsonWriter.write2File(fullPathControlData, controlCharts, true)
    }


    private fun defineNameForData(dataFileName: String): String {
        val position1 = dataFileName.lastIndexOf(SEPARATOR)
        val position2 = dataFileName.lastIndexOf(".csv")
        val name = dataFileName.substring(position1, position2)
        return "${name}_calculatedCharts.json"
    }

    private fun defineNameForControlData(dataFileName: String): String {
        val position1 = dataFileName.lastIndexOf(SEPARATOR)
        val position2 = dataFileName.lastIndexOf(".csv")
        val name = dataFileName.substring(position1, position2)
        return "${name}_controlCharts.json"
    }

}