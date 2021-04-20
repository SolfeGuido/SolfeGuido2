package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.onAnswer
import io.github.solfeguido.factories.onResult
import io.github.solfeguido.factories.pianoAnswer
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*

class PlayScreen(context: Context) : UIScreen(context) {

    private lateinit var clef: ClefEnum
    private lateinit var measure: MeasureActor


    override fun create(settings: StateParameter) {
        clef = settings.getValue()
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
            label("SolfeGuido") {
                setPosition(this.x, this.y - this.height)
                addAction(
                    Actions.parallel(
                        Actions.fadeOut(0.2f, Interpolation.exp10Out),
                        Actions.moveBy(0f, this.height, 0.2f, Interpolation.exp10Out)
                    ),
                )
            }
            row()
            stack {
                measure = measure(clef) {
                    onResult {

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
                        measure.checkNote(it.note)
                    }
                }
            }
        }

        Gdx.input.inputProcessor = stage
    }
}