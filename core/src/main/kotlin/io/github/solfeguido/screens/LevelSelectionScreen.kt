package io.github.solfeguido.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.iconButton
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.*

class LevelSelectionScreen(context: Context) : UIScreen(context) {


    override fun setup(settings: StateParameter): Actor {
        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {
                iconButton(IconName.Home) {
                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>(
                            StateParameter.witType(MenuScreen.VisibleMenu.LevelKeySelection),
                            Align.top
                        )
                    }
                }

                label("Level selection") {
                    it.expandX()
                }

                pad(10f)
                it.expandX().fillX()
            }

            row()

            scrollPane {
                this.setScrollbarsVisible(false)
                fadeScrollBars = false
                setOrigin(Align.center)
                verticalGroup {
                    for(i in 1..20) {
                        borderButton("Level $i") {
                            icon(IconName.FullStar)
                        }
                    }

                    fill()
                    center()
                    padTop(10f)
                    padBottom(10f)
                    space(10f)
                }

                it.grow()
            }
        }
    }
}