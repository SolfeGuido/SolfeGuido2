package io.github.solfeguido.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.IconCheckBox
import io.github.solfeguido.actors.TextureCheckBox
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.enums.*
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import io.github.solfeguido.skins.getDefaultSkin
import ktx.actors.onClick
import ktx.inject.Context
import ktx.preferences.get
import ktx.scene2d.*

class OptionScreen(context: Context) : UIScreen(context) {

    private val preferencesManager: PreferencesManager = context.inject()

    private inline fun <S> KWidget<S>.iconPreference(
        icon: IconName,
        prefValue: SettingsEnum,
        crossinline callback: () -> Unit = {}
    ): IconCheckBox {
        return preferenceCheckBox(IconCheckBox(icon), prefValue, callback)
    }

    private inline fun <A : CheckBox, S> KWidget<S>.preferenceCheckBox(
        widget: A,
        prefValue: SettingsEnum,
        crossinline callback: () -> Unit = {}
    ): A {
        val actualValue = preferencesManager.get(prefValue)
        widget.isChecked = actualValue == prefValue
        widget.onClick {
            preferencesManager.set(prefValue)
            callback.invoke()
        }
        return actor(widget)
    }

    override fun setup(settings: StateParameter): Actor {
        val stateMachine: StateMachine = context.inject()
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
                        iconPreference(
                            IconName.Mobile,
                            Vibrations.Disabled
                        )
                        iconPreference(
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
                    iconPreference(IconName.RomanNotes, NoteStyle.RomanNotes)
                    iconPreference(IconName.LatinNotes, NoteStyle.LatinNotes)
                    iconPreference(IconName.EnglishNotes, NoteStyle.EnglishNotes)
                    it.right()
                }

                row()

                label("Buttons : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    iconPreference(IconName.NotesButton, ButtonStyle.NotesButton)
                    iconPreference(IconName.PianoKeys, ButtonStyle.PianoKeys)
                    iconPreference(IconName.PianoWithNotes, ButtonStyle.PianoWithNotes)
                    it.right()
                }

                row()

                label("Theme : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    // Could actually reload the scene maybe here ?
                    iconPreference(IconName.Sun, Theme.Light) {
                        stateMachine.switch<OptionScreen>(settings) {
                            Scene2DSkin.defaultSkin = getDefaultSkin(context.inject(), Theme.Light)
                        }
                    }
                    iconPreference(IconName.Moon, Theme.Dark) {
                        stateMachine.switch<OptionScreen>(settings) {
                            Scene2DSkin.defaultSkin = getDefaultSkin(context.inject(), Theme.Dark)
                        }
                    }
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

                label("Language : ", "contentLabelStyle") {
                    setFontScale(0.7f)
                }

                buttonGroup(1, 1) {
                    preferenceCheckBox(TextureCheckBox("En"), Language.English)
                    preferenceCheckBox(TextureCheckBox("Fr"), Language.French)
                    preferenceCheckBox(TextureCheckBox("Es"), Language.Spanish)
                    preferenceCheckBox(TextureCheckBox("It"), Language.Italian)
                    preferenceCheckBox(TextureCheckBox("Sv"), Language.Swedish)
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