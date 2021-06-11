package io.github.solfeguido.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ObjectMap
import io.github.solfeguido.screens.TransitionScreen
import io.github.solfeguido.screens.UIScreen
import ktx.collections.gdxMapOf
import ktx.collections.set
import ktx.inject.Context

typealias ScreenProvider = () -> UIScreen

class StateMachine(val context: Context) : Screen {
    private val stack = mutableListOf<UIScreen>()
    private val changes = mutableListOf<() -> Unit>()
    val constructors: ObjectMap<Class<out UIScreen>, ScreenProvider> = gdxMapOf()


    private fun performChanges() {
        while (changes.isNotEmpty()) {
            changes.removeFirst().invoke()
        }
    }

    internal inline fun <reified Type : UIScreen> addScreen(): StateMachine {
        val constructor = Type::class.java.getDeclaredConstructor(Context::class.java)
        constructors[Type::class.java] = { constructor.newInstance(context) }
        return this
    }

    internal inline fun <reified Type : UIScreen> addCurrentScreen(): StateMachine {
        addScreen<Type>()
        return push(Type::class.java)
    }

    private fun <Type : UIScreen> createScreen(
        type: Class<Type>,
        param: StateParameter,
    ): UIScreen = constructors[type]().also { it.create(param) }

    fun peek() = stack.last()

    fun first() = stack.first()

    fun <Type : UIScreen> push(type: Class<Type>, param: StateParameter = StateParameter.empty()): StateMachine {
        changes.add {
            stack.add(createScreen(type, param))
        }
        return this
    }

    internal inline fun <reified Type : UIScreen> push(param: StateParameter) = push(Type::class.java, param)

    internal fun pop(showAgain: Boolean = false): StateMachine {
        changes.add {
            stack.last().hide()
            stack.last().dispose()
            stack.removeAt(stack.size - 1)
            if (showAgain) {
                stack.last().show()
            }
        }
        return this
    }

    internal inline fun <reified Type : UIScreen> switch(
        param: StateParameter = StateParameter.empty(),
        align: Int = Align.left,
        actor: Actor? = null,
    ): StateMachine {
        val typeClass = Type::class.java
        if (!constructors.containsKey(typeClass)) {
            throw Exception("The constructor $typeClass is not configured in the state machine")
        }

        changes.add {
            stack.add(
                createScreen(
                    TransitionScreen::class.java, StateParameter.witType(
                        TransitionScreen.TransitionData(
                            align,
                            Type::class.java,
                            param,
                            actor
                        )
                    )
                )
            )
        }
        return this
    }

    fun replaceBeforeLast(clazz: Class<UIScreen>, param: StateParameter = StateParameter.empty()): StateMachine {
        if (!constructors.containsKey(clazz)) {
            throw Exception("The constructor $clazz is not configured in the state machine")
        }

        changes.add {
            val beforeLast = stack[stack.size - 2]
            val nwScreen = createScreen(clazz, param)
            stack[stack.size - 2] = nwScreen
            beforeLast.dispose()
        }

        return this
    }

    override fun hide() = Unit
    override fun show() = Unit

    override fun render(delta: Float) {
        stack.forEach { it.render(delta) }
        performChanges()
    }

    override fun pause() {
        stack.forEach { it.pause() }
    }

    override fun resume() {
        stack.forEach { it.resume() }
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log("RESIZE", "Resize to $width, $height")
        stack.forEach { it.resize(width, height) }
    }

    override fun dispose() {
        stack.forEach { it.dispose() }
    }

}