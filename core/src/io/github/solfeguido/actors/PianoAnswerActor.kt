package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.PianoKeyTypeEnum
import io.github.solfeguido.ui.PianoKey


class PianoAnswerActor: Stack() {

    init {
        debug = false
        val whiteKeys = Table()
        val blackKeys = Table()
        val whiteWidth = prefWidth / 7
        val blackWidth = whiteWidth / 2
        val blackPadding = whiteWidth - (blackWidth / 2)
        //blackKeys.space(10f)

        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        createBlackKey(blackKeys, blackPadding, blackPadding/ 3)
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        createBlackKey(blackKeys, blackPadding / 3, blackPadding)
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        createBlackKey(blackKeys, blackPadding, blackPadding / 3)
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        createBlackKey(blackKeys, blackPadding / 3, blackPadding / 3)
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        createBlackKey(blackKeys, blackPadding / 3, blackPadding)
        whiteKeys.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(whiteKeys)
        this.add(blackKeys)
    }

    private fun createBlackKey(container: Table, leftPad: Float, rightPad: Float){
        container.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow().pad(0f, leftPad, 0f, rightPad).align(Align.top).height(3 * prefHeight / 4f)
    }


    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height / 4f

}