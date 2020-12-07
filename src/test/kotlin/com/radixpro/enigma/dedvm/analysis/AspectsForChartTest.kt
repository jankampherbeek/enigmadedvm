package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.core.*
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AspectsForChartTest {

    private val chartMock: Chart = mockk()
    private lateinit var aspectsFound: List<ActualAspect>
    private lateinit var expectedAspects: List<ActualAspect>

    @BeforeEach
    fun setUp() {
        every { chartMock.pointPositions } returns createPointPositions()
        every { chartMock.cusps } returns createCusps()
        aspectsFound = AspectsForChart().findAspects(chartMock)
        expectedAspects = createExpectedAspects()
    }

    @Test
    fun `Expected aspects should all be found`() {
        for (aspectToFind in expectedAspects) {
            aspectsFound.indexOf(aspectToFind) shouldBeGreaterThan -1
        }
    }

    @Test
    fun `Number of actual aspects should be equal to number of expected aspects`() {
        aspectsFound.size shouldBe expectedAspects.size
    }

    private fun createExpectedAspects(): List<ActualAspect> {
        val expectedAspects = mutableListOf<ActualAspect>()
        expectedAspects.add(ActualAspect(CelPoints.SUN, CelPoints.MERCURY, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.SUN, CelPoints.MARS, Aspects.TRIGON))
        expectedAspects.add(ActualAspect(CelPoints.SUN, CelPoints.NEPTUNE, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.SUN, CelPoints.MEAN_NODE, Aspects.OPPOSITION))
        expectedAspects.add(ActualAspect(CelPoints.MOON, CelPoints.VENUS, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.MOON, CelPoints.MARS, Aspects.TRIGON))
        expectedAspects.add(ActualAspect(CelPoints.MOON, CelPoints.MEAN_NODE, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.MOON, MundanePoints.MC, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.MERCURY, CelPoints.CHIRON, Aspects.TRIGON))
        expectedAspects.add(ActualAspect(CelPoints.VENUS, CelPoints.NEPTUNE, Aspects.TRIGON))
        expectedAspects.add(ActualAspect(CelPoints.VENUS, CelPoints.PLUTO, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.VENUS, MundanePoints.ASC, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.VENUS, MundanePoints.MC, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.MARS, CelPoints.PLUTO, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.MARS, CelPoints.MEAN_NODE, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.MARS, MundanePoints.ASC, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.JUPITER, CelPoints.SATURN, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.JUPITER, CelPoints.PLUTO, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.JUPITER, MundanePoints.MC, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.SATURN, CelPoints.PLUTO, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.SATURN, MundanePoints.MC, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.SATURN, CelPoints.MEAN_APOGEE, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.URANUS, CelPoints.MEAN_APOGEE, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.NEPTUNE, CelPoints.PLUTO, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.NEPTUNE, CelPoints.MEAN_NODE, Aspects.SQUARE))
        expectedAspects.add(ActualAspect(CelPoints.NEPTUNE, MundanePoints.ASC, Aspects.SEXTILE))
        expectedAspects.add(ActualAspect(CelPoints.NEPTUNE, MundanePoints.MC, Aspects.TRIGON))
        expectedAspects.add(ActualAspect(CelPoints.PLUTO, MundanePoints.ASC, Aspects.CONJUNCTION))
        expectedAspects.add(ActualAspect(CelPoints.PLUTO, MundanePoints.MC, Aspects.SEXTILE))
        return expectedAspects.toList()
    }

    private fun createPointPositions(): List<PointPosition> {
        val pointPositions = mutableListOf<PointPosition>()
        pointPositions.add(PointPosition(CelPoints.SUN, 254.736667, 1.015))              // 14.44.12 Sag  + 1.00.54
        pointPositions.add(PointPosition(CelPoints.MOON, 144.515556, 13.318056))        // 24.30.56 Leo  +13.19.05
        pointPositions.add(PointPosition(CelPoints.MERCURY, 247.160833, 1.562222))      //  7.09.39 Sag  + 1.33.44
        pointPositions.add(PointPosition(CelPoints.VENUS, 228.439444, 1.245278))        // 18.26.22 Sco  + 1.14.43
        pointPositions.add(PointPosition(CelPoints.MARS, 18.305556, 0.306667))          // 18.18.20 Ari  + 0.15.24
        pointPositions.add(PointPosition(CelPoints.JUPITER, 297.227222, 0.203056))      // 27.13.38 Cap  + 0.12.11
        pointPositions.add(PointPosition(CelPoints.SATURN, 298.911389, 0.095833))       // 28.54.41 Cap  + 0.05.45
        pointPositions.add(PointPosition(CelPoints.URANUS, 37.345, -0.016667))          //  7.20.42 Tau  - 0.01.48
        pointPositions.add(PointPosition(CelPoints.NEPTUNE, 348.178333, 0.004167))      // 18.10.42 Pis  + 0.00.15
        pointPositions.add(PointPosition(CelPoints.PLUTO, 293.411389, 0.027222))        // 23.24.41 Cap  + 0.01.38
        pointPositions.add(PointPosition(CelPoints.MEAN_NODE, 80.2125, -0.052778))      // 20.12.45 Gem  - 0.03.10
        pointPositions.add(PointPosition(CelPoints.MEAN_APOGEE, 35.133889, -3.648056))  //  5.08.02 Tau  - 3.38.53
        pointPositions.add(PointPosition(CelPoints.CHIRON, 4.982778, -0.008611))        //  4.58.58 Ari  - 0.00.31
        return pointPositions.toList()
    }

    private fun createCusps(): List<Double> {
        val allCusps = mutableListOf<Double>()
        allCusps.add(0.0)           // dummy value
        allCusps.add(288.610833)    // Asc 18.36.39 CP
        allCusps.add(345.478056)    // c2  15.28.41 PI
        allCusps.add(28.296389)     // c3  28.17.47 AR
        allCusps.add(53.921944)     // IC  23.55.19 TA
        allCusps.add(72.465556)     // c5  12.27.56 GE
        allCusps.add(89.156944)     // c6  29.09.25 GE
        allCusps.add(108.610833)    // Dsc 18.36.36 CN
        allCusps.add(165.478056)    // c8  15.28.41 VI
        allCusps.add(208.296389)    // c9  28.17.47 LI
        allCusps.add(233.921944)    // MC  23.55.19 SC
        allCusps.add(252.465556)    // c11 12.27.56 SA
        allCusps.add(269.156944)    // c12 29.09.25 SA
        return allCusps.toList()
    }


}