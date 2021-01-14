/*
 *
 *  * Jan Kampherbeek, (c) 2020, 2021.
 *  * EnigmaDedVM is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm

/**
 * Helper class for starting the application.
 */
object App {
    @JvmStatic
    fun main(args: Array<String?>) {
        StartEnigma().run(args)
    }
}
