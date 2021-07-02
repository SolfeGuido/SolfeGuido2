package io.github.solfeguido.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.IconCheckBox
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.SolfeGuidoPreferences
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import ktx.actors.onClick
import ktx.inject.Context
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*

class OptionScreen(context: Context) : UIScreen(context) {

    private val preferences: Preferences = context.inject()

    private inline fun <S, reified T : Any> KWidget<S>.preferenceCheckBox(
        icon: IconName,
        prefName: String,
        prefValue: T
    ): IconCheckBox {
        val actualValue: T? = preferences[prefName]
        val res = IconCheckBox(icon)
        res.isChecked = actualValue == prefValue
        res.onClick {
            preferences[prefName] = prefValue
            preferences.flush()
        }
        return actor(res)
    }

    override fun setup(settings: StateParameter): Actor {

        val preferences: Preferences = context.inject()
        val vibrations: String = preferences["vibrations"] ?: "1"
        val noteStyle = preferences["noteStyle"] ?: SolfeGuidoPreferences.NoteStyle.EnglishNotes.name
        val buttons = preferences["buttonStyle"] ?: SolfeGuidoPreferences.ButtonStyle.NotesButton.name

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
                            Constants.Preferences.VIBRATIONS,
                            SolfeGuidoPreferences.Vibrations.Enabled.value
                        )
                        preferenceCheckBox(
                            IconName.MobileVibrate,
                            Constants.Preferences.VIBRATIONS,
                            SolfeGuidoPreferences.Vibrations.Disabled.value
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
                        Constants.Preferences.NOTE_STYLE,
                        SolfeGuidoPreferences.NoteStyle.RomanNotes
                    )
                    preferenceCheckBox(
                        IconName.LatinNotes,
                        Constants.Preferences.NOTE_STYLE,
                        SolfeGuidoPreferences.NoteStyle.LatinNotes
                    )
                    preferenceCheckBox(
                        IconName.EnglishNotes,
                        Constants.Preferences.NOTE_STYLE,
                        SolfeGuidoPreferences.NoteStyle.EnglishNotes
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
                        Constants.Preferences.BUTTON_STYLE,
                        SolfeGuidoPreferences.ButtonStyle.NotesButton
                    )
                    preferenceCheckBox(
                        IconName.PianoKeys,
                        Constants.Preferences.BUTTON_STYLE,
                        SolfeGuidoPreferences.ButtonStyle.PianoKeys
                    )
                    preferenceCheckBox(
                        IconName.PianoWithNotes,
                        Constants.Preferences.BUTTON_STYLE,
                        SolfeGuidoPreferences.ButtonStyle.PianoWithNotes
                    )
                    it.right()
                }

                row()

                label("Theme : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.Sun, Constants.Preferences.THEME, SolfeGuidoPreferences.Theme.Light)
                    preferenceCheckBox(IconName.Moon, Constants.Preferences.THEME, SolfeGuidoPreferences.Theme.Dark)
                    it.right()
                }

                row()
                label("Sound : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
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

    override fun back(): Boolean {
        //TODO: could save the last state of the menu screen to return to it instead of the root
        context.inject<StateMachine>().switch<MenuScreen>()
        return true
    }

}