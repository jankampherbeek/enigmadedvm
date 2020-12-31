/*
 * Jan Kampherbeek, (c) 2020, 2021.
 * EnigmaDedVM is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */
package com.radixpro.enigma.dedvm.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

const val styleSheet = "css/enigma.css"

/**
 * Creates a Label, based on the Builder pattern.
 */
class LabelBuilder {
    private var text = ""
    private var prefWidth = 0.0
    private var prefHeight = 0.0
    private var layoutX = 0.0
    private var layoutY = 0.0
    private var alignment: Pos? = null
    private var styleClass: String? = null

    fun setText(text: String): LabelBuilder {
        this.text = text
        return this
    }

    fun setPrefWidth(prefWidth: Double): LabelBuilder {
        this.prefWidth = prefWidth
        return this
    }

    fun setStyleClass(styleClass: String): LabelBuilder {
        this.styleClass = styleClass
        return this
    }

    fun setAlignment(alignment: Pos): LabelBuilder {
        this.alignment = alignment
        return this
    }

    fun build(): Label {
        val lblText: String = if (text.isNotEmpty()) text else ""
        val label = Label(lblText)
        if (prefWidth > 0.0) label.prefWidth = prefWidth
        if (prefHeight > 0.0) label.prefHeight = prefHeight
        if (layoutX > 0.0) label.layoutX = layoutX
        if (layoutY > 0.0) label.layoutY = layoutY
        if (null != styleClass && styleClass!!.isNotBlank()) label.styleClass.add(styleClass)
        if (null != alignment) label.alignment = alignment
        return label
    }
}


class PaneBuilder {
    private var width = 0.0
    private var height = 0.0
    private var styleClass: String? = null
    private var children: Array<Node>? = null

    fun setWidth(width: Double): PaneBuilder {
        this.width = width
        return this
    }

    fun setHeight(height: Double): PaneBuilder {
        this.height = height
        return this
    }

    fun setStyleClass(styleClass: String): PaneBuilder {
        this.styleClass = styleClass
        return this
    }

    fun setChildren(children: Array<Node>?): PaneBuilder {
        this.children = children
        return this
    }

    fun build(): Pane {
        val pane = Pane()
        if (width > 0.0) pane.prefWidth = width
        if (height > 0.0) pane.prefHeight = height
        if (styleClass != null && styleClass!!.isNotBlank()) pane.styleClass.add(styleClass)
        if (children != null && children!!.isNotEmpty()) pane.children.addAll(children!!)
        return pane
    }
}


/**
 * Creates a VBox, based on the Builder pattern.
 */
class VBoxBuilder {
    private var prefWidth = 0.0
    private var padding = 0.0
    private var prefHeight = 0.0
    private var style: String? = null
    private var children: Array<Node>? = null

    fun setWidth(prefWidth: Double): VBoxBuilder {
        this.prefWidth = prefWidth
        return this
    }

    fun setHeight(prefHeight: Double): VBoxBuilder {
        this.prefHeight = prefHeight
        return this
    }

    fun setPadding(padding: Double): VBoxBuilder {
        this.padding = padding
        return this
    }

    fun setChildren(children: Array<Node>?): VBoxBuilder {
        this.children = children
        return this
    }

    fun build(): VBox {
        val vBox = VBox()
        vBox.stylesheets.add(styleSheet)
        vBox.prefWidth = prefWidth
        if (prefHeight > 0.0) vBox.prefHeight = prefHeight
        if (padding > 0.0) vBox.padding = Insets(padding, padding, padding, padding)
        if (null != style && style!!.isNotBlank()) vBox.style = style
        if (children != null && children!!.isNotEmpty()) vBox.children.addAll(children!!)
        return vBox
    }
}


/**
 * Creates a Button, based on the Builder pattern.
 */
class ButtonBuilder(rbKey:String) {
    private var text = Rosetta.getText(rbKey)
    private var disabled = false
    private var focusTraversable = false
    private var prefWidth = 0.0

    fun setPrefWidth(width: Double): ButtonBuilder {
        this.prefWidth = width
        return this
    }

    fun setDisabled(disabled: Boolean): ButtonBuilder {
        this.disabled = disabled
        return this
    }

    fun setFocusTraversable(focusTraversable: Boolean): ButtonBuilder {
        this.focusTraversable = focusTraversable
        return this
    }

    fun build(): Button {
        var btnText = ""
        btnText = if (text.isNotEmpty()) text else ""
        val button = Button(btnText)
        button.isDisable = disabled
        button.isFocusTraversable = focusTraversable
        if (prefWidth > 1.0) button.prefWidth = prefWidth
        return button
    }
}

/**
 * Creates a GridPane, based on the Builder pattern.
 */
class GridPaneBuilder {
    private var prefHeight = 0.0
    private var prefWidth = 0.0
    private var padding = 0.0
    private var hGap = 0.0
    private var vGap = 0.0
    private var styleSheet: String? = null

    fun setPrefHeight(prefHeight: Double): GridPaneBuilder {
        this.prefHeight = prefHeight
        return this
    }

    fun setPrefWidth(prefWidth: Double): GridPaneBuilder {
        this.prefWidth = prefWidth
        return this
    }

    fun setVGap(vGap: Double): GridPaneBuilder {
        this.vGap = vGap
        return this
    }

    fun setHGap(hGap: Double): GridPaneBuilder {
        this.hGap = hGap
        return this
    }

    fun setStyleSheet(styleSheet: String): GridPaneBuilder {
        this.styleSheet = styleSheet
        return this
    }

    fun build(): GridPane {
        val gridPane = GridPane()
        gridPane.stylesheets.add(styleSheet)
        if (prefHeight > 0.0) gridPane.prefHeight = prefHeight
        if (prefWidth > 0.0) gridPane.prefWidth = prefWidth
        if (padding > 0.0) gridPane.padding = Insets(padding, padding, padding, padding)
        if (hGap > 0.0) gridPane.hgap = hGap
        if (vGap > 0.0) gridPane.vgap = vGap
        if (null != styleSheet && styleSheet!!.isNotBlank()) gridPane.stylesheets.add(styleSheet)
        return gridPane
    }
}

class ButtonBarBuilder {
    private var buttons: Array<Node>? = null
    fun setButtons(buttons: Array<Node>?): ButtonBarBuilder {
        this.buttons = buttons
        return this
    }

    fun build(): ButtonBar {
        val buttonBar = ButtonBar()
        if (null != buttons && buttons!!.isNotEmpty()) buttonBar.buttons.addAll(buttons!!)
        return buttonBar
    }
}
