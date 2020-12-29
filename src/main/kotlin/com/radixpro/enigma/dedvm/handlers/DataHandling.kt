/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.persistency.ChartsWriter
import com.radixpro.enigma.dedvm.persistency.ControlDataCreator
import com.radixpro.enigma.dedvm.persistency.CsvInputDataReader
import com.radixpro.enigma.dedvm.persistency.JsonWriter
import java.io.File.separator as SEPARATOR

class InputDataHandler(
    private val csvInputDataReader: CsvInputDataReader,
    private val chartsCalculator: ChartsCalculator,
    private val controlDataCreator: ControlDataCreator,
    private val chartsWriter: ChartsWriter
) {

    fun handleData(fileAndPath: String) {
        val inputDataRecords = csvInputDataReader.readInputData(fileAndPath)
        val calculatedCharts = chartsCalculator.processInputData(inputDataRecords)
        val fileNameData = defineNameForData(fileAndPath)
        val fullPathNameData = ".${SEPARATOR}testdata${SEPARATOR}${fileNameData}"
        chartsWriter.writeCharts(calculatedCharts, fileNameData)
        val controlDataRecords = controlDataCreator.createControlData(inputDataRecords)
        val calculatedControlCharts = chartsCalculator.processInputData(controlDataRecords)
        val fileNameControlData = defineNameForControlData(fileAndPath)
        val fullPathControlData = ".${SEPARATOR}testdata${SEPARATOR}${fileNameControlData}"
        chartsWriter.writeCharts(calculatedControlCharts, fileNameControlData)
    }

    private fun defineNameForData(dataFileName: String): String {
        val position1 = dataFileName.lastIndexOf(SEPARATOR)
        val position2 = dataFileName.lastIndexOf(".csv")
        val name = dataFileName.substring(position1+1, position2)
        return "${name}_calculatedCharts.json"
    }

    private fun defineNameForControlData(dataFileName: String): String {
        val position1 = dataFileName.lastIndexOf(SEPARATOR)
        val position2 = dataFileName.lastIndexOf(".csv")
        val name = dataFileName.substring(position1+1, position2)
        return "${name}_controlCharts.json"
    }

}