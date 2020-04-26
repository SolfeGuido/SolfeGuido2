package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.answerButton
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.borderLabel
import io.github.solfeguido.ui.BorderButton
import ktx.actors.onClick
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.table

class ButtonAnswerActor : Table(), KTable {

    private val noteList : GdxArray<BorderButton> = gdxArrayOf()

    init {
        debug = false
        arrayOf("Do", "Ré", "Mi", "Fa", "Sol", "La", "Si").forEach {
            noteButton(it)
        }

        table {
            borderButton(IconName.SharpAccidental.value, "iconButtonStyle"){
                onClick { }
            }.inCell.grow().pad(5f)
            row()
            borderButton(IconName.FlatAccidental.value, "iconButtonStyle"){

            }.inCell.grow().pad(5f)
        }.inCell.grow().pad(5f)
    }

    private fun noteButton(note: String) = answerButton(note) {
        onClick { println(note) }
        //this@ButtonAnswerActor.noteList.add(this)
    }.inCell.grow()

    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height/ 4f
}