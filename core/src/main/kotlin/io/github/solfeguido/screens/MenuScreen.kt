package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.zoomDialog
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

    enum class VisibleMenu {
        Root,
        Play
    }

    private lateinit var measure: MeasureActor
    private lateinit var backButton: Actor
    private lateinit var shownMenu: VisibleMenu

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
        if (widgetStack.size > 1) {
            backButton.clearActions()
            backButton += (Actions.moveTo(
                -backButton.width,
                backButton.y
            ) + Actions.visible(true)) + Actions.moveTo(5f, backButton.y, 0.4f, Interpolation.fade)
        } else {
            backButton.clearActions()
            backButton += Actions.moveTo(
                -backButton.width,
                backButton.y,
                0.4f,
                Interpolation.fade
            ) + Actions.visible(false)
        }
    }

    private fun pushActor(actor: Actor) {
        val top = widgetStack.last()
        top.clearActions()
        actor.clearActions()
        val shift = if (widgetStack.size == 1) -stage.width else -(top.width / 4f)

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

    override fun create(settings: StateParameter) {
        shownMenu = settings.getOrDefault(VisibleMenu.Root)
        super.create(settings)
    }

    override fun show() {
        super.show()
        lateinit var playMenu: Table
        lateinit var playOptions: ScrollPane
        lateinit var keyOptions: Actor
        stage += scene2d.table {
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
                                context.inject<StateMachine>()
                                    .switch<ClassicSelectionScreen>(align = Align.top, actor = label)
                            }
                        }
                        borderButton("Levels") {
                            icon(IconName.FullStar, 0.9f).left()
                            label.setAlignment(Align.right)
                            pad(10f)
                            onClick {
                                context.inject<StateMachine>().switch<LevelSelectionScreen>(align = Align.bottom)
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
                        //         //TODO: smooth transition
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
                if (shownMenu == VisibleMenu.Play) {
                    widgetStack.add(playOptions)
                    updateBackButton()
                }
                it.grow()
            }
        }
        addListeners()
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int) =
        velocityX > 1000f && popActor()

    private fun addListeners() {
        val isBackKey: (Int) -> Boolean = { it == Input.Keys.ESCAPE || it == Input.Keys.BACK }
        stage.addListener(object : InputListener() {
            override fun keyTyped(event: InputEvent, character: Char): Boolean {
                if (widgetStack.size == 1) {
                    if (isBackKey(event.keyCode)) {
                        Gdx.app.exit()
                        return true
                    }
                    return false
                }
                if (isBackKey(event.keyCode) && !popActor()) {
                    return true
                }
                return false
            }
        })

    }

}