package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.solfeguido.enums.PianoKeyTypeEnum
import io.github.solfeguido.ui.PianoKey


class PianoAnswerActor: Table() {

    init {
        debug = false
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.Black)).grow()
        this.add(PianoKey(type = PianoKeyTypeEnum.White)).grow()
    }

    override fun getPrefWidth() = Gdx.graphics.width.toFloat()
    override fun getPrefHeight() = Gdx.graphics.height / 4f

}