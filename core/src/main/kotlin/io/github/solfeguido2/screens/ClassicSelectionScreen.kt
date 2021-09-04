package io.github.solfeguido2.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido2.core.GameManager
import io.github.solfeguido2.core.PreferencesManager
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.KeySignatureEnum
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.factories.*
import io.github.solfeguido2.settings.GameSettings
import io.github.solfeguido2.settings.MeasureSettings
import io.github.solfeguido2.settings.TimeSettings
import io.github.solfeguido2.settings.gamemode.NoteGuessOptions
import io.github.solfeguido2.settings.generator.RandomGenerator
import io.github.solfeguido2.structures.Constants
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.*

class ClassicSelectionScreen(context: Context) : UIScreen(context) {

    private val stateMachine: StateMachine = context.inject()

    override fun setup(settings: StateParameter): Actor {

        val sPrefs = context.inject<PreferencesManager>()
        var selectedClef = ClefEnum.GClef
        var selectedKeySignature = KeySignatureEnum.CMajor
        var accidentalsEnabled = false
        var timeSettings = TimeSettings.ClassicCountdownMode

        val clefClick: (Actor, ClefEnum) -> Unit = { test, clef ->
            test.onClick {
                selectedClef = clef
            }
        }

        val timeClick: (Actor, TimeSettings) -> Unit = { btn, time ->
            btn.onClick {
                timeSettings = time
            }
        }

        val accidentalClick: (Actor, Boolean) -> Unit = { btn, enable ->
            btn.onClick {
                accidentalsEnabled = enable
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

                label(Nls.ModeClassic()) {
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
                        ClefEnum.values().forEach { clef ->
                            iconCheckBox(clef.icon) {
                                clefClick(this, clef)
                            }
                        }
                    }

                    row()
                    buttonGroup(1, 1) {
                        iconCheckBox(IconName.Speedometer) { timeClick(this, TimeSettings.ClassicCountdownMode) }
                        iconCheckBox(IconName.Infinity) { timeClick(this, TimeSettings.InfiniteMode) }
                    }
                    row()

                    buttonGroup(1, 1) {
                        iconCheckBox(IconName.Natural) { accidentalClick(this, false) }
                        iconCheckBox(IconName.SharpAccidental) { accidentalClick(this, true) }
                    }

                    row()

                    container {
                        pad(0f, 150f, 0f, 150f)
                        scrollPane {
                            buttonGroup(1, 1) {
                                KeySignatureEnum.values().forEach { signature ->
                                    textCheckBox(signature.name) {
                                        onClick {
                                            selectedKeySignature = signature
                                        }
                                    }
                                }
                            }
                        }
                    }

                    row()


                    borderButton(Nls.Play()) {
                        icon(IconName.Play, 0.9f).pad(5f)
                        pad(5f)

                        onClick {
                            val manager = GameManager(
                                context, GameSettings(
                                    timeSettings,
                                    NoteGuessOptions(
                                        listOf(
                                            MeasureSettings(
                                                selectedClef,
                                                signature = selectedKeySignature,
                                                //TODO: maybe let the user configure the high & low values ?
                                                generator = RandomGenerator(
                                                    selectedClef.minNote,
                                                    selectedClef.minNote + Constants.MAX_NOTE_SPAN,
                                                    accidentalsEnabled
                                                )
                                            )
                                        )
                                    ),
                                )
                            ) {
                                stateMachine.switch<ClassicSelectionScreen>()
                            }

                            stateMachine.switch<PlayScreen>(
                                StateParameter.witType(manager),
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