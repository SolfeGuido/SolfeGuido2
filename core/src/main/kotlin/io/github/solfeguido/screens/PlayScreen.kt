package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.ScoreActor
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.core.StatsManager
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.SolfeGuidoPreferences
import io.github.solfeguido.factories.*
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import ktx.inject.Context
import ktx.preferences.get
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class PlayScreen(context: Context) : UIScreen(context) {

    private lateinit var game: IGameModeOptions
    private lateinit var timer: TimeSettings

    private val preferences: Preferences = context.inject()
    private val stats: StatsManager = context.inject()

    override fun setup(settings: StateParameter): Actor {
        val options: GameSettings = settings.get()
        game = options.options
        timer = options.time
        Gdx.input.inputProcessor = null

        val answerType = preferences[Constants.Preferences.BUTTON_STYLE] ?: SolfeGuidoPreferences.ButtonStyle.PianoKeys.name
        lateinit var scoreActor: ScoreActor

        return scene2d.table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            val timer = timer(context, timer) {
                onTimerEnd {
                    game.endGame(context, scoreActor.score)

                    scene2d.zoomDialog {
                        title("Finished !")
                        line("your score is ... ${scoreActor.score}")
                        borderButton("Ok").actor.icon(IconName.Check, 0.5f)
                        setOrigin(Align.center)

                        onDialogHide {
                            context.inject<StateMachine>().switch<MenuScreen>(
                                align = Align.top,
                                param = StateParameter.witType(MenuScreen.VisibleMenu.Play)
                            )
                        }
                    }.show(this.stage)
                }
            }
            row()
            stack {
                scoreActor = score(0) {
                    setFillParent(true)
                    align(Align.topLeft)
                    pad(2f, 10f, 0f, 10f)
                    setPosition(0f, 0f)
                }
                game.populateScene(this) { result ->
                    stats.registerResult(result)
                    if (!result.isCorrect) {
                        scoreActor.negativeAnimation()
                        timer.wrong()
                    } else {
                        scoreActor.addPoint()
                        timer.correct()
                    }
                }
                it.grow()
            }
            row()
            container {
                align(Align.top)

                val answerer = when (answerType) {
                    SolfeGuidoPreferences.ButtonStyle.PianoKeys.name -> pianoAnswer()
                    SolfeGuidoPreferences.ButtonStyle.PianoWithNotes.name -> pianoAnswer(showNotes = true)
                    else -> buttonAnswer()
                }

                answerer.onAnswer {
                    game.validateNote(it.note)
                }
            }
        }
    }
}