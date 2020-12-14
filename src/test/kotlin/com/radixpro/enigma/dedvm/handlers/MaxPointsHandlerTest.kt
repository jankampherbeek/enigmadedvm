package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MaxPointsHandlerTest {

    private val handler = Injector.injectMaxPointsHandler()

    @Test
    fun `Defining max points should not cause anyn error`() {
        handler.processCharts()
    }
}