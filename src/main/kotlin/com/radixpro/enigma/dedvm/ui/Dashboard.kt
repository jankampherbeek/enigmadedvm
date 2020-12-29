/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.ui

import com.radixpro.enigma.dedvm.ui.UiDictionary.GAP
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.ButtonBar
import javafx.scene.control.CheckBox
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage

class Dashboard {
    // texts
    private val txtBtnDataFile = "Chart data"
    private val txtBtnEventData = "Event data"
    private val txtBtnCharts = "Charts"
    private val txtBtnExit = "Exit"
    private val txtBtnHelp = "Help"
    private val txtBtnRun = "Run"
    private val txtBtnLanguage = "Nederlands/Dutch"
    private val txtCbSelectAll = "Select all"
    private val txtCbSunMoonAscInSign = "Sun, Moon, Asc in sign"
    private val txtCbInHouses1Or10 = "In houses 1 or 10"
    private val txtCbAtCorners = "At corners"
    private val txtCbElevated = "Elevated"
    private val txtCbProminent = "Prominent"
    private val txtCbUnaspected = "Unaspected"
    private val txtCbMaximal = "Maximal"
    private val txtCbPrinciples = "Principles"
    private val txtLblInfo = "EnigmaDedVM contains a set\nof methods to analyse charts.\nIt is created originally to support\nan investigation into " +
            "astrological\naspects of suicide by dutch researcher\nVivian Muller, but it can be used for\nnumerous other investigations."
    private val txtLblRetrieveDataFile = "Retrieve the data file"
    private val txtLblRetrieveEventFile = "Retrieve the event file"
    private val txtLblShowCharts =  "Show charts"
    private val txtTitle = "Enigma DedVM version 0.9 (Beta)"
    // buttons
    private val btnDataFile = ButtonBuilder().setText(txtBtnDataFile).setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
    private val btnEventFile = ButtonBuilder().setText(txtBtnEventData).setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
    private val btnCharts = ButtonBuilder().setText(txtBtnCharts).setPrefWidth(100.0).setDisabled(true).setFocusTraversable(false).build()
    private val btnExit = ButtonBuilder().setText(txtBtnExit).setDisabled(false).setFocusTraversable(true).build()
    private val btnHelp = ButtonBuilder().setText(txtBtnHelp).setDisabled(false).setFocusTraversable(true).build()
    private val btnRun = ButtonBuilder().setText(txtBtnRun).setDisabled(true).setFocusTraversable(false).build()
    private val btnLanguage = ButtonBuilder().setText(txtBtnLanguage).setPrefWidth(200.0).setDisabled(false).setFocusTraversable(true).build()
    // checkboxes
    private val cbAll = CheckBox(txtCbSelectAll)
    private val cbSma = CheckBox(txtCbSunMoonAscInSign)
    private val cbBam = CheckBox(txtCbInHouses1Or10)
    private val cbBco = CheckBox(txtCbAtCorners)
    private val cbElev = CheckBox(txtCbElevated)
    private val cbPra = CheckBox(txtCbProminent)
    private val cbNas = CheckBox(txtCbUnaspected)
    private val cbMax = CheckBox(txtCbMaximal)
    private val cbPri = CheckBox(txtCbPrinciples)
    // general
    private val height = 500.0
    private val width = 600.0
    private val stage = Stage()

    fun showDashboard() {

        stage.minHeight = height
        stage.minWidth = width
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = txtTitle
        stage.scene = Scene(createGridPane())
        stage.show()

    }

    private fun createGridPane(): GridPane {
        val grid = GridPaneBuilder().setHGap(GAP).setVGap(GAP).setPrefWidth(width).setPrefHeight(height).setStyleSheet(styleSheet).build()
        grid.add(createTitlePane(), 0, 0, 3, 1)
        grid.add(createImagePane(),0, 1, 1, 3)
        grid.add(createGenInfoPane(),0, 4, 1, 8)
        grid.add(createLanguagePane(), 0, 13, 1, 1)
        grid.add(createSingleLinePane(txtLblRetrieveDataFile),1, 1, 1, 1)
        grid.add(createSingleLinePane(txtLblRetrieveEventFile),1, 2, 1, 1)
        grid.add(createSingleLinePane(txtLblShowCharts),1, 3, 1, 1)
        grid.add(btnDataFile, 2, 1, 1, 1)
        grid.add(btnEventFile, 2, 2, 1, 1)
        grid.add(btnCharts, 2, 3, 1, 1)
        grid.add(cbAll, 1, 4, 2, 1)
        grid.add(cbSma, 1, 5, 2, 1)
        grid.add(cbBam, 1, 6, 2, 1)
        grid.add(cbBco, 1, 7, 2, 1)
        grid.add(cbElev, 1, 8, 2, 1)
        grid.add(cbPra, 1, 9, 2, 1)
        grid.add(cbNas, 1, 10, 2, 1)
        grid.add(cbMax, 1, 11, 2, 1)
        grid.add(cbPri, 1, 12, 2, 1)
        grid.add(createMainButtonBar(), 1, 13, 2, 1)
        return grid
    }

    private fun createTitlePane(): Pane {
        val lblTitle = LabelBuilder().setText(txtTitle).setPrefWidth(width).setAlignment(Pos.CENTER).setStyleClass("titletext").build()
        return PaneBuilder().setHeight(57.0).setWidth(width).setStyleClass("titlepane").setChildren(arrayOf(lblTitle)).build()
    }

    private fun createImagePane(): Pane {
        val image = Image("img/ziggurat.png")
        val imageView = ImageView(image)
        imageView.fitWidth = 223.0
        imageView.fitHeight = 86.0
        imageView.isPickOnBounds = true
        imageView.isPreserveRatio = true
        val pane = PaneBuilder().setWidth(250.0).setHeight(100.0).build()
        pane.children.add(imageView)
        return pane
    }

    private fun createGenInfoPane(): Pane {
        val lblInfo = LabelBuilder().setText(txtLblInfo).setPrefWidth(230.0).setAlignment(Pos.CENTER).build()
        lblInfo.isWrapText = true
        lblInfo.textAlignment = TextAlignment.CENTER
        return PaneBuilder().setHeight(120.0).setWidth(250.0).setChildren(arrayOf(lblInfo)).build()
    }

    private fun createLanguagePane(): Pane {
        return PaneBuilder().setHeight(30.0).setWidth(230.0).setChildren(arrayOf(btnLanguage)).build()
    }

    private fun createSingleLinePane(text: String): Pane {
        val lbl = LabelBuilder().setText(text).build()
        val pane = PaneBuilder().setHeight(28.0).setWidth(300.0).setChildren(arrayOf(lbl)).build()
        pane.style = "-fx-background-color: lightblue;"
        return pane
    }

    private fun createMainButtonBar(): ButtonBar {
        return ButtonBarBuilder().setButtons(arrayOf(btnExit, btnHelp, btnRun)).build()
    }

}