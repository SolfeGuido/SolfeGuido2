package io.github.solfeguido.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.iconCheckBox
import io.github.solfeguido.factories.measure
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.*

class ClassicSelectionScreen(context: Context) : UIScreen(context) {


    override fun setup(settings: StateParameter): Actor {
        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {
                iconButton(IconName.Home) {
                    onClick {
                        context.inject<StateMachine>()
                            .switch<MenuScreen>(StateParameter.witType(MenuScreen.VisibleMenu.Play))
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
                measure(ClefEnum.GClef)

                table {
                    buttonGroup(1, 1) {

                        iconCheckBox(IconName.GClef, ClefEnum.GClef) {

                        }
                        iconCheckBox(IconName.FClef, ClefEnum.FClef) {

                        }

                        iconCheckBox(IconName.CClef3, ClefEnum.CClef3) {

                        }

                        iconCheckBox(IconName.CClef4, ClefEnum.CClef4) {

                        }
                        onChange {
                            //println(this.buttonGroup.checked)
                        }
                    }

                    row()
                    buttonGroup(1, 1) {
                        iconCheckBox(IconName.Infinity, 0) {

                        }

                        iconCheckBox(IconName.Speedometer, 2) {

                        }
                    }
                }

                it.grow()
            }

        }
    }

}