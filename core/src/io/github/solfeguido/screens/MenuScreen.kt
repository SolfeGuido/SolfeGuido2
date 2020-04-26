package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.settings.TimeSettings
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class MenuScreen(context: Context) : UIScreen(context) {

    private lateinit var measure: MeasureActor

    override fun keyTyped(character: Char): Boolean {
        return when(character) {
            '+' -> measure.nextNote().let { true }
            '-' -> measure.prevNote().let { true }
            else -> super.keyTyped(character)
        }
    }

    override fun show() {
        super.show()
        info { "Init stage" }

        stage += scene2d.table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            slidingTable(Align.top) {
                debug = true

                    iconButton(IconName.Info) {
                        onClick {
                            //Slide to other state
                            context.inject<AssetManager>().get<Sound>(Constants.CLICK_SOUND).play()
                        }
                        pad(5f)
                        it.expandX().top().left()
                    }

                label("SolfeGuido") {
                }

                iconButton(IconName.Cog) {
                    label.setAlignment(Align.topRight)
                    onClick {
                        setOrigin(Align.center)
                        this.addAction(Actions.rotateBy(360f, 0.3f))
                        info { "Show options" }
                    }
                    pad(5f)
                    it.expandX().top().right()
                }
                pad(10f)
                it.expandX().fillX()
            }
            row()
            timer(context, TimeSettings())
            row()
            val padding = 0f
            measure(ClefEnum.GClef) {
                measure = this
                it.grow().pad(padding, 0f, padding, 0f)
            }
            row()
            buttonAnswer()
            row()
            slidingTable(Align.bottomLeft) {
                debug = true
                iconButton(IconName.Off){
                    label.setAlignment(Align.topRight)
                    onClick {
                        Gdx.app.exit()
                    }
                    pad(5f)
                    it.expandX().bottom().left()
                }
                pad(10f)
                it.expandX().fillX()
            }
        }
    }
}