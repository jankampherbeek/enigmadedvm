/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.ui

import com.radixpro.enigma.dedvm.handlers.*
import com.radixpro.enigma.dedvm.ui.UiDictionary.GAP
import javafx.application.Platform
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
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File

class Dashboard(
    private val inputDataHandler: InputDataHandler,
    private val smaInSignHandler: SMAInSignHandler,
    private val bodiesInHouseHandler: BodiesInHouseHandler,
    private val bodiesAtCornersHandler: BodiesAtCornersHandler,
    private val elevationHandler: ElevationHandler,
    private val prominentAspectsHandler: ProminentAspectsHandler,
    private val unaspectedPointsHandler: UnaspectedPointsHandler,
    private val maxPointsHandler: MaxPointsHandler,
    private val principleHandler: PrincipleHandler,
    private val feedback: Feedback
) {
    // texts
    private lateinit var txtLblInfo: String
    private lateinit var txtLblRetrieveDataFile: String
    private lateinit var txtTitle: String

    // buttons
    private lateinit var btnDataFile: Button
    private lateinit var btnExit: Button
    private lateinit var btnHelp: Button
    private lateinit var btnRun: Button
    private lateinit var btnLanguage: Button

    // checkboxes
    private lateinit var cbSma: CheckBox
    private lateinit var cbBam: CheckBox
    private lateinit var cbBco: CheckBox
    private lateinit var cbElev: CheckBox
    private lateinit var cbPra: CheckBox
    private lateinit var cbNas: CheckBox
    private lateinit var cbMax: CheckBox
    private lateinit var cbPri: CheckBox

    // general
    private val height = 500.0
    private val width = 600.0
    private lateinit var stage: Stage
    private val fileNameData = ".${File.separator}data${File.separator}calculatedcharts.json"
    private val fileNameCtrlData = ".${File.separator}data${File.separator}controlcharts.json"

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
        checkStatus()
    }

    private fun defineButtons() {
        btnDataFile = ButtonBuilder("dashboard.btn_datafile").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
        btnExit = ButtonBuilder("dashboard.btn_exit").setDisabled(false).setFocusTraversable(true).build()
        btnHelp = ButtonBuilder("dashboard.btn_help").setDisabled(false).setFocusTraversable(true).build()
        btnRun = ButtonBuilder("dashboard.btn_run").setDisabled(true).setFocusTraversable(false).build()
        btnLanguage = ButtonBuilder("dashboard.btn_language").setPrefWidth(200.0).setDisabled(false).setFocusTraversable(true).build()
        btnLanguage.onAction = EventHandler { onLanguage() }
        btnDataFile.onAction = EventHandler { onDataFile() }
        btnRun.onAction = EventHandler { onPerformTests() }
        btnExit.onAction = EventHandler { onExit() }
    }

    private fun defineCheckBoxes() {

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
        txtTitle = Rosetta.getText("dashboard.title")
    }

    private fun checkStatus() {
        val dataFile = File(fileNameData)
        val ctrlDataFile = File(fileNameCtrlData)
        if (dataFile.exists() && ctrlDataFile.exists()) {
            btnDataFile.isDisable = true
            btnDataFile.isFocusTraversable = false
            btnRun.isDisable = false
            btnRun.isFocusTraversable = true
        } else {
            btnDataFile.isDisable = false
            btnDataFile.isFocusTraversable = true
            btnRun.isDisable = true
            btnRun.isFocusTraversable = false
        }
    }


    private fun createGridPane(): GridPane {
        val grid = GridPaneBuilder().setHGap(GAP).setVGap(GAP).setPrefWidth(width).setPrefHeight(height).setStyleSheet(styleSheet).build()
        grid.add(createTitlePane(), 0, 0, 3, 1)
        grid.add(createImagePane(), 0, 1, 1, 3)
        grid.add(createGenInfoPane(), 0, 4, 1, 8)
        grid.add(createLanguagePane(), 0, 13, 1, 1)
        grid.add(createSingleLinePane(txtLblRetrieveDataFile), 1, 1, 1, 1)
        grid.add(LabelBuilder().setText(Rosetta.getText("dashboard.lbl_availabletests")).build(),1, 3, 1, 1)
        grid.add(btnDataFile, 2, 1, 1, 1)
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

    private fun showFeedback(msg: String) {
        feedback.show(msg)
    }

    private fun onDataFile() {
        val dataFile = FileChooser().showOpenDialog(stage)
        if (null != dataFile) {
            try {
                inputDataHandler.handleData(dataFile)
                showFeedback(Rosetta.getText("dashboard.msg_dataimported"))
            } catch(e: Exception) {
                showFeedback(Rosetta.getText("dashboard.msg_error"))
            }
            checkStatus()
        }
    }

    private fun onPerformTests() {
        try {
            if (cbSma.isSelected) smaInSignHandler.processCharts()
            if (cbBam.isSelected) bodiesInHouseHandler.processChartsAscMc()
            if (cbBco.isSelected) bodiesAtCornersHandler.processCharts()
            if (cbElev.isSelected) elevationHandler.processCharts()
            if (cbMax.isSelected) maxPointsHandler.processCharts()
            if (cbNas.isSelected) unaspectedPointsHandler.processCharts()
            if (cbPra.isSelected) prominentAspectsHandler.processCharts()
            if (cbPri.isSelected) principleHandler.processCharts()
            if (cbSma.isSelected || cbBam.isSelected || cbBco.isSelected || cbElev.isSelected || cbMax.isSelected || cbNas.isSelected || cbPra.isSelected ||
                    cbPri.isSelected) showFeedback(Rosetta.getText("dashboard.msg_results"))
            else showFeedback(Rosetta.getText("dashboard.msg_noselection"))
        } catch (e: Exception) {
            showFeedback(Rosetta.getText("dashboard.msg_error"))
        }
    }

    private fun onLanguage() {
        stage.close()
        Rosetta.changeLanguage()
        showDashboard()
    }

    private fun onExit() {
        Platform.exit()
    }

}