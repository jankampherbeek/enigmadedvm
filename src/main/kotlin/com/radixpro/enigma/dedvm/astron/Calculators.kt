/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.astron

import com.radixpro.enigma.dedvm.core.CelPoints
import com.radixpro.enigma.dedvm.core.Chart
import com.radixpro.enigma.dedvm.core.ChartInputData
import com.radixpro.enigma.dedvm.core.PointPosition

class ChartsCalculator(private val seFrontend: SeFrontend) {

    fun processInputData(inputDataRecords: List<ChartInputData>): List<Chart> {
        val calcResults: MutableList<Chart> = ArrayList()
        val flags = 0 or 2 or 256           // 2 = SwissEph, 256 = speed
        for (inputData in inputDataRecords) {
            val location = inputData.location
            val jdUt = seFrontend.defineJdUt(inputData.dateTimeParts)
            val armc = seFrontend.defineArmc(jdUt, flags, location)
            val epsilon = seFrontend.defineEpsilon(jdUt, flags)
            val celObjectPositions: MutableList<PointPosition> = ArrayList()
            for (celObject in CelPoints.values()) {
                val coPositions = seFrontend.defineLongitudeForBody(jdUt, celObject.id, flags)
                celObjectPositions.add(PointPosition(celObject,coPositions[0], coPositions[1]))
            }
            val cusps = seFrontend.defineLongitudeForPlacidus(jdUt, flags, location).toList()
            calcResults.add(Chart(inputData.id.toString(), inputData.name, location,
                                  jdUt, armc, epsilon, inputData.dateTimeParts, celObjectPositions, cusps))
        }
        return calcResults.toList()
    }

}