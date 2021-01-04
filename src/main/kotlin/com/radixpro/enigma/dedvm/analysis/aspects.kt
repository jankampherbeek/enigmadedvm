/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.analysis

import com.radixpro.enigma.dedvm.core.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class AspectsForChart {

    private val fullCircle = 360.0

    fun findAspects(chart: Chart): List<ActualAspect> {
        val actualAspects: MutableList<ActualAspect> = ArrayList()
        val mc = PointPosition(MundanePoints.MC, chart.cusps[10], 0.0)
        val asc = PointPosition(MundanePoints.ASC, chart.cusps[1], 0.0)
        val celPoints = chart.pointPositions
        for (i in 0..12) {
            for (j in (i + 1)..12) {
                actualAspects.addAll(checkForAspect(celPoints[i], celPoints[j]))
            }
            actualAspects.addAll(checkForAspect(celPoints[i], mc))
            actualAspects.addAll(checkForAspect(celPoints[i], asc))
        }
        return actualAspects.toList()
    }

    fun findAnyAspect(firstPoint: PointPosition, secondPoint: PointPosition): Boolean {
        val pos1 = min(firstPoint.lon, secondPoint.lon)
        val pos2 = max(firstPoint.lon, secondPoint.lon)
        val distance1 = pos2 - pos1
        val distance2 = pos1 - pos2 + fullCircle
        var found = false
        for (aspect in Aspects.values()) {
            val orb = defineOrb(firstPoint.point, secondPoint.point, aspect)
            val angle = aspect.degrees
            if ((abs(distance1 - angle) <= orb) || (abs(distance2 - angle) < orb)) found = true
        }
        return found
    }


    private fun checkForAspect(firstPoint: PointPosition, secondPoint: PointPosition): List<ActualAspect> {
        val foundAspects: MutableList<ActualAspect> = ArrayList()
        val pos1 = min(firstPoint.lon, secondPoint.lon)
        val pos2 = max(firstPoint.lon, secondPoint.lon)
        val distance1 = pos2 - pos1
        val distance2 = pos1 - pos2 + fullCircle
        for (aspect in Aspects.values()) {
            val angle = aspect.degrees
            val orb = defineOrb(firstPoint.point, secondPoint.point, aspect)
            val found = (abs(distance1 - angle) <= orb) || (abs(distance2 - angle) < orb)
            if (found) foundAspects.add(ActualAspect(firstPoint.point, secondPoint.point, aspect))
        }
        return foundAspects.toList()
    }

    private fun defineOrb(partner1: Points, partner2: Points, aspect: Aspects): Double {
        return if (partner1 == CelPoints.SUN || partner1 == CelPoints.MOON || partner2 == CelPoints.SUN || partner2 == CelPoints.MOON) aspect.orbForLights
        else aspect.orb
    }



}




