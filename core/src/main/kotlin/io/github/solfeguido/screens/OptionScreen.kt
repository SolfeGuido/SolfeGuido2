package io.github.solfeguido.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import io.github.solfeguido.factories.measure
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.buttonGroup
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class OptionScreen(context: Context) : UIScreen(context) {

    override fun setup(settings: StateParameter): Actor {

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
                    buttonGroup(1, 1) {
                        iconCheckBox(IconName.Mobile)
                        iconCheckBox(IconName.MobileVibrate)
                    }
                    row()
                }

                buttonGroup(1, 1) {
                    iconCheckBox(IconName.RomanNotes)
                    iconCheckBox(IconName.LatinNotes)
                    iconCheckBox(IconName.EnglishNotes)
                }

                row()

                buttonGroup(1, 1) {
                    iconCheckBox(IconName.NotesButton)
                    iconCheckBox(IconName.PianoKeys)
                    iconCheckBox(IconName.PianoWithNotes)
                }
                it.grow()
            }

        }
    }

}