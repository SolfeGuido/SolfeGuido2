package io.github.solfeguido.screens

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.scene2d
import ktx.scene2d.table

class TransitionScreen(context: Context) : UIScreen(context) {

    class TransitionData<T : UIScreen>(
        val origin: Int,
        val clazz: Class<T>,
        val parameter: StateParameter = StateParameter.empty(),
        val actor: Actor? = null
    )

    private fun originToCoordinates(origin: Int) = when {
        Align.isLeft(origin) -> Vector2(-Constants.WIDTH.toFloat(), 0f)
        Align.isRight(origin) -> Vector2(Constants.WIDTH.toFloat(), 0f)
        Align.isTop(origin) -> Vector2(0f, Constants.HEIGHT.toFloat())
        Align.isBottom(origin) -> Vector2(0f, -Constants.HEIGHT.toFloat())
        else -> throw Error("$origin is not a valid origin")
    }

    override fun setup(settings: StateParameter): Actor {
        val transition: TransitionData<UIScreen> = settings.get()
        val stateMachine: StateMachine = context.inject()


        val origin = originToCoordinates(transition.origin)
        val res = scene2d.table {
            isTransform = true
            background = colorDrawable(gCol("background"), Constants.WIDTH, Constants.WIDTH)
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
        }

        transition.actor?.let { res.add(it) }

        res += Actions.moveTo(origin.x, origin.y) + Actions.moveTo(
            0f,
            0f,
            0.3f,
            Interpolation.exp10Out
        ) + Actions.run {
            stateMachine.replaceBeforeLast(transition.clazz, transition.parameter)
        } + Actions.delay(0.1f) + Actions.moveTo(-origin.x, -origin.y, 0.4f, Interpolation.exp10Out) + Actions.run {
            stateMachine.pop()
        }

        return res
    }

}