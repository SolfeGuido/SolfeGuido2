package io.github.solfeguido.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.IconCheckBox
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.enums.*
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import ktx.actors.onClick
import ktx.inject.Context
import ktx.preferences.get
import ktx.scene2d.*

class OptionScreen(context: Context) : UIScreen(context) {

    private val preferencesManager: PreferencesManager = context.inject()

    private inline fun <S> KWidget<S>.preferenceCheckBox(
        icon: IconName,
        prefValue: SettingsEnum
    ): IconCheckBox {
        val actualValue = preferencesManager.get(prefValue)
        val res = IconCheckBox(icon)
        res.isChecked = actualValue == prefValue
        res.onClick {
            preferencesManager.set(prefValue)
        }
        return actor(res)
    }

    override fun setup(settings: StateParameter): Actor {

        val preferences: Preferences = context.inject()

        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {

                iconButton(IconName.Home) {
                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>()
                    }
                    pad(5f)
                    it.top().left()
                }

                label("Options") {
                    it.expandX()
                }

                pad(10f)
                it.fillX().expandX()
            }
            row()
            table {
                if (Gdx.app.type == Application.ApplicationType.Android) {
                    label("Vibrations : ", "contentLabelStyle") {
                        setFontScale(0.7f)
                    }
                    buttonGroup(1, 1) {
                        preferenceCheckBox(
                            IconName.Mobile,
                            Vibrations.Disabled
                        )
                        preferenceCheckBox(
                            IconName.MobileVibrate,
                            Vibrations.Enabled
                        )
                    }
                    row()
                }

                label("Note style : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(
                        IconName.RomanNotes,
                        NoteStyle.RomanNotes
                    )
                    preferenceCheckBox(
                        IconName.LatinNotes,
                        NoteStyle.LatinNotes
                    )
                    preferenceCheckBox(
                        IconName.EnglishNotes,
                        NoteStyle.EnglishNotes
                    )
                    it.right()
                }

                row()

                label("Buttons : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(
                        IconName.NotesButton,
                        ButtonStyle.NotesButton
                    )
                    preferenceCheckBox(
                        IconName.PianoKeys,
                        ButtonStyle.PianoKeys
                    )
                    preferenceCheckBox(
                        IconName.PianoWithNotes,
                        ButtonStyle.PianoWithNotes
                    )
                    it.right()
                }

                row()

                label("Theme : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.Sun, Theme.Light)
                    preferenceCheckBox(IconName.Moon, Theme.Dark)
                    it.right()
                }

                row()
                label("Sound : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    // Todo : maybe change with a proper slider
                    iconCheckBox(IconName.VolumeOn)
                    iconCheckBox(IconName.VolumeOff)
                    it.right()
                }

                row()

                borderButton("Menu") {
                    this.pad(10f)
                    it.colspan(2)
                    it.pad(10f)

                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>()
                    }
                }

                it.grow()
            }

        }
    }

    override fun dispose() {
        preferencesManager.save()
        super.dispose()
    }

    override fun back(): Boolean {
        //TODO: could save the last state of the menu screen to return to it instead of the root
        context.inject<StateMachine>().switch<MenuScreen>()
        return true
    }

}