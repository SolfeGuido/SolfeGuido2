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
import io.github.solfeguido.enums.PreferenceEnum
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

    private inline fun <S> KWidget<S>.preferenceCheckBox(
        icon: IconName,
        prefName: String,
        prefValue: PreferenceEnum
    ): IconCheckBox {
        val actualValue: Int? = preferences[prefName]
        val res = IconCheckBox(icon)
        res.isChecked = actualValue == prefValue.value
        res.onClick {
            preferences[prefName] = prefValue.value
            preferences.flush()
        }
        return actor(res)
    }

    override fun setup(settings: StateParameter): Actor {

        val preferences: Preferences = context.inject()
        val vibrations: Boolean = preferences["vibrations"] ?: true
        val noteStyle: Int = preferences["noteStyle"] ?: 0
        val buttons: Int = preferences["buttonStyle"] ?: 0

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
                        preferenceCheckBox(IconName.Mobile, Constants.Preferences.VIBRATIONS, PreferenceEnum.Enabled)
                        preferenceCheckBox(IconName.MobileVibrate, Constants.Preferences.VIBRATIONS, PreferenceEnum.Disabled)
                    }
                    row()
                }

                label("Note style : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.RomanNotes, Constants.Preferences.NOTE_STYLE, PreferenceEnum.RomanNotes)
                    preferenceCheckBox(IconName.LatinNotes, Constants.Preferences.NOTE_STYLE, PreferenceEnum.LatinNotes)
                    preferenceCheckBox(IconName.EnglishNotes, Constants.Preferences.NOTE_STYLE, PreferenceEnum.EnglishNotes)
                    it.right()
                }

                row()

                label("Buttons : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.NotesButton, Constants.Preferences.BUTTON_STYLE, PreferenceEnum.NotesButton)
                    preferenceCheckBox(IconName.PianoKeys, Constants.Preferences.BUTTON_STYLE, PreferenceEnum.PianoKeys)
                    preferenceCheckBox(IconName.PianoWithNotes, Constants.Preferences.BUTTON_STYLE, PreferenceEnum.PianoWithNotes)
                    it.right()
                }

                row()

                label("Theme : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.Sun, Constants.Preferences.THEME, PreferenceEnum.Light)
                    preferenceCheckBox(IconName.Moon, Constants.Preferences.THEME, PreferenceEnum.Dark)
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

}