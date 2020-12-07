package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.core.PointPosition
import com.radixpro.enigma.dedvm.core.CelPoints
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class McDistanceTest {

    val mc = 100.0
    val sun = PointPosition(CelPoints.SUN, 70.0, 1.0)
    val moon = PointPosition(CelPoints.MOON, 180.0, 12.0)
    val mars = PointPosition(CelPoints.MARS, 129.0, 0.7)
    val points = listOf(sun, moon, mars)

    @Test
    fun `Point that is closest to MC is correctly defined`() {
        McDistance().closestToMc(points, mc) shouldBe CelPoints.MARS
    }

}


