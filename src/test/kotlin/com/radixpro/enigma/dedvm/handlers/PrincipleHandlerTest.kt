/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

internal class PrincipleHandlerTest {

    private val handler = Injector.injectPrincipleHandler()

    @Test
    fun `Processing all principles should not lead to any error`() {
        handler.processCharts(1)
    }
}