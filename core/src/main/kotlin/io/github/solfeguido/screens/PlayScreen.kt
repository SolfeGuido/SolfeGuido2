package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.ScoreActor
import io.github.solfeguido.core.GameManager
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ButtonStyle
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.*
import io.github.solfeguido.ui.ZoomDialog
import ktx.inject.Context
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class PlayScreen(context: Context) : UIScreen(context) {

    private val preferencesManager: PreferencesManager = context.inject()

    override fun setup(settings: StateParameter): Actor {
        val manager: GameManager = settings.get()
        val timer = manager.settings.time
        Gdx.input.inputProcessor = null

        val answerType = preferencesManager.buttonStyle
        val noteStyle = preferencesManager.noteStyle
        lateinit var scoreActor: ScoreActor
        manager.start()
        return scene2d.table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            val timer = timer(context, timer) {
                onTimerEnd {
                    /*
                    End game parameters:
                        - the context (for example, for the level manager to be updated) - could be given when creating the objet
                        - a "gameStats" object :
                            - wrongNotes
                            - correctNotes
                            - timePlayed
                            - timeWon
                            - timeLost

                     */
                    manager.end()
                    scene2d.zoomDialog {
                        closeOptions = setOf(ZoomDialog.ClosingOptions.ESCAPE, ZoomDialog.ClosingOptions.CROSS)
                        title("Finished !")
                        line("your score is ... ${scoreActor.score}")
                        borderButton("Ok").actor.icon(IconName.Check, 0.5f)
                        setOrigin(Align.center)

                        onDialogHide {
                            manager.exit()
                            true
                        }
                    }.show(this.stage)
                    true
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

                manager.populateScene(this) { result ->
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
                    ButtonStyle.PianoKeys -> pianoAnswer(noteStyle)
                    ButtonStyle.PianoWithNotes -> pianoAnswer(noteStyle, showNotes = true)
                    else -> buttonAnswer(noteStyle, manager.hasAccidentals)
                }


                answerer.onAnswer { answer ->
                    manager.validateAnswer(answer)
                    true
                }
            }
        }
    }
}