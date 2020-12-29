/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.ui

import java.util.*

/**
 * i18N manager, takes care of Resource Bundles and Locale's.<br></br>
 * Implemented as a singleton.
 */
object Rosetta {

    private const val RB_LOCATION = "texts"
    private const val RB_HELP_LOCATION = "help"
    private const val DUTCH = "du"
    private const val ENGLISH = "en"

    private val localeEn: Locale = Locale(ENGLISH, ENGLISH.toUpperCase())
    private val localeDu: Locale = Locale(DUTCH, DUTCH.toUpperCase())
    private var localeCurrent = localeEn
    private var resourceBundle = ResourceBundle.getBundle(RB_LOCATION, localeCurrent)
    private var helpResourceBundle = ResourceBundle.getBundle(RB_HELP_LOCATION, localeCurrent)


    /**
     * Change language, switches between Dutch and English.
     */
    fun changeLanguage() {
        localeCurrent = if(localeCurrent == localeEn) localeDu else localeEn
        defineResourceBundles()
    }


    private fun defineResourceBundles() {
        resourceBundle = ResourceBundle.getBundle(RB_LOCATION, localeCurrent)
        helpResourceBundle = ResourceBundle.getBundle(RB_HELP_LOCATION, localeCurrent)
    }

    fun getText(key: String): String {
        return resourceBundle!!.getString(key)
    }

    fun getHelpText(key: String): String {
        return helpResourceBundle!!.getString(key)
    }


}