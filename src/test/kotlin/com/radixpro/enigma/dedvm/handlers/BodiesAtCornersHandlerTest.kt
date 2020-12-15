package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BodiesAtCornersHandlerTest {

    private val handler = Injector.injectBodiesAtCornersHandler()

    @Test
    fun `Calculating bodies at corners should not cause any errors`() {
        handler.processCharts()
    }
}