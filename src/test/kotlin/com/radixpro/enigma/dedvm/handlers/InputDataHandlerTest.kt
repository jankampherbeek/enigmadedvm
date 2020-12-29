/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import java.io.File.separator as SEPARATOR
import com.radixpro.enigma.dedvm.astron.ChartsCalculator
import com.radixpro.enigma.dedvm.core.Chart
import com.radixpro.enigma.dedvm.core.ChartInputData
import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
import com.radixpro.enigma.dedvm.di.Injector
import com.radixpro.enigma.dedvm.persistency.ControlDataCreator
import com.radixpro.enigma.dedvm.persistency.CsvInputDataReader
import com.radixpro.enigma.dedvm.persistency.JsonWriter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InputDataHandlerTest {

    private val handler = Injector.injectInputDataHandler()
    private val inputDataPathAndName = ".${SEPARATOR}testdata${SEPARATOR}data_long.csv"

    @BeforeEach
    fun setUp() {

    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `Handling of inputdata should not cause any error`() {
        handler.handleData(inputDataPathAndName)

    }



}