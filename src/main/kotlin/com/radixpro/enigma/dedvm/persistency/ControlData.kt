/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.persistency

import com.radixpro.enigma.dedvm.core.ChartInputData
import com.radixpro.enigma.dedvm.core.DateTimeParts
import com.radixpro.enigma.dedvm.core.Location
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * Creates a set of controldata.
 */
class ControlDataCreator(private val randomizer: ListRandomizer,
                         private val controlDataCalendar: ControlDataCalendar) {

    private var controlInputData: MutableList<ChartInputData> = ArrayList()
    private var months: MutableList<Int> = ArrayList()
    private var years: MutableList<Int> = ArrayList()
    private var days: MutableList<Int> = ArrayList()
    private var utHours: MutableList<Int> = ArrayList()
    private var utMinutes: MutableList<Int> = ArrayList()
    private var utSeconds: MutableList<Int> = ArrayList()
    private var latitudes: MutableList<Double> = ArrayList()
    private var longitudes: MutableList<Double> = ArrayList()

    fun createControlData(inputData: List<ChartInputData>): List<ChartInputData> {
        processInputData(inputData)
        sortDaysAndShuffleOtherItems()
        processData()
        return controlInputData.toList()
    }


    private fun processInputData(inputDataSet:  List<ChartInputData>) {
        for (chartInputData in inputDataSet) {
            val dateTimeParts = chartInputData.dateTimeParts
            val location = chartInputData.location
            utHours.add(dateTimeParts.hour)
            utMinutes.add(dateTimeParts.minute)
            utSeconds.add(dateTimeParts.second)
            years.add(dateTimeParts.year)
            months.add(dateTimeParts.month)
            days.add(dateTimeParts.day)
            latitudes.add(location.geoLat)
            longitudes.add(location.geoLon)
        }
    }


    private fun sortDaysAndShuffleOtherItems() {
        days.sort()
        days.reverse()
        randomizer.randomize(years)
        randomizer.randomize(months)
        randomizer.randomize(utHours)
        randomizer.randomize(utMinutes)
        randomizer.randomize(utSeconds)
        randomizer.randomize(latitudes)
        randomizer.randomize(longitudes)
    }

    private fun processData() {   // use only UT
        var counter = 0
        while (years.size > 0) {
            val year = getIntFromList(years)
            val day = getIntFromList(days)
            val month = findMonth(day, year)
            val utHour = getIntFromList(utHours)
            val utMinute = getIntFromList(utMinutes)
            val utSecond = getIntFromList(utSeconds)
            val latitude = getDoubleFromList(latitudes)
            val longitude = getDoubleFromList(longitudes)
            val location = Location(longitude, latitude)
            val dateTimeParts = DateTimeParts(year, month, day, utHour, utMinute, utSecond, 0.0)
            val id = counter++
            val chartInputData = ChartInputData(id, "Controldata $id", dateTimeParts, location)
            controlInputData.add(chartInputData)
        }
    }

    private fun getIntFromList(theList: MutableList<Int>): Int {
        val result = theList[0]
        theList.removeAt(0)
        return result
    }

    private fun getDoubleFromList(theList: MutableList<Double>): Double {
        val result = theList[0]
        theList.removeAt(0)
        return result
    }

    private fun findMonth(day: Int, year: Int): Int {
        var found = false
        var month = 0
        var counter = 0
        while (!found && counter < months.size) {
            month = months[counter]
            if (controlDataCalendar.dayFitsInMonth(day, month, year)) {
                found = true
                months.removeAt(counter)
            }
            counter++
        }
        return month
    }

}


/**
 * Interpreting the calendar. Helper class for the construction of controldata.
 */
class ControlDataCalendar {
    private val months31 = listOf(1, 3, 5, 7, 8, 10, 12)
    private val months30 = listOf(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    /**
     * Use only for Gregorian calendar and for UT
     */
    fun dayFitsInMonth(day: Int, month: Int, year: Int): Boolean {
        return (day < 29
                || day == 29 && 2 != month
                || day == 30 && months30.contains(month)
                || day == 31 && months31.contains(month)
                || isLeapYear(year) && day < 30)
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 400 == 0 || year % 100 != 0 && year % 4 == 0
    }
}



/**
 * Pseudo-randomizes the sequence of items in a collection.
 * Uses the nanoseconds of the current time as seed for the randomizer.
 */
class ListRandomizer {
    /**
     * Perform the randomization.
     * @param shuffleThis The list to randomize.
     * @return The randomized list.
     */
    fun randomize(shuffleThis: MutableList<*>): List<*> {
        return shuffle(shuffleThis)
    }

    private fun shuffle(shuffleThis: MutableList<*>): List<*> {
        val seed = generateSeedFromTime()
        val random = createRandomizer(seed)
        shuffleThis.shuffle(random)
        return shuffleThis
    }

    private fun generateSeedFromTime(): Long {
        return LocalDateTime.now().nano.toLong()
    }

    private fun createRandomizer(seed: Long): Random {
        val random = Random()
        random.setSeed(seed)
        return random
    }
}