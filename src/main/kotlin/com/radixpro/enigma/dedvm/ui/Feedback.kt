/*
 *
 *  * Jan Kampherbeek, (c) 2020, 2021.
 *  * EnigmaDedVM is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.dedvm.ui

import com.radixpro.enigma.dedvm.ui.UiDictionary.GAP
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.ButtonBar
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * Popup with user-feedback.
 */
class Feedback {

    // general
    private val height = 80.0
    private val width = 280.0
    private lateinit var stage: Stage

    fun show(msg: String) {
        stage = Stage()
        stage.minHeight = height
        stage.minWidth = width
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = Rosetta.getText("feedback.title")
        stage.scene = Scene(createVBox(msg))
        stage.show()
    }

    private fun createVBox(msg: String): VBox {
        return VBoxBuilder().setWidth(width).setHeight(height).setPadding(GAP).setChildren(arrayOf(createTextPane(msg), createBtnBar())).build()
    }

    private fun createTextPane(msg: String): Pane {
        val lblMessage = LabelBuilder().setText(msg).setPrefWidth(width).setAlignment(Pos.CENTER).build()
        return PaneBuilder().setWidth(width).setHeight(height).setChildren(arrayOf(lblMessage)).build()
    }

    private fun createBtnBar(): ButtonBar {
        val btnClose = ButtonBuilder("feedback.btn_close").build()
        btnClose.onAction = EventHandler { stage.close() }
        return ButtonBarBuilder().setButtons(arrayOf(btnClose)).build()
    }

}