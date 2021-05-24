package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.settings.GameModeSettings
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*

class PlayScreen(context: Context) : UIScreen(context) {

    private lateinit var game: IGameModeOptions
    private lateinit var timer: TimeSettings


    override fun create(settings: StateParameter) {
        val options: GameModeSettings = settings.getValue()
        game = options.options
        timer = options.time
        super.create(settings)
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = null

        info { "Init stage" }

        stage += scene2d.table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            val timer = timer(context, timer) {
                onTimerEnd {
                    scene2d.zoomDialog {
                        title("Test")
                        line("You finished the game")
                        line("your score is ...")
                        line("Made in x seconds")
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

        Gdx.input.inputProcessor = stage
    }
}