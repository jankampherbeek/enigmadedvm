/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.exceptions

/**
 * Exception to handle failures when persisting data.
 */
class SaveException(message: String?) : Exception(message)
