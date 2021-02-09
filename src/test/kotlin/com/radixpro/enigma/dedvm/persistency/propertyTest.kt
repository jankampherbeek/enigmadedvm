/*
 *
 *  * Jan Kampherbeek, (c) 2020, 2021.
 *  * EnigmaDedVM is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.persistency

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


internal class PropertyTest {

    private val propReader = PropertyReader()
    private val propWriter = PropertyWriter()
    private val key = "mykey"
    private val value = "myvalue"

    @Test
    fun writeReadProperty() {
        propWriter.writeProperty(key, value)
        propReader.readProperty(key) shouldBe value
    }
}