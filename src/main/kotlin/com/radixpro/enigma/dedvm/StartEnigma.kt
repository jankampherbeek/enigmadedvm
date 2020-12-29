/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm

import com.radixpro.enigma.dedvm.di.Injector
import javafx.application.Application
import javafx.stage.Stage

class StartEnigma: Application() {

    override fun start(primaryStage: Stage) {
        Injector.injectDashboard().showDashboard()
    }

}