package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.enums.NoteNameEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.factories.*
import io.github.solfeguido.ui.AnswerButton
import io.github.solfeguido.ui.events.AnswerGivenEvent
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
        NoteNameEnum.values().dropLast(1).forEach {
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

            // Declaring outside of init scope because
            // we need the "flatBtn" var to make it work
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

    private fun noteButton(noteName: NoteNameEnum) = answerButton(noteName.value) {
        val order = noteName.orderEnum
        onClick {
            firePooled<AnswerGivenEvent> {
                val accidental = this@ButtonAnswerActor.currentAccidental
                note = when(accidental) {
                    NoteAccidentalEnum.Flat -> order.previous()
                    NoteAccidentalEnum.Sharp -> order.next()
                    else -> order
                }
                println(note)
            }
        }
        this@ButtonAnswerActor.noteList.add(this)
        it.grow()
    }

    override fun getPrefWidth() = Constants.WIDTH.toFloat()
    override fun getPrefHeight() = Constants.HEIGHT / 4f
}