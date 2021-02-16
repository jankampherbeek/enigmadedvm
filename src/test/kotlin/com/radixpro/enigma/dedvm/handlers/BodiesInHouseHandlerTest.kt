/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

internal class BodiesInHouseHandlerTest {

    private val handler = Injector.injectBodiesInHouseHandler()

    @Test
    fun `Checking for occurrences in houses 1 and 10 should run without errors`() {
        handler.processCharts(1)
    }

}