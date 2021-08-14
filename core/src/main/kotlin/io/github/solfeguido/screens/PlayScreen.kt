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
import io.github.solfeguido.enums.Nls
import io.github.solfeguido.factories.*
import io.github.solfeguido.ui.ZoomDialog
import ktx.actors.onClick
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.stack
import ktx.scene2d.table

class PlayScreen(context: Context) : UIScreen(context) {

    private val preferencesManager: PreferencesManager = context.inject()
    private lateinit var gameManager: GameManager

    private val pauseCallbacks = gdxArrayOf<() -> Unit>()
    private val resumeCallbacks = gdxArrayOf<() -> Unit>()

    private fun onPause(callback: () -> Unit) = pauseCallbacks.add(callback)
    private fun onResume(callback: () -> Unit) = resumeCallbacks.add(callback)

    override fun setup(settings: StateParameter): Actor {
        gameManager = settings.get()
        val timer = gameManager.settings.time
        Gdx.input.inputProcessor = null

        val answerType = preferencesManager.buttonStyle
        val noteStyle = preferencesManager.noteStyle
        lateinit var scoreActor: ScoreActor
        gameManager.start()
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
                    gameManager.end()
                    scene2d.zoomDialog {
                        closeOptions = setOf(ZoomDialog.ClosingOptions.ESCAPE, ZoomDialog.ClosingOptions.CROSS)
                        title("Finished !")
                        line("your score is ... ${scoreActor.score}")
                        borderButton("Ok").actor.icon(IconName.Check, 0.5f)
                        setOrigin(Align.center)

                        onDialogHide {
                            gameManager.exit()
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

                gameManager.populateScene(this) { result ->
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
                    else -> buttonAnswer(noteStyle, gameManager.hasAccidentals)
                }


                answerer.onAnswer { answer ->
                    gameManager.validateAnswer(answer)
                    true
                }
            }
            onPause {
                timer.pause()
                gameManager.pause()
            }
            onResume {
                timer.resume()
                gameManager.resume()
            }
        }
    }

    override fun back(): Boolean {
        if (gameManager.isPaused) {
            resumeCallbacks.forEach { it.invoke() }
        } else {
            pauseCallbacks.forEach { it.invoke() }
            scene2d.zoomDialog {
                title(Nls.Paused())
                line("Close this dialog to resume the game")
                borderButton(Nls.Resume())
                val btn = borderButton(Nls.Menu.invoke())
                btn.actor.onClick { gameManager.exit() }


                onDialogHide { back() }
            }.show(this.stage)
        }
        return true
    }
}