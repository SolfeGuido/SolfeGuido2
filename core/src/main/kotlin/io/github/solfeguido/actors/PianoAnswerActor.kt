package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.NoteNameEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.enums.PianoKeyTypeEnum
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.enums.NoteStyle
import io.github.solfeguido.factories.firePooled
import io.github.solfeguido.ui.PianoKey
import io.github.solfeguido.ui.events.AnswerGivenEvent
import ktx.actors.onClick


class PianoAnswerActor(private val noteStyle: NoteStyle, private val showNotes: Boolean = false) :
    Stack() {

    init {
        debug = false
        val whiteKeys = Table()
        val blackKeys = Table()
        val whiteWidth = prefWidth / 7
        val blackWidth = whiteWidth / 2
        val blackPadding = whiteWidth - (blackWidth / 2)
        //blackKeys.space(10f)

        val createWhiteKey = { note: NoteOrderEnum ->
            val text = if (showNotes) NoteNameEnum[note.name, noteStyle] else ""
            val key = PianoKey(text, type = PianoKeyTypeEnum.White)
            key.onClick {
                firePooled<AnswerGivenEvent> { this.note = note }
            }
            whiteKeys.add(key).grow()
        }

        val createBlackKey = { note: NoteOrderEnum, leftPad: Float, rightPad: Float ->
            val key = PianoKey("", type = PianoKeyTypeEnum.Black)
            key.onClick {
                firePooled<AnswerGivenEvent> { this.note = note }
            }
            blackKeys.add(key).grow()
                .pad(0f, leftPad, 0f, rightPad)
                .align(Align.top).height(3 * prefHeight / 4f)
        }


        createWhiteKey(NoteOrderEnum.fromIndex(0))
        createBlackKey(NoteOrderEnum.fromIndex(1), blackPadding, blackPadding / 3)
        createWhiteKey(NoteOrderEnum.fromIndex(2))
        createBlackKey(NoteOrderEnum.fromIndex(3), blackPadding / 3, blackPadding)
        createWhiteKey(NoteOrderEnum.fromIndex(4))
        createWhiteKey(NoteOrderEnum.fromIndex(5))
        createBlackKey(NoteOrderEnum.fromIndex(6), blackPadding, blackPadding / 3)
        createWhiteKey(NoteOrderEnum.fromIndex(7))
        createBlackKey(NoteOrderEnum.fromIndex(8), blackPadding / 3, blackPadding / 3)
        createWhiteKey(NoteOrderEnum.fromIndex(9))
        createBlackKey(NoteOrderEnum.fromIndex(10), blackPadding / 3, blackPadding)
        createWhiteKey(NoteOrderEnum.fromIndex(11))
        this.add(whiteKeys)
        this.add(blackKeys)
    }


    override fun getPrefWidth() = Constants.WIDTH.toFloat()
    override fun getPrefHeight() = Constants.HEIGHT / 4f

}