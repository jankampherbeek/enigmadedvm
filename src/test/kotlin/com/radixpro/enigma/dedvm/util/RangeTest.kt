package com.radixpro.enigma.dedvm.util

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RangeTest {

    private val delta = 0.00000001

    @Test
    fun `Range returns value that is already correct without changing it`() {
        Range.checkValue(213.0) shouldBe (213.0 plusOrMinus delta)
    }

    @Test
    fun `A value that is too small is changed to a correct value`() {
        Range.checkValue(-10.0) shouldBe (350.0 plusOrMinus delta)
    }

    @Test
    fun `A value that is too large is changed to a correct value`() {
        Range.checkValue(410.0) shouldBe (50.0 plusOrMinus delta)
    }

    @Test
    fun `Using non default border values still gives a correct result`() {
        Range.checkValue(133.3, 0.0, 100.0) shouldBe (33.3 plusOrMinus delta)
    }

    @Test
    fun `Using a wrong sequence in border values still gives a correct result`() {
        Range.checkValue(1033.3, 1000.0, 0.0) shouldBe (33.3 plusOrMinus delta)
    }
}