package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.answerButton
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.ui.AnswerButton
import ktx.actors.onClick
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.scene2d.KTable
import ktx.scene2d.table

class ButtonAnswerActor : Table(), KTable {

    private val noteList : GdxArray<AnswerButton> = gdxArrayOf()

    init {
        debug = false
        arrayOf("Do", "Ré", "Mi", "Fa", "Sol", "La", "Si").forEach {
            noteButton(it)
        }

        table {
            borderButton(IconName.SharpAccidental.value, "iconButtonStyle"){
                onClick {
                    this@ButtonAnswerActor.switchButtonIcons(IconName.SharpAccidental)
                }
            }.inCell.grow().pad(5f)
            row()
            borderButton(IconName.FlatAccidental.value, "iconButtonStyle"){
                onClick {
                    this@ButtonAnswerActor.switchButtonIcons(IconName.FlatAccidental)
                }
            }.inCell.grow().pad(5f)
        }.inCell.grow().pad(5f)
    }

    private fun switchButtonIcons(icon: IconName) = noteList.forEachIndexed { index, answerButton ->
        answerButton.toggleIcon(icon, index / 20f)
    }

    private fun noteButton(note: String) = answerButton(note) {
        onClick { println(note) }
        this@ButtonAnswerActor.noteList.add(this)
    }.inCell.grow()

    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height/ 4f
}