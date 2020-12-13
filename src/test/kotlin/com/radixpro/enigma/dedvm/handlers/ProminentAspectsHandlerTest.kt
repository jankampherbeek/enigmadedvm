package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ProminentAspectsHandlerTest {

    private val handler = Injector.injectProminentAspectsHandler()

    @Test
    fun `Defining prominency based on aspects should notgieve any errors`() {
        handler.processCharts()
    }
}