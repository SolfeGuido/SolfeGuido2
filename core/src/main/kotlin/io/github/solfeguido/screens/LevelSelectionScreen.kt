package io.github.solfeguido.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.core.LevelManager
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.factories.icon
import io.github.solfeguido.factories.iconButton
import io.github.solfeguido.settings.GameSettings
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table

class LevelSelectionScreen(context: Context) : UIScreen(context) {

    private val stateMachine: StateMachine = context.inject()

    override fun setup(settings: StateParameter): Actor {
        val clef = settings.get<ClefEnum>()
        val levelManager = context.inject<LevelManager>()
        val clefRequirements = levelManager.levelRequirements[clef]!!
        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {
                iconButton(IconName.Home) {
                    onClick {
                        stateMachine.switch<MenuScreen>(
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
                table {
                    pad(10f)
                    clefRequirements.forEachIndexed { index, t ->
                        val enabled = index == 0 || levelManager.levelResult(clef, index).correctGuesses >= t.minScore
                        for (star in 1..5) {
                            icon(IconName.FullStar) {
                                color = gCol("font")
                                it.pad(15f, 2f, 15f, 2f)
                            }
                        }

                        borderButton("Level ${index + 1}") {
                            pad(5f)
                            isDisabled = !enabled
                            icon(if (isDisabled) IconName.Lock else IconName.Play).right()
                            label.setAlignment(Align.right)
                            it.fill()

                            if (!isDisabled) {
                                onClick {
                                    val requirements = levelManager.generateLevel(clef, index)
                                    context.inject<StateMachine>().switch<PlayScreen>(
                                        StateParameter.witType(GameSettings.levelGame(requirements)),
                                        Align.right
                                    )
                                }
                            }
                        }
                        row()
                    }
                    center()
                }

                it.grow()
            }
        }

    }

    override fun back(): Boolean {
        stateMachine.switch<MenuScreen>(
            align = Align.top,
            param = StateParameter.witType(MenuScreen.VisibleMenu.LevelKeySelection)
        )
        return true
    }
}