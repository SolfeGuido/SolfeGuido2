package io.github.solfeguido.screens

import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.iconButton
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class LevelSelectionScreen(context: Context) : UIScreen(context) {


    override fun show() {
        super.show()

        stage += scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {
                iconButton(IconName.Home) {
                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>(StateParameter.witType(MenuScreen.VisibleMenu.Play))
                    }
                }

                label("Level selection") {
                    it.expandX()
                }

                pad(10f)
                it.expandX().fillX()
            }

            row()

            stack {
                label("Test")
                it.grow()
            }
        }
    }
}