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

    private val height = 500.0
    private val width = 600.0

    private val stage = Stage()
    private val btnDataFile = ButtonBuilder().setText("Find data").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
    private val btnEventFile = ButtonBuilder().setText("Event data").setPrefWidth(100.0).setDisabled(false).setFocusTraversable(true).build()
    private val btnCharts = ButtonBuilder().setText("Charts").setPrefWidth(100.0).setDisabled(true).setFocusTraversable(false).build()
    private val btnExit = ButtonBuilder().setText("Exit").setDisabled(false).setFocusTraversable(true).build()
    private val btnHelp = ButtonBuilder().setText("Help").setDisabled(false).setFocusTraversable(true).build()
    private val btnRun = ButtonBuilder().setText("Run").setDisabled(true).setFocusTraversable(false).build()
    private val btnLanguage = ButtonBuilder().setText("Nederlands/Dutch").setPrefWidth(200.0).setDisabled(false).setFocusTraversable(true).build()

    private val cbAll = CheckBox("Select all")
    private val cbSma = CheckBox("Sun, Moon, Asc in sign")
    private val cbBam = CheckBox("In houses 1 or 10")
    private val cbBco = CheckBox("At corners")
    private val cbElev = CheckBox("Elevated")
    private val cbPra = CheckBox("Prominent")
    private val cbNas = CheckBox("Unaspected")
    private val cbMax = CheckBox("Maximal")
    private val cbPri = CheckBox("Principles")


    fun showDashboard() {

        stage.minHeight = height
        stage.minWidth = width
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Enigma DedVM version 0.9 (Beta)"
        stage.scene = Scene(createGridPane())
        stage.show()

    }

    private fun createGridPane(): GridPane {
        val grid = GridPaneBuilder().setHGap(GAP).setVGap(GAP).setPrefWidth(width).setPrefHeight(height).setStyleSheet(styleSheet).build()
        // row, col, rowspan, colspan
        grid.add(createTitlePane(), 0, 0, 3, 1)
        grid.add(createImagePane(),0, 1, 1, 3)
        grid.add(createGenInfoPane(),0, 4, 1, 8)
        grid.add(createLanguagePane(), 0, 13, 1, 1)
        grid.add(createSingleLinePane("Retrieve the data file"),1, 1, 1, 1)
        grid.add(createSingleLinePane("Retrieve the event file"),1, 2, 1, 1)
        grid.add(createSingleLinePane("Show charts"),1, 3, 1, 1)
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
        val lblTitle = LabelBuilder().setText("Enigma DedVM version 0.9 (Beta)").setPrefWidth(width).setAlignment(Pos.CENTER).setStyleClass("titletext").build()
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
        val infoText = "EnigmaDedVM contains a set\n"+
                "of methods to analyse charts.\n"+
                "It is created originally to support\n" +
                "an investigation into astrological\n"+
                "aspects of suicide by dutch researcher\n"+
                "Vivian Muller, but it can be used for\n"+
                "numerous other investigations."
        val lblInfo = LabelBuilder().setText(infoText).setPrefWidth(230.0).setAlignment(Pos.CENTER).build()
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
        pane.style = "-fx-background-color: lightblue;";
        return pane
    }

    private fun createMainButtonBar(): ButtonBar {
        return ButtonBarBuilder().setButtons(arrayOf(btnExit, btnHelp, btnRun)).build()
    }

}