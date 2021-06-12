package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import ktx.inject.Context
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class PlayScreen(context: Context) : UIScreen(context) {

    private lateinit var game: IGameModeOptions
    private lateinit var timer: TimeSettings


    override fun setup(settings: StateParameter): Actor {
        val options: GameSettings = settings.get()
        game = options.options
        timer = options.time
        Gdx.input.inputProcessor = null


        return scene2d.table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            val timer = timer(context, timer) {
                onTimerEnd {
                    game.endGame()
                    scene2d.zoomDialog {
                        title("Finished !")
                        line("your score is ...")
                        borderButton("Ok").actor.icon(IconName.Check, 0.5f)
                        setOrigin(Align.center)

                        onDialogHide {
                            //TODO: smooth transition
                            context.inject<StateMachine>().switch<MenuScreen>()
                        }
                    }.show(this.stage)
                }
            }
            row()
            stack {
                game.populateScene(this) { result ->
                    if (!result.isCorrect) {
                        timer.wrong()
                    } else {
                        timer.correct()
                    }
                }
                it.grow()
            }
            row()
            container {
                align(Align.top)
                pianoAnswer {
                    onAnswer {
                        // Handle answer based on what was generated
                        game.validateNote(it.note)
                    }
                }
            }
        }

    }
}