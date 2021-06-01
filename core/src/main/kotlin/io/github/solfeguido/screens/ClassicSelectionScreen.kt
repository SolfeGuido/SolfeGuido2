package io.github.solfeguido.screens

import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.factories.measure
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class ClassicSelectionScreen(context: Context) : UIScreen(context) {


    override fun show() {
        super.show()

        stage += scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {
                iconButton(IconName.ChevronLeft) {
                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>(StateParameter.witType(MenuScreen.VisibleMenu.Play))
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
                it.grow()
            }

        }
    }

}