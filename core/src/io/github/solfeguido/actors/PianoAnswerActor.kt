package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.enums.PianoKeyTypeEnum
import io.github.solfeguido.factories.onAnswer
import io.github.solfeguido.ui.PianoKey
import ktx.actors.onClick
import kotlin.math.max
import kotlin.math.min


class PianoAnswerActor : Stack() {

    init {
        debug = false
        val whiteKeys = Table()
        val blackKeys = Table()
        val whiteWidth = prefWidth / 7
        val blackWidth = whiteWidth / 2
        val blackPadding = whiteWidth - (blackWidth / 2)
        //blackKeys.space(10f)

        val createWhiteKey = { note: NoteOrderEnum ->
            val key = PianoKey(type = PianoKeyTypeEnum.White)
            key.onClick {
                this@PianoAnswerActor.fire(AnswerGivenEvent(note))
            }
            whiteKeys.add(key).grow()
        }

        val createBlackKey = { note: NoteOrderEnum, leftPad: Float, rightPad: Float ->
            val key = PianoKey(type = PianoKeyTypeEnum.Black)
            key.onClick {
                this@PianoAnswerActor.fire(AnswerGivenEvent(note))
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


    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height / 4f

}