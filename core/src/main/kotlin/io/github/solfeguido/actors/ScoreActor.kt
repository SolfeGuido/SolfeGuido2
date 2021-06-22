package io.github.solfeguido.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.Scene2DSkin

class ScoreActor(var score: Int = 0) : Table() {

    private val label: Label =
        Label("$score", Scene2DSkin.defaultSkin, "contentLabelStyle").also { add(it).top().left() }


    fun addPoint(point: Int = 1) {
        score += point
        label.setText("$score")
        positiveAnimation()
    }

    fun positiveAnimation() {
        // Bounce up animation
        val move = this.label.height / 2f
        this.label += Actions.moveBy(0f, move, 0.1f, Interpolation.circleOut) +
                Actions.moveBy(0f, -move / 2f, 0.1f, Interpolation.circleOut) +
                Actions.moveBy(0f, move / 2f, 0.1f, Interpolation.circleOut) +
                Actions.moveBy(0f, -move / 4f, 0.1f, Interpolation.circleOut)
    }


    fun loosePoint(point: Int = 1) {
        score -= point
        label.setText("$score")
        negativeAnimation()
    }

    fun negativeAnimation() {
        // Shake animation
        val move = this.label.width / 2f

        this.label += Actions.moveBy(-move, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(move * 2, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(-move * 2.5f, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(move * 2.5f, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(-move * 1.5f, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(move, 0f, 0.1f, Interpolation.linear) +
                Actions.moveBy(-move * .5f, 0f, 0.1f, Interpolation.linear)
    }

}