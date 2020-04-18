package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.PianoKeyTypeEnum
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin
import ktx.style.get

const val BORDER_TICKNESS = 4f

class PianoKey(text: String = "", private val type : PianoKeyTypeEnum = PianoKeyTypeEnum.White) :
        Button(Scene2DSkin.defaultSkin.get<TextButton.TextButtonStyle>(type.style) ), KTable {


    private val backgroundColor
            get() = if(type == PianoKeyTypeEnum.Black) gCol("font") else gCol("background")


    init {
        this.isTransform = true
        this.setOrigin(Align.center)
        setupListeners()
    }

    private val borderDrawable = colorDrawable(1, 1,gCol("font"))
    private val backgroundDrawable = colorDrawable(1, 1, backgroundColor)

    override fun draw(batch: Batch, parentAlpha: Float) {
        backgroundDrawable.draw(batch, x, y, width * scaleX, height * scaleY )
        borderDrawable.draw(batch, x, y, width, BORDER_TICKNESS)
        borderDrawable.draw(batch, x, y + height - BORDER_TICKNESS, width, BORDER_TICKNESS)
        borderDrawable.draw(batch, x , y, BORDER_TICKNESS, height)
        borderDrawable.draw(batch, x + width - BORDER_TICKNESS, y, BORDER_TICKNESS, height)
        super.draw(batch, parentAlpha)
    }

    private fun setupListeners(){
        val self = this
        addListener(object: ClickListener() {

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                // TODO : emit event based on note index
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val res = super.touchDown(event, x, y, pointer, button)
                if(self.isDisabled) return res
                self.addAction(Actions.scaleTo(Constants.PIANO_PRESSED_SCALING, Constants.PIANO_PRESSED_SCALING, .2f, Interpolation.exp10Out))
                return res
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }

}