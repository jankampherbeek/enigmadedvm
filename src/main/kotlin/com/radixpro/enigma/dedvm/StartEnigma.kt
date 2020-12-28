package com.radixpro.enigma.dedvm

import com.radixpro.enigma.dedvm.di.Injector
import javafx.application.Application
import javafx.stage.Stage

class StartEnigma: Application() {

    override fun start(primaryStage: Stage) {
        Injector.injectDashboard().showDashboard()
    }

}