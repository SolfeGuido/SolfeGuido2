package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.factories.*
import io.github.solfeguido.ui.AnswerButton
import ktx.actors.onClick
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.scene2d.KTable
import ktx.scene2d.table

class ButtonAnswerActor : Table(), KTable {

    private val noteList : GdxArray<AnswerButton> = gdxArrayOf()

    private var currentAccidental: NoteAccidentalEnum = NoteAccidentalEnum.Natural

    init {
        debug = false
        arrayOf("Do", "Ré", "Mi", "Fa", "Sol", "La", "Si").forEach {
            noteButton(it)
        }
        val self = this
        table {
            val sharpBtn = borderButton(IconName.SharpAccidental.value, "iconBorderButtonStyle"){
                it.grow().pad(5f)
            }
            row()
            val flatBtn = borderButton(IconName.FlatAccidental.value, "iconBorderButtonStyle"){
                onClick {
                    self.switchButtonIcons(IconName.FlatAccidental)
                    self.currentAccidental = if(self.currentAccidental == NoteAccidentalEnum.Flat) {
                        this.setBackgroundColor(gCol("background"))
                        NoteAccidentalEnum.Natural
                    } else {
                        this.setBackgroundColor(gCol("correct"))
                        sharpBtn.setBackgroundColor(gCol("background"))
                        NoteAccidentalEnum.Flat
                    }
                }
                it.grow().pad(5f)
            }
            sharpBtn.onClick {
                this@ButtonAnswerActor.switchButtonIcons(IconName.SharpAccidental)
                self.currentAccidental = if(self.currentAccidental == NoteAccidentalEnum.Sharp) {
                    sharpBtn.setBackgroundColor(gCol("background"))
                    NoteAccidentalEnum.Natural
                } else {
                    sharpBtn.setBackgroundColor(gCol("correct"))
                    flatBtn.setBackgroundColor(gCol("background"))
                    NoteAccidentalEnum.Sharp
                }
            }
            it.grow().pad(5f)
        }
    }

    private fun switchButtonIcons(icon: IconName) = noteList.forEachIndexed { index, answerButton ->
        answerButton.toggleIcon(icon, index / 40f)
    }

    private fun noteButton(note: String) = answerButton(note) {
        onClick { println(note) }
        this@ButtonAnswerActor.noteList.add(this)
        it.grow()
    }

    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height/ 4f
}