/*
 *
 *  * Jan Kampherbeek, (c) 2020, 2021.
 *  * EnigmaDedVM is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.persistency

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

const val propFile = "dedvm.properties"

class PropertyWriter() {

    fun writeProperty(key: String, value: String) {
        val inStream = FileOutputStream(propFile)
        val prop = Properties()
        prop.setProperty(key, value)
        prop.store(inStream, null)
    }
}

class PropertyReader() {

    fun readProperty(key: String): String {
        val inStream = FileInputStream(propFile)
        val prop = Properties()
        prop.load(inStream)
        return if (prop.getProperty(key)!= null) (prop.getProperty(key)) else ""
    }
}