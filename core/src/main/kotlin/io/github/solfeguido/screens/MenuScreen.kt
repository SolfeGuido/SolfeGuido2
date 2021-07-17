package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.zoomDialog
import io.github.solfeguido.settings.MeasureSettings
import ktx.actors.div
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.scene2d.*

class MenuScreen(context: Context) : UIScreen(context) {

    enum class VisibleMenu {
        Root,
        Play,
        LevelKeySelection
    }

    private lateinit var measure: MeasureActor
    private lateinit var backButton: Actor
    private lateinit var shownMenu: VisibleMenu

    private val stateMachine: StateMachine = context.inject()
    private val widgetStack = gdxArrayOf<Actor>()

//    override fun keyTyped(character: Char): Boolean {
//        return when (character) {
//            //'+' -> measure.nextNote().let { true }
//            //'-' -> measure.prevNote().let { true }
//            else -> super.keyTyped(character)
//        }
//    }

    private fun showCreditsDialog() {
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

    private fun updateBackButton() {
        // backButton.clearActions()
        backButton += if (widgetStack.size > 1) {
            if (backButton.isVisible) return
            (Actions.moveTo(
                -backButton.width,
                20f
            ) + Actions.visible(true)) + Actions.moveTo(5f, 20f, 0.4f, Interpolation.fade)
        } else {
            Actions.moveTo(
                -backButton.width,
                20f,
                0.4f,
                Interpolation.fade
            ) + Actions.visible(false)
        }
    }

    private fun pushActor(actor: Actor) {
        val top = widgetStack.last()
        top.clearActions()
        actor.clearActions()
        val shift = -stage.width

        top += Actions.moveBy(shift, 0f, 0.4f, Interpolation.exp10Out) + Actions.visible(
            widgetStack.size != 1
        )
        actor += (Actions.moveTo(stage.width, 0f) / Actions.scaleTo(
            0.5f,
            1f
        )) + Actions.visible(true) +
                Actions.moveTo(0f, 0f, 0.4f, Interpolation.exp10Out) /
                Actions.scaleTo(1f, 1f, 0.4f, Interpolation.exp10Out)
        widgetStack.add(actor)
        updateBackButton()
    }

    private fun popActor(): Boolean {
        if (widgetStack.size < 2) return false
        val top = widgetStack.pop()
        val current = widgetStack.last()
        top.clearActions()
        current.clearActions()
        top += Actions.moveTo(
            stage.width,
            0f,
            0.8f,
            Interpolation.exp10Out
        ) + Actions.visible(false)
        current += (Actions.moveTo(-stage.width, 0f) / Actions.scaleTo(0f, 1f)) + Actions.visible(
            true
        ) +
                (Actions.scaleTo(1f, 1f, 0.4f, Interpolation.exp10Out) /
                        Actions.moveTo(0f, 0f, 0.4f, Interpolation.exp10Out))
        updateBackButton()
        return true
    }


    override fun setup(settings: StateParameter): Actor {
        shownMenu = settings.getOrDefault(VisibleMenu.Root)

        lateinit var playMenu: Table
        lateinit var playOptions: ScrollPane
        lateinit var levelKeyOptions: ScrollPane
        lateinit var keyOptions: Actor
        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            slidingTable(Align.top) {
                backButton = iconButton(IconName.ChevronLeft) {
                    onClick {
                        popActor()
                    }
                    pad(5f)
                    it.expandX().top().left()
                    isVisible = false
                }


                label("SolfeGuido")

                iconButton(IconName.Cog) {
                    label.setAlignment(Align.topRight)
                    onClick {
                        context.inject<AssetStorage>().get<Sound>(Constants.CLICK_SOUND).play()
                        this.addAction(Actions.rotateBy(360f, 0.3f))
                        stateMachine.switch<OptionScreen>()
                    }
                    pad(5f)
                    it.expandX().top().right()
                }
                pad(10f)
                it.expandX().fillX()
            }
            row()
            stack {
                measure = measure(MeasureSettings(ClefEnum.GClef), context.inject<PreferencesManager>().noteStyle)
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
                    isVisible = shownMenu == VisibleMenu.Root
                }
                widgetStack.add(playMenu)
                playOptions = scrollPane {
                    this.setScrollbarsVisible(false)
                    fadeScrollBars = false
                    setOrigin(Align.center)
                    isVisible = shownMenu == VisibleMenu.Play
                    verticalGroup {
                        borderButton("Classic") {
                            icon(IconName.Music, 0.9f).left()
                            label.setAlignment(Align.right)
                            left()
                            pad(10f)
                            onClick {
                                val label = Label("Loading", Scene2DSkin.defaultSkin, "contentLabelStyle")
                                stateMachine.switch<ClassicSelectionScreen>(align = Align.top, actor = label)

                            }
                        }
                        borderButton("Levels") {
                            icon(IconName.FullStar, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                pushActor(levelKeyOptions)
                            }
                        }
//                        borderButton("Ear training") {
//                            icon(IconName.Eacute, 0.9f).left()
//                            label.setAlignment(Align.right)
//                            pad(10f)
//                            onClick {
//                                //TODO go to play scene
//                                info { "Doing ear training" }
//                            }
//                        }
                        // borderButton("Custom") {
                        //     icon(IconName.Road, 0.9f).left()
                        //     label.setAlignment(Align.right)
                        //     pad(10f)
                        //     onClick {
                        //         // TODO: load list of custom games
                        //         info { "Playing custom gamemode" }
                        //     }
                        // }
                        // borderButton("Create custom") {
                        //     icon(IconName.Direction, 0.9f).left()
                        //     label.setAlignment(Align.right)
                        //     pad(10f)
                        //     onClick {
                        //         context.inject<StateMachine>().switch<GameCreationScreen>()
                        //     }
                        // }
//                        borderButton("Key Signature") {
//                            icon(IconName.SharpAccidental, 0.9f).left()
//                            label.setAlignment(Align.right)
//                            pad(10f)
//                            onClick { pushActor(keyOptions) }
//                        }
                        // Later
                        // borderButton("Competitive") {
                        //     icon(IconName.Speedometer, 0.9f).left()
                        //     label.setAlignment(Align.right)
                        //     pad(10f)
                        //     onClick { pushActor(classicOptions)  }
                        // }
                        fill()
                        center()
                        padTop(10f)
                        padBottom(10f)
                        this.space(10f)
                    }
                }

                levelKeyOptions = scrollPane {
                    this.setScrollbarsVisible(false)
                    fadeScrollBars = false
                    setOrigin(Align.center)
                    isVisible = shownMenu == VisibleMenu.LevelKeySelection
                    verticalGroup {

                        ClefEnum.values().forEach { clef ->
                            borderButton(clef.name) {
                                icon(clef.icon)
                                label.setAlignment(Align.right)
                                pad(10f)
                                onClick {
                                    stateMachine.switch<LevelSelectionScreen>(
                                        align = Align.bottom,
                                        param = StateParameter.witType(clef)
                                    )
                                }
                            }
                        }

                        fill()
                        center()
                        padTop(10f)
                        padBottom(10f)
                        this.space(10f)
                    }
                }

                if (shownMenu == VisibleMenu.Play) {
                    widgetStack.add(playOptions)
                    updateBackButton()
                } else if (shownMenu == VisibleMenu.LevelKeySelection) {
                    widgetStack.add(playOptions)
                    widgetStack.add(levelKeyOptions)
                    updateBackButton()
                }
                it.grow()
            }
        }
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int) =
        velocityX > 1000f && popActor()


    override fun back(): Boolean {
        if (widgetStack.size == 1) {
            Gdx.app.exit()
            return true
        }
        return popActor()
    }

}