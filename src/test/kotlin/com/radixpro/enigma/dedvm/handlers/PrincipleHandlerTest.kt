package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PrincipleHandlerTest {

    private val handler = Injector.injectPrincipleHandler()

    @Test
    fun `Processing all principles should not lead to any error`() {
        handler.processCharts()
    }
}