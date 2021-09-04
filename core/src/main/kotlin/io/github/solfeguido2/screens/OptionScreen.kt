package io.github.solfeguido2.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.I18NBundleLoader
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import io.github.solfeguido2.actors.IconCheckBox
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.core.PreferencesManager
import io.github.solfeguido2.enums.*
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.iconButton
import io.github.solfeguido2.factories.iconCheckBox
import io.github.solfeguido2.skins.getDefaultSkin
import io.github.solfeguido2.structures.Constants
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.assets.async.toIdentifier
import ktx.inject.Context
import ktx.scene2d.*
import java.util.*

class OptionScreen(context: Context) : UIScreen(context) {

    private val preferencesManager: PreferencesManager = context.inject()
    private lateinit var menu: MenuScreen.VisibleMenu

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
        menu = settings.get()
        val stateMachine: StateMachine = context.inject()
        val assetManager: AssetStorage = context.inject()
        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {

                iconButton(IconName.Home) {
                    onClick {
                        back()
                    }
                    pad(5f)
                    it.top().left()
                }

                label(Nls.Settings()) {
                    it.expandX()
                }

                pad(10f)
                it.fillX().expandX()
            }
            row()
            table {
                if (Gdx.app.type == Application.ApplicationType.Android) {
                    label(Nls.SettingsVibrations(), "contentLabelStyle") {
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

                        it.left()
                    }
                    row()
                }

                label(Nls.SettingsNoteStyle(), "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    iconPreference(IconName.RomanNotes, NoteStyle.RomanNotes)
                    iconPreference(IconName.LatinNotes, NoteStyle.LatinNotes)
                    iconPreference(IconName.EnglishNotes, NoteStyle.EnglishNotes)
                    it.left()
                }

                row()

                label(Nls.SettingsAnswer(), "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    iconPreference(IconName.NotesButton, ButtonStyle.NotesButton)
                    iconPreference(IconName.PianoKeys, ButtonStyle.PianoKeys)
                    iconPreference(IconName.PianoWithNotes, ButtonStyle.PianoWithNotes)
                    it.left()
                }

                row()

                label(Nls.SettingsTheme(), "contentLabelStyle") {
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

                    it.left()
                }

                row()
                label(Nls.SettingsSound(), "contentLabelStyle") {
                    setFontScale(0.7f)
                }
                buttonGroup(1, 1) {
                    // Todo : maybe change with a proper slider
                    iconPreference(IconName.VolumeOn, SoundEnabled.Enabled)
                    iconPreference(IconName.VolumeOff, SoundEnabled.Disabled)
                    it.left()
                }

                row()

                label(Nls.SettingsLanguage(), "contentLabelStyle") {
                    setFontScale(0.7f)
                }

                buttonGroup(1, 1) {
                    Language.values().forEach { lang ->
                        val checkBox = CheckBox("", Scene2DSkin.defaultSkin, lang.code)
                        checkBox.setOrigin(Align.center)
                        checkBox.onChange {
                            val scale = if (isChecked) 0.4f else 1f
                            isTransform = true
                            this += Actions.scaleTo(scale, scale, 0.2f, Interpolation.exp10Out)
                        }
                        checkBox.pad(2f)

                        preferenceCheckBox(checkBox, lang) {
                            stateMachine.switch<OptionScreen>(settings) {
                                val bundleDescriptor = assetManager.getAssetDescriptor<I18NBundle>(
                                    "i18n/nls",
                                    I18NBundleLoader.I18NBundleParameter(Locale(lang.code))
                                )
                                Nls.i18nBundle = assetManager[bundleDescriptor.toIdentifier()]
                            }
                        }
                    }
                }

                row()

                borderButton(Nls.Menu()) {
                    this.pad(10f)
                    it.colspan(2)
                    it.pad(10f)

                    onClick {
                        back()
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
        context.inject<StateMachine>().switch<MenuScreen>(StateParameter.witType(menu))
        return true
    }

}