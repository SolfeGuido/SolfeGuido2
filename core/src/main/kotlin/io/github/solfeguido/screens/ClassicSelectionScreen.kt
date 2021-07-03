package io.github.solfeguido.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.SPreferences
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import io.github.solfeguido.factories.measure
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
import ktx.actors.onClick
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.scene2d.*

class ClassicSelectionScreen(context: Context) : UIScreen(context) {

    private var selectedClef = ClefEnum.GClef
    private var timeSettings = TimeSettings.ClassicCountdownMode

    private val stateMachine: StateMachine = context.inject()

    override fun setup(settings: StateParameter): Actor {

        val sPrefs = context.inject<SPreferences>()

        val clefClick: (Actor, ClefEnum) -> Unit = { test, clef ->
            test.onClick {
                this@ClassicSelectionScreen.selectedClef = clef
            }
        }

        val timeClick: (Actor, TimeSettings) -> Unit = { btn, time ->
            btn.onClick {
                this@ClassicSelectionScreen.timeSettings = time
            }
        }

        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)



            slidingTable(Align.top) {
                iconButton(IconName.Home) {
                    onClick {
                        stateMachine.switch<MenuScreen>(
                            StateParameter.witType(MenuScreen.VisibleMenu.Play),
                            Align.bottom
                        )
                    }
                    pad(5f)
                    it.top().left()
                }

                label("Classic mode") {
                    it.expandX()
                }
                pad(10f)
                it.expandX().fillX()
            }
            row()
            stack {
                measure(MeasureSettings(ClefEnum.GClef), sPrefs.noteStyle)

                table {
                    buttonGroup(1, 1) {

                        iconCheckBox(IconName.GClef) {
                            clefClick(this, ClefEnum.GClef)
                        }
                        iconCheckBox(IconName.FClef) {
                            clefClick(this, ClefEnum.FClef)
                        }
                        iconCheckBox(IconName.CClef3) {
                            clefClick(this, ClefEnum.CClef3)
                        }
                        iconCheckBox(IconName.CClef4) {
                            clefClick(this, ClefEnum.CClef4)
                        }
                    }

                    row()
                    buttonGroup(1, 1) {
                        iconCheckBox(IconName.Speedometer) { timeClick(this, TimeSettings.ClassicCountdownMode) }
                        iconCheckBox(IconName.Infinity) { timeClick(this, TimeSettings.InfiniteMode) }
                    }
                    row()

                    borderButton("Play") {
                        icon(IconName.Play, 0.9f).pad(5f)
                        pad(5f)

                        onClick {
                            stateMachine.switch<PlayScreen>(
                                StateParameter.witType(
                                    GameSettings(
                                        options = NoteGuessOptions(
                                            gdxArrayOf(
                                                MeasureSettings(this@ClassicSelectionScreen.selectedClef)
                                            )
                                        ),
                                        time = timeSettings
                                    )
                                ),
                                Align.right
                            )
                        }
                    }
                }

                it.grow()
            }
        }
    }

    override fun back(): Boolean {
        stateMachine.switch<MenuScreen>(
            align = Align.bottom,
            param = StateParameter.witType(MenuScreen.VisibleMenu.Play)
        )
        return true
    }

}