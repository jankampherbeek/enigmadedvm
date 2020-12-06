package com.radixpro.enigma.dedvm.analysis

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SignPositionTest {

    @Test
    fun `The sign position of a give longitude is correct`() {
        SignPosition().idOfSign(123.456) shouldBe 5
    }
}