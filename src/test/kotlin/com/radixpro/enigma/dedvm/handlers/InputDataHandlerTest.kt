/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File.separator as SEPARATOR

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