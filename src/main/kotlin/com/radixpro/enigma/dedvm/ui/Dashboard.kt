/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.ui

import com.radixpro.enigma.dedvm.ui.UiDictionary.GAP
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
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
    private lateinit var txtLblInfo : String
    private lateinit var txtLblRetrieveDataFile: String
    private lateinit var txtLblRetrieveEventFile : String
    private lateinit var txtLblShowCharts : String
    private lateinit var txtTitle : String
    // buttons
    private lateinit var btnDataFile : Button
    private lateinit var btnEventFile : Button
    private lateinit var btnCharts : Button
    private lateinit var btnExit  : Button
    private lateinit var btnHelp  : Button
    private lateinit var btnRun : Button
    private lateinit var btnLanguage  : Button
    // checkboxes
    private lateinit var cbAll : CheckBox
    private lateinit var cbSma : CheckBox
    private lateinit var cbBam : CheckBox
    private lateinit var cbBco : CheckBox
    private lateinit var cbElev : CheckBox
    private lateinit var cbPra : CheckBox
    private lateinit var cbNas : CheckBox
    private lateinit var cbMax : CheckBox
    private lateinit var cbPri : CheckBox
    // general
    private val height = 500.0
    private val width = 600.0
    private lateinit var stage: Stage

    fun showDashboard() {
        initialize()
        stage = Stage()
        stage.minHeight = height
        stage.minWidth = width
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = txtTitle
        stage.scene = Scene(createGridPane())
        stage.show()
    }

    private fun initialize() {
        defineButtons()
        defineCheckBoxes()
        defineTexts()
    }

    private fun defineButtons() {
        btnDataFile = ButtonBuilder("dashboard.btn_datafile").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
        btnEventFile = ButtonBuilder("dashboard.btn_eventdata").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
        btnCharts = ButtonBuilder("dashboard.btn_charts").setPrefWidth(100.0).setDisabled(true).setFocusTraversable(false).build()
        btnExit = ButtonBuilder("dashboard.btn_exit").setDisabled(false).setFocusTraversable(true).build()
        btnHelp = ButtonBuilder("dashboard.btn_help").setDisabled(false).setFocusTraversable(true).build()
        btnRun = ButtonBuilder("dashboard.btn_run").setDisabled(true).setFocusTraversable(false).build()
        btnLanguage = ButtonBuilder("dashboard.btn_language").setPrefWidth(200.0).setDisabled(false).setFocusTraversable(true).build()
        btnLanguage.onAction = EventHandler { onLanguage()}
    }

    private fun defineCheckBoxes() {
        cbAll = CheckBox(Rosetta.getText("dashboard.cb_selectall"))
        cbSma = CheckBox(Rosetta.getText("dashboard.cb_sunmoonascinsign"))
        cbBam = CheckBox(Rosetta.getText("dashboard.cb_inhouses1or10"))
        cbBco = CheckBox(Rosetta.getText("dashboard.cb_atcorners"))
        cbElev = CheckBox(Rosetta.getText("dashboard.cb_elevated"))
        cbPra = CheckBox(Rosetta.getText("dashboard.cb_prominent"))
        cbNas = CheckBox(Rosetta.getText("dashboard.cb_unaspected"))
        cbMax = CheckBox(Rosetta.getText("dashboard.cb_maximal"))
        cbPri = CheckBox(Rosetta.getText("dashboard.cb_principles"))
    }

    private fun defineTexts() {
        txtLblInfo = Rosetta.getText("dashboard.lbl_info")
        txtLblRetrieveDataFile = Rosetta.getText("dashboard.lbl_retrievedatafile")
        txtLblRetrieveEventFile = Rosetta.getText("dashboard.lbl_retrieveeventfile")
        txtLblShowCharts = Rosetta.getText("dashboard.lbl_showcharts")
        txtTitle = Rosetta.getText("dashboard.title")
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

    private fun onLanguage() {
        stage.close()
        Rosetta.changeLanguage()
        showDashboard()

    }

}