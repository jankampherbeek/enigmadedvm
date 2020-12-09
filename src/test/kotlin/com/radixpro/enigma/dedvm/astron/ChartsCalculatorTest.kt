package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.CelPoints
import com.radixpro.enigma.dedvm.core.ChartInputData
import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach

internal class ChartsCalculatorTest {

    private lateinit var calculator: ChartsCalculator
    private val seFrontend = SeFrontend

    @BeforeEach
    fun setUp() {
        calculator = ChartsCalculator(seFrontend)
    }

    @Test
    fun `Processing inputdata should result in correct set of Charts`() {
        val charts = calculator.processInputData(defineInputData())
        charts[0].id shouldBe "395"
        charts[0].jdUt shouldBe (2407865.24037042 plusOrMinus 0.00000001)
        charts[0].pointPositions[0].point shouldBe CelPoints.SUN
        charts[0].pointPositions[0].lon shouldBe (68.663091639566 plusOrMinus 0.00000001)
        charts.size shouldBe 2
    }

    private fun defineInputData(): List<ChartInputData> {
        val ciData: MutableList<ChartInputData> = ArrayList()
        val dateTimeParts1 = DateTimeParts(1880,5,29, 18, 30, 0, 0.73111)
        val dateTimeParts2 = DateTimeParts(1880, 7, 20, 21, 55, 0, 1.65)
        val location1 = Location(51.8, 10.9666667)
        val location2 = Location(58.8833333, 24.8333333)
        ciData.add(ChartInputData(395, "Oswald Spengler", dateTimeParts1, location1))
        ciData.add(ChartInputData(396, "Hermann KEyserling", dateTimeParts2, location2))
        return ciData.toList()
    }
}