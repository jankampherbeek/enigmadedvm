/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */

package com.radixpro.enigma.dedvm.ui

import com.radixpro.enigma.dedvm.handlers.*
import com.radixpro.enigma.dedvm.persistency.PropertyReader
import com.radixpro.enigma.dedvm.persistency.PropertyWriter
import com.radixpro.enigma.dedvm.ui.Rosetta.getHelpText
import com.radixpro.enigma.dedvm.ui.Rosetta.getText
import com.radixpro.enigma.dedvm.ui.UiDictionary.GAP
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.log4j.Logger
import java.io.File

const val ctrlGroupKey = "nrctrlgroups"

/**
 * StartScreen for the application.
 */
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
    private val feedback: Feedback,
    private val propReader: PropertyReader,
    private val propWriter: PropertyWriter
) {
    private val log: Logger = Logger.getLogger(Dashboard::class.java)

    // texts
    private lateinit var lblAvailableTests: Label
    private lateinit var txtLblControlGroup: String
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
    private lateinit var tfNrOfCtrlGroups: TextField
    private var nrOfCtrlGroups = 1
    private val height = 500.0
    private val width = 600.0
    private lateinit var stage: Stage
    private val fileNameData = ".${File.separator}data${File.separator}calculatedcharts.json"
    private val fileNameCtrlData = ".${File.separator}data${File.separator}controlcharts.json"

    fun showDashboard() {
        log.info("EnigmaDedVM started.")
        initialize()
        stage = Stage()
        stage.minHeight = height
        stage.minWidth = width
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = txtTitle
        stage.scene = Scene(createVBox())
        stage.show()
    }

    private fun initialize() {
        defineProperties()
        defineButtons()
        defineCheckBoxes()
        defineTexts()
        defineTextFields()
        checkStatus()
    }

    private fun createVBox(): VBox {
        return VBoxBuilder().setPadding(GAP).setHeight(height + 2 * GAP).setWidth(width + 2 * GAP)
            .setChildren(arrayOf(createGridPane())).build()
    }

    private fun defineProperties() {
        val value = propReader.readProperty(ctrlGroupKey)
        if (value.isNotEmpty()) nrOfCtrlGroups = value.toInt()
        else {
            nrOfCtrlGroups = 1
            propWriter.writeProperty(ctrlGroupKey, nrOfCtrlGroups.toString())
        }
    }

    private fun defineButtons() {
        btnDataFile =
            ButtonBuilder("dashboard.btn_datafile").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true)
                .build()
        btnExit = ButtonBuilder("dashboard.btn_exit").setDisabled(false).setFocusTraversable(true).build()
        btnHelp = ButtonBuilder("dashboard.btn_help").setDisabled(false).setFocusTraversable(true).build()
        btnRun = ButtonBuilder("dashboard.btn_run").setDisabled(true).setFocusTraversable(false).build()
        btnLanguage =
            ButtonBuilder("dashboard.btn_language").setPrefWidth(200.0).setDisabled(false).setFocusTraversable(true)
                .build()
        btnLanguage.onAction = EventHandler { onLanguage() }
        btnDataFile.onAction = EventHandler { onDataFile() }
        btnRun.onAction = EventHandler { onPerformTests() }
        btnExit.onAction = EventHandler { onExit() }
        btnHelp.onAction = EventHandler { onHelp() }
    }

    private fun defineCheckBoxes() {
        cbSma = CheckBox(getText("dashboard.cb_sunmoonascinsign"))
        cbBam = CheckBox(getText("dashboard.cb_inhouses1or10"))
        cbBco = CheckBox(getText("dashboard.cb_atcorners"))
        cbElev = CheckBox(getText("dashboard.cb_elevated"))
        cbPra = CheckBox(getText("dashboard.cb_prominent"))
        cbNas = CheckBox(getText("dashboard.cb_unaspected"))
        cbMax = CheckBox(getText("dashboard.cb_maximal"))
        cbPri = CheckBox(getText("dashboard.cb_principles"))

    }

    private fun defineTexts() {
        lblAvailableTests = LabelBuilder().setText(getText("dashboard.lbl_availabletests")).build()
        txtLblControlGroup = getText("dashboard.lbl_definecontrolgroup")
        txtLblInfo = getText("dashboard.lbl_info")
        txtLblRetrieveDataFile = getText("dashboard.lbl_retrievedatafile")
        txtTitle = getText("dashboard.title")
    }

    private fun defineTextFields() {
        tfNrOfCtrlGroups = TextField()
        tfNrOfCtrlGroups.prefColumnCount = 4
        tfNrOfCtrlGroups.text = nrOfCtrlGroups.toString()
    }

    private fun checkStatus() {
        val dataFile = File(fileNameData)
        val ctrlDataFile = File(fileNameCtrlData)
        val tempNrOfCtrlGroups = try {
            tfNrOfCtrlGroups.text.toInt()
        } catch (e: Exception) {
            -1
        }
//        if (dataFile.exists() && ctrlDataFile.exists()) {
        if (dataFile.exists()) {
            log.info("Data file does exist.")
            nrOfCtrlGroups = tempNrOfCtrlGroups
            setNodeStatus(tfNrOfCtrlGroups, true)
            setNodeStatus(btnDataFile, true)
            setNodeStatus(btnRun, false)
        } else if (tempNrOfCtrlGroups < 1) {
            log.error("Value for number of controlgroups is invalid.")
            setNodeStatus(tfNrOfCtrlGroups, false)
            setNodeStatus(btnDataFile, true)
            setNodeStatus(btnRun, true)
        } else {
            log.info("No data file and/or no controldata file.")
            setNodeStatus(tfNrOfCtrlGroups, false)
            setNodeStatus(btnDataFile, false)
            setNodeStatus(btnRun, true)
        }
    }

    private fun setNodeStatus(node: Control, disabled: Boolean) {
        node.isDisable = disabled
        node.isFocusTraversable = !disabled
    }


    private fun createGridPane(): GridPane {
        val grid = GridPaneBuilder().setHGap(GAP).setVGap(GAP).setPrefWidth(width).setPrefHeight(height)
            .setStyleSheet(styleSheet).build()
        grid.add(createTitlePane(), 0, 0, 3, 1)
        grid.add(createImagePane(), 0, 1, 1, 3)
        grid.add(createGenInfoPane(), 0, 4, 1, 8)
        grid.add(createLanguagePane(), 0, 13, 1, 1)
        grid.add(createSingleLinePane(txtLblControlGroup), 1, 1, 1, 1)
        grid.add(tfNrOfCtrlGroups, 2, 1, 1, 1)
        grid.add(createSingleLinePane(txtLblRetrieveDataFile), 1, 2, 1, 1)
        grid.add(btnDataFile, 2, 2, 1, 1)
        grid.add(lblAvailableTests, 1, 4, 1, 1)
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
        val lblTitle =
            LabelBuilder().setText(txtTitle).setPrefWidth(width).setAlignment(Pos.CENTER).setStyleClass("titletext")
                .build()
        return PaneBuilder().setHeight(57.0).setWidth(width).setStyleClass("titlepane").setChildren(arrayOf(lblTitle))
            .build()
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

    private fun saveProperty() {
        propWriter.writeProperty(ctrlGroupKey, nrOfCtrlGroups.toString())
    }

    private fun onDataFile() {
        val dataFile = FileChooser().showOpenDialog(stage)
        if (null != dataFile) {
            try {
                inputDataHandler.handleDataForMultipleSubControlGroups(dataFile, nrOfCtrlGroups)
                showFeedback(getText("dashboard.msg_dataimported"))
                log.info("Data has been imported.")
            } catch (e: Exception) {
                showFeedback(getText("dashboard.msg_error"))
                log.error("Could not import data: " + e.message)
            }
            checkStatus()
        }
    }

    private fun onPerformTests() {
        saveProperty()
        try {
            if (cbSma.isSelected) smaInSignHandler.processCharts(nrOfCtrlGroups)
            if (cbBam.isSelected) bodiesInHouseHandler.processCharts(nrOfCtrlGroups)
            if (cbBco.isSelected) bodiesAtCornersHandler.processCharts(nrOfCtrlGroups)
            if (cbElev.isSelected) elevationHandler.processCharts(nrOfCtrlGroups)
            if (cbPra.isSelected) prominentAspectsHandler.processCharts(nrOfCtrlGroups)
            if (cbMax.isSelected) maxPointsHandler.processCharts(nrOfCtrlGroups)
            if (cbNas.isSelected) unaspectedPointsHandler.processCharts(nrOfCtrlGroups)
            if (cbPri.isSelected) principleHandler.processCharts(nrOfCtrlGroups)
            if (cbSma.isSelected || cbBam.isSelected || cbBco.isSelected || cbElev.isSelected || cbMax.isSelected || cbNas.isSelected || cbPra.isSelected ||
                cbPri.isSelected
            ) showFeedback(getText("dashboard.msg_results"))
            else showFeedback(getText("dashboard.msg_noselection"))
        } catch (e: Exception) {
            log.error(e.message)
            showFeedback(getText("dashboard.msg_error"))
        }
    }

    private fun onLanguage() {
        stage.close()
        Rosetta.changeLanguage()
        showDashboard()
    }

    private fun onHelp() {
        Help(getHelpText("help.dashboard.title"), getHelpText("help.dashboard.content")).showContent()
    }

    private fun onExit() {
        log.info("Closing EnigmaDedVM.")
        Platform.exit()
    }

}