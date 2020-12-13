package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

internal class ElevationHandlerTest {

    private val handler = Injector.injectElevationHandler()

    @Test
    fun `Finding elevations should cause no errors`() {
        handler.processCharts()
    }
}