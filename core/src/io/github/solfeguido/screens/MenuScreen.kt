package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.ui.STextButton
import ktx.actors.div
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*

class MenuScreen(context: Context) : UIScreen(context) {

    private lateinit var measure: MeasureActor
    private lateinit var playMenu: Table
    private lateinit var playOptions: ScrollPane

    private val showingOptions
        get() = playOptions.x in -10f..10f

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
            this.borderButton("Ok").actor.icon(IconName.Check, 0.5f)
            key(Input.Keys.ENTER, true)
            key(Input.Keys.ESCAPE, false)
            setOrigin(Align.center)
            contentTable.pad(10f)
        }.show(this@MenuScreen.stage)
    }

    private fun showPlayOptions() {
        playMenu.clearActions()
        playOptions.clearActions()
        playMenu += Actions.scaleTo(0f, 0f, 0.4f, Interpolation.exp10Out) / Actions.moveTo(-stage.width, 0f, 0.4f, Interpolation.exp10Out)
        playOptions += (Actions.moveTo(stage.width,0f) / Actions.scaleTo(0f, 1f)) +
                Actions.moveTo(0f, 0f, 0.4f, Interpolation.swingOut) /
                Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut)
    }

    private fun showPlayMenu() {
        playMenu.clearActions()
        playOptions.clearActions()
        playOptions += Actions.moveTo(playOptions.width, 0f, 0.8f, Interpolation.exp10Out)
        playMenu +=  Actions.moveTo(0f, 0f, 0.4f, Interpolation.swingOut) /
                Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut)
    }

    override fun show() {
        super.show()
        stage += scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            slidingTable(Align.top) {
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
            stack {
                table {
                    stack {
                        measure = measure(ClefEnum.GClef)
                        playMenu = table {
                            borderButton("Stats") {
                                icon(IconName.PointsChart, 0.9f).pad(5f)
                                pad(5f)
                                it.pad(10f)
                            }
                            borderButton("Play") {
                                icon(IconName.Play, 0.9f).pad(5f)
                                onClick { showPlayOptions() }
                                pad(25f)
                                it.pad(10f)
                            }
                            borderButton("Leaderboard") {
                                icon(IconName.List, 0.9f).pad(5f)
                                pad(5f)
                                it.pad(10f)
                            }
                        }
                        it.grow()
                    }
                    row()
                    slidingTable(Align.bottomLeft) {
                        iconButton(IconName.Off) {
                            label.setAlignment(Align.topRight)
                            onClick { Gdx.app.exit() }
                            pad(5f)
                            it.expandX().bottom().left()
                        }
                        it.expandX().fillX()
                        pad(10f)
                    }
                }
                scrollPane {
                    playOptions = this
                    verticalGroup {
                        borderButton("Classic") {
                            icon(IconName.Music, 0.9f).left()
                            label.setAlignment(Align.right)
                            left()
                            pad(10f)
                            onClick {
                                info { "Doing classic mode" }
                            }
                        }
                        borderButton("Levels") {
                            icon(IconName.FullStar, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Playing levels" }
                            }
                        }
                        borderButton("Competitive") {
                            icon(IconName.Speedometer, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Doing competitive" }
                            }
                        }
                        borderButton("Ear training") {
                            icon(IconName.Eacute, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Doing ear training" }
                            }
                        }
                        borderButton("Custom") {
                            icon(IconName.Road, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Playing custom gamemode" }
                            }
                        }
                        borderButton("Create custom") {
                            icon(IconName.Direction, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Creating custom gamemode" }
                            }
                        }
                        borderButton("FreePlay") {
                            icon(IconName.Infinity, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Playing infinity" }
                            }
                        }
                        borderButton("Key Signature") {
                            icon(IconName.SharpAccidental, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                info { "Playing Guess the accidental" }
                            }
                        }
                        borderButton("Back") {
                            icon(IconName.Home, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                showPlayMenu()
                            }
                        }
                        fill()
                        center()
                        padTop(10f)
                        padBottom(10f)
                        this.space(10f)
                    }
                    setOrigin(Align.center)
                    setScale(0f)
                }
                it.grow()
            }
        }
        addListeners()
    }

    private fun addListeners(){
        val isBackKey : (Int) -> Boolean = { it == Input.Keys.ESCAPE || it == Input.Keys.BACK}
        stage.addListener(object: InputListener() {
            override fun keyTyped(event: InputEvent, character: Char): Boolean {
                if(!showingOptions) {
                    if(isBackKey(event.keyCode)) {
                        Gdx.app.exit()
                        return true
                    }
                    return false
                }
                if(isBackKey(event.keyCode)) {
                    showPlayMenu()
                    return true
                }
                return false
            }
        })

    }

}