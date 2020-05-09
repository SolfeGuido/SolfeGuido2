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
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*

class MenuScreen(context: Context) : UIScreen(context) {

    private lateinit var measure: MeasureActor

    private val widgetStack = gdxArrayOf<Actor>()

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

    private fun pushActor(actor: Actor) {
        val top = widgetStack.last()
        top.clearActions()
        actor.clearActions()
        top += Actions.moveTo(-stage.width, 0f, 0.4f, Interpolation.exp10Out)
        actor += (Actions.moveTo(stage.width, 0f) / Actions.scaleTo(0f, 1f)) +
                Actions.moveTo(0f, 0f, 0.4f, Interpolation.swingOut) /
                Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut)
        widgetStack.add(actor)
    }

    private fun popActor(): Boolean {
        if(widgetStack.size < 2) return false
        val top = widgetStack.pop()
        val current = widgetStack.last()
        top.clearActions()
        current.clearActions()
        top += Actions.moveTo(stage.width, 0f, 0.8f, Interpolation.exp10Out)
        current += (Actions.moveTo(-stage.width, 0f) / Actions.scaleTo(0f, 1f)) +
                (Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut) /
                    Actions.moveTo(0f, 0f, 0.4f, Interpolation.swingOut) )
        return true
    }


    override fun show() {
        super.show()
        lateinit var playMenu: Table
        lateinit var classicOptions: Group
        lateinit var playOptions: ScrollPane
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
                                onClick { pushActor(playOptions) }
                                pad(25f)
                                it.pad(10f)
                            }
                            borderButton("Leaderboard") {
                                icon(IconName.List, 0.9f).pad(5f)
                                pad(5f)
                                it.pad(10f)
                            }
                        }
                        classicOptions = scrollPane {
                            setScale(0f)
                            verticalGroup {
                                setOrigin(Align.center)
                                fill()
                                center()
                                this.space(10f)
                                borderButton("Clé de sol") {
                                    icon(IconName.GClef, 0.9f).pad(5f)
                                    pad(5f)
                                }
                                borderButton("Clé de Fa") {
                                    icon(IconName.FClef, 0.9f).pad(5f)
                                    pad(5f)
                                }
                                borderButton("Clé d'UT3") {
                                    icon(IconName.CClef3, 0.9f).pad(5f)
                                    pad(5f)
                                }
                                borderButton("Clé d'UT4") {
                                    icon(IconName.CClef3, 0.9f).pad(5f)
                                    pad(5f)
                                }
                            }
                        }
                        widgetStack.add(playMenu)
                        it.grow()
                    }
                }
                playOptions = scrollPane {
                    this.setScrollbarsVisible(false)
                    fadeScrollBars = false
                    verticalGroup {
                        borderButton("Classic") {
                            icon(IconName.Music, 0.9f).left()
                            label.setAlignment(Align.right)
                            left()
                            pad(10f)
                            onClick { pushActor(classicOptions) }
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
                if(widgetStack.size == 1) {
                    if(isBackKey(event.keyCode)) {
                        Gdx.app.exit()
                        return true
                    }
                    return false
                }
                if(isBackKey(event.keyCode) && !popActor()) {
                    return true
                }
                return false
            }
        })

    }

}