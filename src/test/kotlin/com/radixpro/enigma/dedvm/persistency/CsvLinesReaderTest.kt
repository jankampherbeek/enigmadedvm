/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.persistency

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import java.io.File.separator as SEPARATOR

internal class CsvLinesReaderTest {

    @Test
    fun `Reading lines from a file gives the correct number of lines`() {
       val  fileName =  "testdata${SEPARATOR}data_short.csv"
       CsvLinesReader().readLinesFromCsv(fileName).size shouldBe 2
    }

    @Test
    fun `Lines read from a csv file should have the expected content`() {
        val  fileName =  "testdata${SEPARATOR}data_short.csv"
        val allLines = CsvLinesReader().readLinesFromCsv(fileName)
        allLines[0][0] shouldBe "395"
        allLines[1][1] shouldBe "Hermann Keyserling"
    }


    @Test
    fun `Temporary test to read 100 lines form original research`() {
        val  fileName =  "testdata${SEPARATOR}Dataanoniemeersteonderzoeksgroep.csv"
        CsvLinesReader().readLinesFromCsv(fileName).size shouldBe 100
    }


}