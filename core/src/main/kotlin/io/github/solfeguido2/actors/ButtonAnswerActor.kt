package io.github.solfeguido2.actors

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.solfeguido2.core.IAnswerGiver
import io.github.solfeguido2.enums.*
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.factories.answerButton
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.firePooled
import io.github.solfeguido2.factories.gCol
import io.github.solfeguido2.ui.AnswerButton
import io.github.solfeguido2.events.AnswerGivenEvent
import io.github.solfeguido2.ui.PianoKey
import ktx.actors.onClick
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.scene2d.KTable
import ktx.scene2d.table

class ButtonAnswerActor(
    private val noteStyle: NoteStyle,
    showAccidentals: Boolean = false
) : Table(), KTable, IAnswerGiver {


    private val noteList: GdxArray<AnswerButton> = gdxArrayOf()
    private val noteOrders = mutableMapOf<NoteOrderEnum, AnswerButton>()


    private var currentAccidental: NoteAccidentalEnum = NoteAccidentalEnum.Natural

    init {
        NoteNameEnum.values().dropLast(1).forEach {
            noteButton(it)
        }
        val self = this
        if (showAccidentals) {
            table {
                val sharpBtn = borderButton(IconName.SharpAccidental.value, "iconBorderButtonStyle") {
                    it.grow().pad(5f)
                }
                row()
                val flatBtn = borderButton(IconName.FlatAccidental.value, "iconBorderButtonStyle") {
                    onClick {
                        self.switchButtonIcons(IconName.FlatAccidental)
                        self.currentAccidental = if (self.currentAccidental == NoteAccidentalEnum.Flat) {
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
                    self.currentAccidental = if (self.currentAccidental == NoteAccidentalEnum.Sharp) {
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
    }

    override fun highlightAnswer(note: NoteOrderEnum) {
        noteOrders[note]?.highlight()
    }

    private fun switchButtonIcons(icon: IconName) = noteList.forEachIndexed { index, answerButton ->
        answerButton.toggleIcon(icon, index / 40f)
    }

    private fun noteButton(noteName: NoteNameEnum) = answerButton(NoteNameEnum[noteName.value, noteStyle]) {
        val order = noteName.orderEnum
        this@ButtonAnswerActor.noteOrders[order] = this
        onClick {
            firePooled<AnswerGivenEvent> {
                val accidental = this@ButtonAnswerActor.currentAccidental
                note = when (accidental) {
                    NoteAccidentalEnum.Flat -> order.previous()
                    NoteAccidentalEnum.Sharp -> order.next()
                    else -> order
                }
            }
        }
        this@ButtonAnswerActor.noteList.add(this)
        it.grow()
    }

    override fun getPrefWidth() = Constants.WIDTH.toFloat()
    override fun getPrefHeight() = Constants.HEIGHT / 4f
}