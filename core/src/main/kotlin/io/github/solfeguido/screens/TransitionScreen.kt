package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gdxFloatArrayOf
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.scene2d
import ktx.scene2d.table

class TransitionScreen(context: Context) : UIScreen(context) {

    private lateinit var mainWidget: Table

    class TransitionData<T : UIScreen>(
        val origin: Int,
        val clazz: Class<T>,
        val parameter: StateParameter = StateParameter.empty(),
        val actor: Actor? = null
    )

    private fun scaleValues(origin: Int) = when {
        Align.isLeft(origin) -> gdxFloatArrayOf(
            0f, 0f,// Start position
            0f, 1f,// End position
            1f, 1f//
        )
        else -> throw Error("nope")
    }

    private fun originToCoordinates(origin: Int) = when {
        Align.isLeft(origin) -> Vector2(-Gdx.graphics.width.toFloat(), 0f)
        Align.isRight(origin) -> Vector2(Gdx.graphics.width.toFloat(), 0f)
        Align.isTop(origin) -> Vector2(0f, Gdx.graphics.height.toFloat())
        Align.isBottom(origin) -> Vector2(0f, -Gdx.graphics.height.toFloat())
        else -> throw Error("$origin is not a valid origin")
    }

    override fun create(settings: StateParameter) {
        super.create(settings)
        val transition: TransitionData<UIScreen> = settings.get()
        val stateMachine: StateMachine = context.inject()

        transition.actor?.let { mainWidget.add(it) }

        val origin = originToCoordinates(transition.origin)

        this.mainWidget += Actions.moveTo(origin.x, origin.y) + Actions.moveTo(
            0f,
            0f,
            0.3f,
            Interpolation.exp10Out
        ) + Actions.run {
            stateMachine.replaceBeforeLast(transition.clazz, transition.parameter)
        } + Actions.delay(0.1f) + Actions.moveTo(-origin.x, -origin.y, 0.3f, Interpolation.exp10Out) + Actions.run {
            stateMachine.pop()
        }
    }

    override fun show() {
        super.show()

        mainWidget = scene2d.table {
            isTransform = true
            background = colorDrawable(Color.WHITE, Gdx.graphics.width, Gdx.graphics.height)
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
        }
        stage += mainWidget
    }
}