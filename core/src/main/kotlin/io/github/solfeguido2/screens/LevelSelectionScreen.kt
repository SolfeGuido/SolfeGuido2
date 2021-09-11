package io.github.solfeguido2.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido2.core.GameManager
import io.github.solfeguido2.core.LevelManager
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.gCol
import io.github.solfeguido2.factories.icon
import io.github.solfeguido2.factories.iconButton
import io.github.solfeguido2.settings.GameSettings
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

                label(Nls.ModeLevel()) {
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
                    clefRequirements.forEachIndexed { index, _ ->
                        val enabled = levelManager.hasAccessTo(clef, index)
                        borderButton(Nls.Level(index + 1)) {
                            pad(5f)
                            isDisabled = !enabled
                            icon(if (isDisabled) IconName.Lock else IconName.Play).right()
                            label.setAlignment(Align.right)
                            it.fill()

                            if (!isDisabled) {
                                onClick {
                                    val requirements = levelManager.generateLevel(clef, index)
                                    val gameSettings = GameSettings.levelGame(requirements)
                                    val manager = GameManager(context, gameSettings) {
                                        stateMachine.switch<LevelSelectionScreen>(StateParameter.witType(clef))
                                    }
                                    stateMachine.switch<PlayScreen>(
                                        StateParameter.witType(manager),
                                        Align.right
                                    )
                                }
                            }

                            it.pad(5f)
                        }

                        levelManager.levelResult(clef, index)?.let { gameStats ->
                            levelManager.levelRequirements[clef]?.get(index)?.let { requirements ->
                                var totalStars = 1
                                if (gameStats.wrongGuesses == 0) {
                                    totalStars++
                                }
                                if (gameStats.score > requirements.minScore) {
                                    totalStars++
                                }
                                for (star in 1..totalStars) {
                                    icon(IconName.FullStar) {
                                        color = gCol("font")
                                        it.pad(15f, 2f, 15f, 2f)
                                    }
                                }
                                for (empty in (totalStars+1)..3) {
                                    icon(IconName.EmptyStar) {
                                        color = gCol("font")
                                        it.pad(15f, 2f, 15f, 2f)
                                    }
                                }
                            }
                        } ?: kotlin.run {
                            for (star in 1..3) {
                                icon(IconName.EmptyStar) {
                                    color = gCol("font")
                                    it.pad(15f, 2f, 15f, 2f)
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