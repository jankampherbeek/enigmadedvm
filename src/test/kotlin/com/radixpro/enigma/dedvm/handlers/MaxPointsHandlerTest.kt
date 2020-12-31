/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

internal class MaxPointsHandlerTest {

    private val handler = Injector.injectMaxPointsHandler()

    @Test
    fun `Defining max points should not cause anyn error`() {
        handler.processCharts()
    }
}