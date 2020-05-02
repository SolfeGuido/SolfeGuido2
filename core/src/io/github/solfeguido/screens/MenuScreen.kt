package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.ui.STextButton
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*

class MenuScreen(context: Context) : UIScreen(context) {

    private lateinit var measure: MeasureActor

    override fun keyTyped(character: Char): Boolean {
        return when(character) {
            '+' -> measure.nextNote().let { true }
            '-' -> measure.prevNote().let { true }
            else -> super.keyTyped(character)
        }
    }

    private fun showCreditsDialog(){
        scene2d.zoomDialog {
            title("Credits")
            line("Made by : Azarias").align(Align.left)
            line("Made with : LibGdx & ktx").align(Align.left)
            line("Sounds : University of Iowa").align(Align.left)
            line("Icons : IconMoonApp").align(Align.left)
            this.borderButton("Ok").actor.icon(IconName.Check)
            key(Input.Keys.ENTER, true)
            key(Input.Keys.ESCAPE, false)
            setOrigin(Align.center)
            contentTable.pad(10f)
        }.show(this@MenuScreen.stage)
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
                            context.inject<AssetStorage>().get<Sound>(Constants.CLICK_SOUND).play()
                            showCreditsDialog()
                        }
                        pad(5f)
                        it.expandX().top().left()
                    }

                label("SolfeGuido")

                iconButton(IconName.Cog) {
                    label.setAlignment(Align.topRight)
                    onClick {
                        context.inject<AssetStorage>().get<Sound>(Constants.CLICK_SOUND).play()
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
            val padding = 0f
            stack {
                measure(ClefEnum.GClef) {
                    measure = this
                    //it.grow().pad(padding, 0f, padding, 0f)
                }
                table {
                    iconButton(IconName.Music, "iconBorderButtonStyle") {

                    }.pad(5f)
                }
                it.grow()
            }
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