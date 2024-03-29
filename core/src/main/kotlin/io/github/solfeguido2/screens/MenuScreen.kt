package io.github.solfeguido2.screens

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
import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.core.GameManager
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.core.PreferencesManager
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.iconButton
import io.github.solfeguido2.factories.measure
import io.github.solfeguido2.settings.GameSettings
import io.github.solfeguido2.settings.MeasureSettings
import ktx.actors.div
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.scene2d.*

class MenuScreen(context: Context) : UIScreen(context) {

    enum class VisibleMenu(val previous: () -> VisibleMenu, val next: () -> VisibleMenu) {
        Root({ Root }, { Play }),
        Play({ Root }, { LevelKeySelection }),
        LevelKeySelection({ Play }, { LevelKeySelection }),
        AutoKeySelection({ Play }, { AutoKeySelection })
    }

    private lateinit var measure: MeasureActor
    private lateinit var backButton: Actor
    private lateinit var shownMenu: VisibleMenu

    private val stateMachine: StateMachine = context.inject()
    private val widgetStack = gdxArrayOf<Actor>()


    private fun updateBackButton() {
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
        shownMenu = shownMenu.next()
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
        shownMenu = shownMenu.previous()
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
        lateinit var autoKeyOptions: ScrollPane
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
                        this.addAction(Actions.rotateBy(360f, 0.3f))
                        stateMachine.switch<OptionScreen>(StateParameter.witType(shownMenu))
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
                    borderButton(Nls.Statistics()) {
                        icon(IconName.PointsChart, 0.9f).pad(5f)
                        pad(5f)
                        it.pad(10f)
                        onClick {
                            stateMachine.switch<StatsScreen>()

                        }
                    }
                    borderButton(Nls.Play()) {
                        icon(IconName.Play, 0.9f).pad(5f)
                        onClick {
                            pushActor(playOptions)
                        }
                        pad(25f)
                        it.pad(10f)
                    }
                    borderButton(Nls.Scoreboard()) {
                        isDisabled = true
                        icon(IconName.List, 0.9f).pad(5f)
                        pad(5f)
                        it.pad(10f)
//                        onClick {
//                            stateMachine.switch<ScoreboardScreen>(StateParameter.witType(shownMenu))
//                        }
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
                        borderButton(Nls.Classic()) {
                            icon(IconName.Music, 0.9f).left()
                            label.setAlignment(Align.right)
                            left()
                            pad(10f)
                            onClick {
                                val label = Label(Nls.Loading(), Scene2DSkin.defaultSkin, "contentLabelStyle")
                                stateMachine.switch<ClassicSelectionScreen>(align = Align.top, actor = label)

                            }
                        }
                        borderButton(Nls.Levels()) {
                            icon(IconName.FullStar, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                pushActor(levelKeyOptions)
                            }
                        }
                        borderButton("Auto") {
                            icon(IconName.Road, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                pushActor(autoKeyOptions)
                            }
                        }
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

                autoKeyOptions = scrollPane {
                    this.setScrollbarsVisible(false)
                    fadeScrollBars = false
                    setOrigin(Align.center)
                    isVisible = shownMenu == VisibleMenu.AutoKeySelection
                    verticalGroup {

                        ClefEnum.values().forEach { clef ->
                            borderButton(clef.name) {
                                icon(clef.icon)
                                label.setAlignment(Align.right)
                                pad(10f)
                                onClick {
                                    val manager = GameManager(context, GameSettings.autoGame(clef)) {
                                        stateMachine.switch<MenuScreen>(StateParameter.witType(VisibleMenu.AutoKeySelection))
                                    }
                                    stateMachine.switch<PlayScreen>(
                                        StateParameter.witType(manager),
                                        align = Align.right,
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
                } else if(shownMenu == VisibleMenu.AutoKeySelection) {
                    widgetStack.add(playOptions)
                    widgetStack.add(autoKeyOptions)
                    updateBackButton()
                }
                it.grow()
            }
            row()
            slidingTable(Align.bottom) {
                iconButton(IconName.Off) {
                    onClick {
                        Gdx.app.exit()
                    }
                    pad(5f)
                    it.expandX().bottom().left()
                }

                iconButton(IconName.Info) {
                    onClick {
                        stateMachine.switch<CreditsScreen>(StateParameter.witType(shownMenu))
                    }
                    pad(5f)
                    it.expandX().bottom().right()

                }
                it.expandX().fillX()
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