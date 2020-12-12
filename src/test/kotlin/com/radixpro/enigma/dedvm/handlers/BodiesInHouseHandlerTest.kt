package com.radixpro.enigma.dedvm.handlers

import com.radixpro.enigma.dedvm.di.Injector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BodiesInHouseHandlerTest {

    private val handler = Injector.injectBodiesInHouseHandler()

    @Test
    fun `Checking for occurrences in houses 1 and 10 should run without errors`() {
        handler.processChartsAscMc()
    }

    @Test
    fun `Checking for occurrences in houses 1, 4, 7 and 10 should run without errors`() {
        handler.processCorners()
    }
}