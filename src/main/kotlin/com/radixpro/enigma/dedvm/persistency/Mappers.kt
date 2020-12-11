package com.radixpro.enigma.dedvm.persistency

import com.radixpro.enigma.dedvm.core.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Mapper for objcts with Charts.
 */
class ChartMapper() {

    /**
     * Maps Json for a set of charts (AllCharts) to an object AllCharts.
     */
    fun jsonToAllCharts(jsonObject: JSONObject): AllCharts {
        return performMapping(jsonObject)
    }

    private fun performMapping(json: JSONObject):AllCharts {
        val name = json["name"] as String
        val creation =json["creation"] as String
        val chartsToConstruct = json["charts"] as JSONArray
        val charts = handleCharts(chartsToConstruct)
        return AllCharts(name, creation, charts)
    }

    private fun handleCharts(jsonArray: JSONArray): List<Chart>{
        val chartsToConstruct: MutableList<Chart> = ArrayList()
        for (entry in jsonArray) {
            val json = entry as JSONObject
            val id = json["id"] as String
            val name = json["name"] as String
            val location = createLocation(json)
            val jdUt = json["jdUt"] as Double
            val armc = json["armc"] as Double
            val epsilon = json["epsilon"] as Double
            val dateTimeParts = createDateTimeParts(json)
            val pointsToConstruct = json["pointPositions"] as JSONArray
            val pointPositions = createPointPositions(pointsToConstruct)
            val cuspsToConstruct = json["cusps"] as JSONArray
            val cusps = createCusps(cuspsToConstruct)
            chartsToConstruct.add(Chart(id, name, location, jdUt, armc, epsilon, dateTimeParts, pointPositions, cusps))
        }
        return chartsToConstruct.toList()
    }

    private fun createLocation(json: JSONObject): Location {
        val locationObject = json["location"] as JSONObject
        val latValue = locationObject["geoLat"] as Double
        val lonValue = locationObject["geoLon"] as Double
        return Location(latValue, lonValue)
    }

    private fun createDateTimeParts(json: JSONObject): DateTimeParts {
        val partsObject = json["dateTimeParts"] as JSONObject
        val year = partsObject["year"].toString().toInt()
        val month = partsObject["month"].toString().toInt()
        val day = partsObject["day"].toString().toInt()
        val hour = partsObject["hour"].toString().toInt()
        val minute = partsObject["minute"].toString().toInt()
        val second = partsObject["second"].toString().toInt()
        val offsetUt = partsObject["offsetUt"] as Double
        return DateTimeParts(year, month, day, hour, minute, second, offsetUt)
    }

    private fun createPointPositions(positions: JSONArray):List<PointPosition> {
        val positionsToConstruct: MutableList<PointPosition> = ArrayList()
        for (entry in positions) {
            val json = entry as JSONObject
            val pointName = json["point"] as String
            val point = CelPoints.valueOf(pointName)
            val lon = json["lon"] as Double
            val speed = json["speed"] as Double
            positionsToConstruct.add(PointPosition(point, lon, speed))
        }
        return positionsToConstruct.toList()
    }

    private fun createCusps(positions: JSONArray): List<Double> {
        val positionsToConstruct: MutableList<Double> = ArrayList()
        for (i in 1 .. 12) {
            positionsToConstruct.add(positions[i] as Double)
        }
        return positionsToConstruct.toList()
    }

}