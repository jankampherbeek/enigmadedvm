package com.radixpro.enigma.dedvm.core

data class DateTimeParts(val year: Int,
                         val month: Int,
                         val day: Int,
                         val hour: Int,
                         val minute: Int,
                         val second: Int,
                         val offsetUt: Double)


data class DateTimeJulian(val jd: Double,
                          val calendar: String)

data class Location(val geoLat: Double,
                    val geoLon: Double)

data class ChartInputData(val id: Int,
                          val name: String,
                          val dateTimeParts: DateTimeParts,
                          val dateTime: DateTimeJulian,
                          val location: Location)