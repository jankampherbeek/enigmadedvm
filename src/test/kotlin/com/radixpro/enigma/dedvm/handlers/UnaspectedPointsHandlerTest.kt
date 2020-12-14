package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UnaspectedPointsHandlerTest {

    private val handler = Injector.injectUnaspectedPointsHandler()

    @Test
    fun `Defining unaspected points should function without errors`() {
        handler.processCharts()
    }
}