package io.github.solfeguido2.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.iconButton
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class CreditsScreen(context: Context) : UIScreen(context) {

    private lateinit var menuTarget: MenuScreen.VisibleMenu

    override fun setup(settings: StateParameter): Actor {
        menuTarget = settings.get()
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

                label(Nls.Credits()) {
                    it.expandX()
                }
                pad(10f)
                it.fillX().expandX()
            }

            row()

            table {
                label(Nls.MadeWith())
                label("LibGDX")
                row()
                label(Nls.By())
                label("AzariasB")
                row()
                label(Nls.Icons())
                label("IconMoonApp")
                row()
                label(Nls.Sounds())
                label("University of Iowa")

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

    override fun back(): Boolean {
        context.inject<StateMachine>().switch<MenuScreen>(StateParameter.witType(menuTarget))
        return true
    }
}