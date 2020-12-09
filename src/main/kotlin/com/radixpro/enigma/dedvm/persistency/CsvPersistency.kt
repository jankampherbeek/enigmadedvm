package com.radixpro.enigma.dedvm.persistency

import com.opencsv.CSVReaderHeaderAware
import com.radixpro.enigma.dedvm.core.ChartInputData
import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
import com.radixpro.enigma.dedvm.exceptions.InputDataException
import java.io.File
import java.io.FileReader
import java.lang.Exception
import kotlin.collections.ArrayList


/**
 * Reads content from a csv file, Structure of the content is not important.
 * Skips header line.
 */
class CsvLinesReader {

    fun readLinesFromCsv(fileName: String): List<Array<String>>  {
        val reader = CSVReaderHeaderAware(FileReader(fileName))
        return reader.readAll()
    }
}

/**
 * Writes lines to a csv file.
 */
class CsvWriter {
    fun write2File(pathFilename: String, lines: List<String>) {
        val file = File(pathFilename)
        file.writeText("")     // overwrites existing content
        for (line: String in lines) {
            file.appendText(line)
            file.appendText("\n")
        }
    }


}


/**
 * Processes input data as used for the calculation of a chart. Converts the original csv lines into a list of ChartInputData.
 */
class CsvInputDataReader(private val linesReader: CsvLinesReader) {

    fun readInputData(fileName: String): List<ChartInputData> {
        val allInputData: MutableList<ChartInputData> = ArrayList()
        val lines = linesReader.readLinesFromCsv(fileName)
        for (line in lines) {
            if (!line.contains(null) && 9 == line.size) allInputData.add(readSingleLine(line))
        }
        return allInputData
    }

    private fun readSingleLine(line: Array<String>): ChartInputData {
        try {
            val id: Int = line[0].trim { it <= ' ' }.toInt()
            val name: String = line[1].trim { it <= ' ' }
            val lonTxt: String = line[2].trim { it <= ' ' }
            val latTxt: String = line[3].trim { it <= ' ' }
            val dateTxt: String = line[4].trim { it <= ' ' }
            // index 5 (calendar) ignored, for this project always Gregorian assumed
            val timeTxt: String = line[6].trim { it <= ' ' }
            val offset: Double = line[7].trim { it <= ' ' }.toDouble()
            val dst: Double = line[8].trim { it <= ' ' }.toDouble()
            val dateTime = createDateTime(dateTxt, timeTxt, offset, dst)
            val location = createLocation(lonTxt, latTxt)
            return ChartInputData(id, name, dateTime, location)
        } catch (e: Exception) {
            throw InputDataException("Error when parsing line : " + line.contentToString())
        }
    }

    private fun createDateTime(dateTxt: String, timeTxt: String, offset: Double, dst: Double): DateTimeParts {
        val dateParts = dateTxt.split("/")
        val timeParts = timeTxt.split(":")
        val combinedOffsetUt = offset + dst
        return DateTimeParts(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt(),
            timeParts[0].toInt(), timeParts[1].toInt(), timeParts[2].toInt(), combinedOffsetUt)
    }

    private fun createLocation(lonText: String, latText: String) : Location {
        val lon: Double = if (lonText.contains("E")) {
            val lonParts = lonText.split("E")
            lonParts[0].toDouble() + (lonParts[1].toDouble() / 60.0)
        } else {
            val lonParts = lonText.split("W")
            -(lonParts[0].toDouble() + (lonParts[1].toDouble() / 60.0))
        }
        val lat: Double = if (latText.contains("N")) {
            val latParts = latText.split("N")
            latParts[0].toDouble() + (latParts[1].toDouble() / 60.0 )
        } else {
            val latParts = latText.split("S")
            -(latParts[0].toDouble() + (latParts[1].toDouble() / 60.0 ))
        }
        return Location(lat, lon)

    }
}