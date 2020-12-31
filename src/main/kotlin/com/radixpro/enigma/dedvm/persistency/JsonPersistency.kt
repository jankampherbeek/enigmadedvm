/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.persistency

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.radixpro.enigma.dedvm.core.AllCharts
import com.radixpro.enigma.dedvm.core.Chart
import com.radixpro.enigma.dedvm.exceptions.SaveException
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.time.LocalDateTime
import java.io.File.separator as SEPARATOR

/**
 * Reader for Json files.
 */
class JsonReader {
    fun readObjectFromFile(inputData: File): JSONObject {
        val parser = JSONParser()
        return try {
            val jsonObject = parser.parse(FileReader(inputData))
            jsonObject as JSONObject
        } catch (pe: ParseException) {
            throw RuntimeException("Could not parse results of : " + inputData + " . Original message " + pe.message)
        } catch (ioe: IOException) {
            throw RuntimeException("Could not read file : " + inputData + " . Original message " + ioe.message)
        }
    }
}

/**
 * Writes an object to file using Json format.
 * Based on an example at: http://www.studytrails.com/java/json/jackson-create-json.jsp
 */
class JsonWriter {
    fun write2File(pathFilename: String,
                   object2Write: Any,
                   useIndent: Boolean) {
        val mapper = ObjectMapper()
        mapper.configure(SerializationFeature.INDENT_OUTPUT, useIndent)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        try {
            val jsonFile = File(pathFilename)
            mapper.writeValue(jsonFile, object2Write)
        } catch (e: IOException) {
            throw SaveException("Could not write to file, using path and filename :$pathFilename . Reason: ${e.message}")
        }
    }
}

/**
 * Writes a collection of calculated charts to a JSON file.
 */
class ChartsWriter(private val jsonWriter: JsonWriter) {

    fun writeCharts(calculatedCharts: List<Chart>, name: String) {
        val allCharts = AllCharts(name, LocalDateTime.now().toString(), calculatedCharts)
        jsonWriter.write2File(name, allCharts, true)
    }
}


class AllChartsReader(private val jsonReader: JsonReader, private val mapper: ChartMapper) {

    fun readAllCharts(fileName: String): AllCharts {
        // todo define path
        val pathAndName = ".${SEPARATOR}data${SEPARATOR}${fileName}"
        val file = File(pathAndName)
        val json = jsonReader.readObjectFromFile(file)
        return mapper.jsonToAllCharts(json)
    }
}

class ResultsWriter(private val jsonWriter: JsonWriter) {

    fun writeResults(fileName: String, object2Write: Any) {
        val pathAndName = ".${SEPARATOR}data${SEPARATOR}${fileName}"
        val file = File(pathAndName)
        jsonWriter.write2File(pathAndName, object2Write, true)
    }
}





