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
import io.github.solfeguido.config.SPreferences
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import ktx.actors.onClick
import ktx.inject.Context
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*

class OptionScreen(context: Context) : UIScreen(context) {

    private val preferences: SPreferences = context.inject()

    private inline fun <S, reified T : Enum<T>> KWidget<S>.preferenceCheckBox(
        icon: IconName,
        prefValue: T
    ): IconCheckBox {
        val actualValue = preferences.get<T>()
        val res = IconCheckBox(icon)
        res.isChecked = actualValue == prefValue
        res.onClick {
            preferences.set(prefValue)
        }
        return actor(res)
    }

    override fun setup(settings: StateParameter): Actor {

        val preferences: Preferences = context.inject()
        val vibrations: String = preferences["vibrations"] ?: "1"
        val noteStyle = preferences["noteStyle"] ?: SPreferences.NoteStyle.EnglishNotes.name
        val buttons = preferences["buttonStyle"] ?: SPreferences.ButtonStyle.NotesButton.name

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
                            SPreferences.Vibrations.Enabled
                        )
                        preferenceCheckBox(
                            IconName.MobileVibrate,
                            SPreferences.Vibrations.Disabled
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
                        SPreferences.NoteStyle.RomanNotes
                    )
                    preferenceCheckBox(
                        IconName.LatinNotes,
                        SPreferences.NoteStyle.LatinNotes
                    )
                    preferenceCheckBox(
                        IconName.EnglishNotes,
                        SPreferences.NoteStyle.EnglishNotes
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
                        SPreferences.ButtonStyle.NotesButton
                    )
                    preferenceCheckBox(
                        IconName.PianoKeys,
                        SPreferences.ButtonStyle.PianoKeys
                    )
                    preferenceCheckBox(
                        IconName.PianoWithNotes,
                        SPreferences.ButtonStyle.PianoWithNotes
                    )
                    it.right()
                }

                row()

                label("Theme : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    preferenceCheckBox(IconName.Sun, SPreferences.Theme.Light)
                    preferenceCheckBox(IconName.Moon, SPreferences.Theme.Dark)
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

    override fun dispose() {
        preferences.save()
        super.dispose()
    }

    override fun back(): Boolean {
        //TODO: could save the last state of the menu screen to return to it instead of the root
        context.inject<StateMachine>().switch<MenuScreen>()
        return true
    }

}